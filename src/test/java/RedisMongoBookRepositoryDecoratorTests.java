import com.mongodb.client.MongoDatabase;
import org.example.Redis.RedisConnectionManager;
import org.example.exceptions.BookAlreadyRentedException;
import org.example.exceptions.TooManyException;
import org.example.models.*;
import org.example.repositories.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import redis.clients.jedis.JedisPooled;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RedisMongoBookRepositoryDecoratorTests {

    private final JedisPooled jedisPool = RedisConnectionManager.getJedis();
    private final RedisBookRepository redisRepository = new RedisBookRepository(jedisPool);;
    private final MgdBookRepository mgdRepository = new MgdBookRepository();
    private final RedisMongoBookRepositoryDecorator decorator = new RedisMongoBookRepositoryDecorator(redisRepository, mgdRepository);
    private final MgdRentRepository mgdRentRepository = new MgdRentRepository(decorator);
    private final MgdClientRepository mgdClientRepository = new MgdClientRepository();

    @BeforeEach
    public void setUp() {
        RedisConnectionManager.initConnectionFromConfig("src/main/resources/application.properties");
        redisRepository.clearCache();
        mgdRepository.getMongoDatabase().getCollection("books").drop();
        mgdRentRepository.getMongoDatabase().getCollection("rents").drop();
        mgdClientRepository.getMongoDatabase().getCollection("clients").drop();
    }

    @Test
    public void testFindById_WhenBookExistsInCache() {
        Book book = new Book("Book in Cache");
        decorator.create(book);

        Book result = decorator.findById(book.getEntityId());

        assertNotNull(result);
        assertEquals(book.getTitle(), result.getTitle());
    }

    @Test
    public void testFindById_WhenBookNotInCache_ButExistsInMongo() {
        Book book = new Book("Book in Mongo");
        mgdRepository.create(book);

        Book result = decorator.findById(book.getEntityId());

        assertNotNull(result);
        assertEquals(book.getTitle(), result.getTitle());

        // Sprawdzamy, czy książka została dodana do cache
        Book cachedBook = redisRepository.findById(book.getEntityId());
        assertNotNull(cachedBook);
        assertEquals(book.getTitle(), cachedBook.getTitle());
    }

    @Test
    public void testFindById_WhenBookNotInCacheOrMongo() {
        UniqueIdMgd bookId = new UniqueIdMgd();

        Book result = decorator.findById(bookId);

        assertNull(result);
    }

    @Test
    public void testFindAll_WhenCacheIsEmpty() {
        Book book1 = new Book( "Book 1");
        Book book2 = new Book( "Book 2");
        mgdRepository.create(book1);
        mgdRepository.create(book2);

        List<Book> result = decorator.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    public void testCreate() {
        Book book = new Book("New Book");

        decorator.create(book);

        // Sprawdzamy, czy książka została zapisana w obu repozytoriach
        Book fromRedis = redisRepository.findById(book.getEntityId());
        assertNotNull(fromRedis);
        assertEquals(book.getTitle(), fromRedis.getTitle());

        Book fromMongo = mgdRepository.findById(book.getEntityId());
        assertNotNull(fromMongo);
        assertEquals(book.getTitle(), fromMongo.getTitle());
    }

    @Test
    public void testUpdate() {
        Book book = new Book( "Book to Update");
        decorator.create(book);

        book.setTitle("Updated Book");
        boolean result = decorator.update(book);

        assertTrue(result);

        Book fromRedis = redisRepository.findById(book.getEntityId());
        assertNotNull(fromRedis);
        assertEquals("Updated Book", fromRedis.getTitle());

        Book fromMongo = mgdRepository.findById(book.getEntityId());
        assertNotNull(fromMongo);
        assertEquals("Updated Book", fromMongo.getTitle());
    }

    @Test
    public void testDelete() {
        Book book = new Book("Book to Delete");
        decorator.create(book);

        boolean result = decorator.delete(book);

        assertTrue(result);

        Book fromRedis = redisRepository.findById(book.getEntityId());
        assertNull(fromRedis);

        Book fromMongo = mgdRepository.findById(book.getEntityId());
        assertNull(fromMongo);
    }

    @Test
    public void testFindById_AfterCacheFlush_FallsBackToMongoDB() {
        Book book = new Book("Book in MongoDB");
        decorator.create(book);

        Book cachedBook = redisRepository.findById(book.getEntityId());
        assertNotNull(cachedBook);
        assertEquals(book.getTitle(), cachedBook.getTitle());

        redisRepository.clearCache();

        cachedBook = redisRepository.findById(book.getEntityId());
        assertNull(cachedBook);

        Book fetchedBook = decorator.findById(book.getEntityId());

        assertNotNull(fetchedBook);
        assertEquals(book.getTitle(), fetchedBook.getTitle());
    }

    @Test
    public void testCreateRent_UpdatesBookIsRentedFlag() throws TooManyException, BookAlreadyRentedException {
        Book book = new Book( "Test Book");
        decorator.create(book);

        Client client = new Client("Test", "Client", new Student());
        mgdClientRepository.create(client);
        LocalDate beginDate = LocalDate.now();
        LocalDate endDate = beginDate.plusDays(30);
        Rent rent = new Rent(client.getEntityId(), book.getEntityId(), beginDate, endDate);

        // Tworzymy wynajem (to spowoduje zmianę pola isRented książki na true w Redis i MongoDB)
        mgdRentRepository.create(rent);

        Book updatedBook = decorator.findById(book.getEntityId());
        assertNotNull(updatedBook, "Book should exist in the repository.");
        assertTrue(updatedBook.isRented(), "Book isRented flag should be true after rent creation.");

        Book cachedBook = redisRepository.findById(book.getEntityId());
        assertNotNull(cachedBook, "Book should be in the Redis cache.");
        assertTrue(cachedBook.isRented(), "Book isRented flag should be true in Redis after rent creation.");
    }

    @Test
    public void testDeleteRent_UpdatesBookIsRentedFlag() throws BookAlreadyRentedException, TooManyException {
        Book book = new Book("Test Book");
        decorator.create(book);

        Client client = new Client("Test", "Client2", new NonStudent());
        mgdClientRepository.create(client);
        LocalDate beginDate = LocalDate.now();
        LocalDate endDate = beginDate.plusDays(30);
        Rent rent = new Rent(client.getEntityId(), book.getEntityId(), beginDate, endDate);
        mgdRentRepository.create(rent);

        // Usuwamy wynajem (powinna nastąpić zmiana flagi isRented na false)
        mgdRentRepository.delete(rent);

        Book updatedBook = decorator.findById(book.getEntityId());
        assertNotNull(updatedBook, "Book should exist in the repository.");
        assertFalse(updatedBook.isRented(), "Book isRented flag should be false after rent deletion.");

        Book cachedBook = redisRepository.findById(book.getEntityId());
        assertNotNull(cachedBook, "Book should be in the Redis cache.");
        assertFalse(cachedBook.isRented(), "Book isRented flag should be false in Redis after rent deletion.");
    }

}
