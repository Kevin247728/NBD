import org.example.Redis.RedisConnectionManager;
import org.example.exceptions.BookAlreadyRentedException;
import org.example.exceptions.TooManyException;
import org.example.models.Book;
import org.example.models.Client;
import org.example.models.NonStudent;
import org.example.models.Rent;
import org.example.repositories.*;
import org.junit.jupiter.api.*;
import redis.clients.jedis.JedisPooled;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MgdRentRepositoryTests {

    private MgdRentRepository rentRepository;
    private MgdClientRepository clientRepository;
    private MgdBookRepository bookRepository;
    private JedisPooled pool;
    private RedisBookRepository redisRepository;

    @BeforeAll
    public void setUp() {
        RedisConnectionManager.initConnectionFromConfig("src/main/resources/application.properties");
        pool = RedisConnectionManager.getJedis();
        redisRepository = new RedisBookRepository(pool);
        clientRepository = new MgdClientRepository();
        bookRepository = new MgdBookRepository();
        rentRepository = new MgdRentRepository(new RedisMongoBookRepositoryDecorator(redisRepository, bookRepository));
        rentRepository.getMongoDatabase().getCollection("rents").drop();
        clientRepository.getMongoDatabase().getCollection("clients").drop();
        bookRepository.getMongoDatabase().getCollection("books").drop();
    }

    @AfterAll
    public void tearDown() {
        rentRepository.close();
    }

    @Test
    public void testConcurrentRentingSameBook() throws TooManyException {

        Client client1 = new Client("Alice", "Concurrency1", new NonStudent());
        Client client2 = new Client("Bob", "Concurrency2", new NonStudent());
        Book book = new Book("Concurrent Rent Book");

        clientRepository.create(client1);
        clientRepository.create(client2);
        bookRepository.create(book);

        LocalDate beginDate = LocalDate.now();
        LocalDate endDate = beginDate.plusDays(30);

        Rent rent1 = new Rent(client1.getEntityId(), book.getEntityId(), beginDate, endDate);
        Rent rent2 = new Rent(client2.getEntityId(), book.getEntityId(), beginDate, endDate);

        assertDoesNotThrow(() -> rentRepository.create(rent1));

        assertThrows(RuntimeException.class, () -> rentRepository.create(rent2));

        Book updatedBook = bookRepository.findById(book.getEntityId());
        assertTrue(updatedBook.isRented(), "Książka powinna być oznaczona jako wypożyczona.");

        List<Rent> rentsForBook = rentRepository.findByClientId(client1.getEntityId());
        assertEquals(1, rentsForBook.size(), "Powinno być zapisane tylko jedno wypożyczenie dla książki.");

    }


    @Test
    public void testCreateRent() throws Exception {
        Client client = new Client("John", "Doe", new NonStudent());
        Book book = new Book("Sample Book");

        clientRepository.create(client);
        bookRepository.create(book);

        LocalDate beginDate = LocalDate.now();
        LocalDate endDate = beginDate.plusDays(30);
        Rent rent = new Rent(client.getEntityId(), book.getEntityId(), beginDate, endDate);
        rentRepository.create(rent);

        Rent foundRent = rentRepository.findById(rent.getEntityId());
        assertNotNull(foundRent);
        assertEquals(client.getEntityId(), foundRent.getClientId());
        assertEquals(book.getEntityId(), foundRent.getBookId());
    }

    @Test
    public void testFindAllRents() throws TooManyException, BookAlreadyRentedException {
        Client client = new Client("Jane", "Smith", new NonStudent());
        Book book1 = new Book("Book One");
        Book book2 = new Book("Book Two");

        clientRepository.create(client);
        bookRepository.create(book1);
        bookRepository.create(book2);

        LocalDate beginDate1 = LocalDate.now();
        LocalDate endDate1 = beginDate1.plusDays(40);
        LocalDate beginDate2 = LocalDate.now();
        LocalDate endDate2 = beginDate2.plusDays(50);

        Rent rent1 = new Rent(client.getEntityId(), book1.getEntityId(), beginDate1, endDate1);
        Rent rent2 = new Rent(client.getEntityId(), book2.getEntityId(), beginDate2, endDate2);

        rentRepository.create(rent1);
        rentRepository.create(rent2);

        List<Rent> rents = rentRepository.findAll();
        assertTrue(rents.size() >= 2);
    }

    @Test
    public void testUpdateRent() throws Exception {
        Client client = new Client("Alice", "Wonderland", new NonStudent());
        Book book = new Book("Old Book");

        clientRepository.create(client);
        bookRepository.create(book);

        LocalDate beginDate = LocalDate.now();
        LocalDate endDate = beginDate.plusDays(100);

        Rent rent = new Rent(client.getEntityId(), book.getEntityId(), beginDate, endDate);
        rentRepository.create(rent);

        rent.setEndDate(LocalDate.now().plusDays(10));
        boolean updated = rentRepository.update(rent);

        assertTrue(updated);
        Rent foundRent = rentRepository.findById(rent.getEntityId());
        assertEquals(LocalDate.now().plusDays(10), foundRent.getEndDate());
    }

    @Test
    public void testDeleteRent() throws Exception {
        Client client = new Client("Bob", "Builder", new NonStudent());
        Book book = new Book("Book to Delete");

        clientRepository.create(client);
        bookRepository.create(book);

        LocalDate beginDate = LocalDate.now();
        LocalDate endDate = beginDate.plusDays(100);

        Rent rent = new Rent(client.getEntityId(), book.getEntityId(), beginDate, endDate);
        rentRepository.create(rent);

        boolean deleted = rentRepository.delete(rent);
        assertTrue(deleted);

        Rent foundRent = rentRepository.findById(rent.getEntityId());
        assertNull(foundRent);
    }


    @Test
    public void testIsBookCurrentlyRented() throws TooManyException, BookAlreadyRentedException {
        Client client = new Client("Emma", "Green", new NonStudent());
        Book book = new Book("Currently Rented Book");

        clientRepository.create(client);
        bookRepository.create(book);

        LocalDate beginDate = LocalDate.now();
        LocalDate endDate = beginDate.plusDays(100);

        Rent rent = new Rent(client.getEntityId(), book.getEntityId(), beginDate, endDate);
        rentRepository.create(rent);

        assertThrows(RuntimeException.class, () -> {
            Rent rentRentedBook = new Rent(client.getEntityId(), book.getEntityId(), beginDate, endDate.plusDays(10));
            rentRepository.create(rentRentedBook);
        });
    }
}
