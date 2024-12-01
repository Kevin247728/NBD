import org.example.Redis.RedisConnectionManager;
import org.example.models.Book;
import org.example.repositories.RedisBookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import redis.clients.jedis.JedisPooled;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RedisBookRepositoryTest {

    private JedisPooled pool;
    private RedisBookRepository redisRepository;

    @BeforeEach
    public void setUp() {
        RedisConnectionManager.initConnectionFromConfig("src/main/resources/application.properties");
        pool = RedisConnectionManager.getJedis();
        redisRepository = new RedisBookRepository(pool);
        redisRepository.clearCache();
    }

    @Test
    public void testCreate() {
        Book book = new Book("Book Title");

        redisRepository.create(book);

        String cacheKey = "book: " + book.getEntityId().getId().toString();
        String json = null;

        try {
            json = pool.get(cacheKey);
        } catch (Exception e) {
            System.err.println("Error while accessing Redis: " + e.getMessage());
        }

        assertNotNull(json);
        Book retrievedBook = redisRepository.findById(book.getEntityId());
        assertNotNull(retrievedBook);
        assertEquals("Book Title", retrievedBook.getTitle());
    }

    @Test
    public void testFindById() {
        Book book = new Book( "Book Title");
        redisRepository.create(book);

        Book retrievedBook = redisRepository.findById(book.getEntityId());

        assertNotNull(retrievedBook);
        assertEquals("Book Title", retrievedBook.getTitle());
    }

    @Test
    public void testFindAll() {
        Book book1 = new Book("Book 1");
        Book book2 = new Book("Book 2");

        redisRepository.create(book1);
        redisRepository.create(book2);

        List<Book> books = redisRepository.findAll();

        assertEquals(2, books.size());
        assertTrue(books.stream().anyMatch(book -> book.getTitle().equals("Book 1")));
        assertTrue(books.stream().anyMatch(book -> book.getTitle().equals("Book 2")));
    }

    @Test
    public void testDelete() {
        Book book = new Book("Book Title");
        redisRepository.create(book);

        boolean result = redisRepository.delete(book);

        assertTrue(result);
        assertNull(redisRepository.findById(book.getEntityId()));
    }

    @Test
    public void testUpdate() {
        Book book = new Book("Book Title");
        redisRepository.create(book);

        book.setTitle("Updated Book Title");
        boolean result = redisRepository.update(book);

        assertTrue(result);

        Book updatedBook = redisRepository.findById(book.getEntityId());
        assertNotNull(updatedBook);
        assertEquals("Updated Book Title", updatedBook.getTitle());
    }

    @Test
    public void testClearCache_AllKeysRemoved() {
        redisRepository.create(new Book("Book 1"));
        redisRepository.create(new Book("Book 2"));

        assertEquals(2, redisRepository.findAll().size());

        redisRepository.clearCache();

        assertTrue(redisRepository.findAll().isEmpty());
    }

    @Test
    public void testClearCache_NoKeysInitially() {
        assertTrue(redisRepository.findAll().isEmpty());

        redisRepository.clearCache();

        assertTrue(redisRepository.findAll().isEmpty());
    }
}

