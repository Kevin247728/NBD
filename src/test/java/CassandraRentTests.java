import com.datastax.oss.driver.api.core.CqlSession;
import org.example.Cassandra.CassandraConnection;
import org.example.exceptions.BookAlreadyRentedException;
import org.example.exceptions.RentDurationExceeded;
import org.example.exceptions.TooManyException;
import org.example.managers.BookManager;
import org.example.managers.ClientManager;
import org.example.managers.RentManager;
import org.example.models.Book;
import org.example.models.Client;
import org.example.models.Rent;
import org.example.repositories.CassandraBookRepository;
import org.example.repositories.CassandraClientRepository;
import org.example.repositories.CassandraRentRepository;
import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CassandraRentTests {

    private static CqlSession session;
    private RentManager rentManager;
    private BookManager bookManager;
    private ClientManager clientManager;

    @BeforeAll
    public void setUp() {
        CassandraConnection cassandraConnection = new CassandraConnection();
        session = cassandraConnection.getSession();

        CassandraRentRepository rentRepository = new CassandraRentRepository(session);
        CassandraBookRepository bookRepository = new CassandraBookRepository(session);
        CassandraClientRepository clientRepository = new CassandraClientRepository(session);

        rentManager = new RentManager(rentRepository, bookRepository, clientRepository);
        bookManager = new BookManager(bookRepository);
        clientManager = new ClientManager(clientRepository);
    }

    @AfterEach
    public void tearDown() {
        session.execute("TRUNCATE rent_a_book.rents;");
        session.execute("TRUNCATE rent_a_book.books;");
        session.execute("TRUNCATE rent_a_book.clients;");
    }

    @AfterAll
    public static void tearDownDatabase() {
        session.close();
    }

    @Test
    public void shouldCreateRentSuccessfully() throws BookAlreadyRentedException, TooManyException {

        Client client = clientManager.createStudent("John", "Doe", 5, 14);
        Book book = bookManager.createBook("Test Book", false);

        LocalDate beginDate = LocalDate.now();
        LocalDate endDate = beginDate.plusDays(7);

        Rent rent = rentManager.createRent(client.getId(), book.getId(), beginDate, endDate, 0);

        assertNotNull(rent);
        assertTrue(bookManager.getBookById(book.getId()).isRented());
    }

    @Test
    public void shouldThrowExceptionWhenClientDoesNotExist() {
        UUID clientId = UUID.randomUUID();

        Book book = bookManager.createBook("Test Book", false);

        LocalDate beginDate = LocalDate.now();
        LocalDate endDate = beginDate.plusDays(7);

        assertThrows(IllegalArgumentException.class, () ->
                rentManager.createRent(clientId, book.getId(), beginDate, endDate, 0)
        );
    }

    @Test
    public void shouldThrowExceptionWhenBookIsAlreadyRented() throws BookAlreadyRentedException, TooManyException {
        Client client = clientManager.createStudent("John", "Doe", 5, 14);
        Book book = bookManager.createBook("Test Book", false);

        LocalDate beginDate = LocalDate.now();
        LocalDate endDate = beginDate.plusDays(7);

        rentManager.createRent(client.getId(), book.getId(), beginDate, endDate, 0);

        Client client2 = clientManager.createStudent("John", "Doe2", 5, 14);

        assertThrows(BookAlreadyRentedException.class, () ->
                rentManager.createRent(client2.getId(), book.getId(), beginDate, endDate, 0)
        );
    }

    @Test
    public void shouldThrowExceptionWhenRentExceedsMaxDays() {
        Client client = clientManager.createStudent("John", "Doe", 5, 14);
        Book book = bookManager.createBook("Test Book", false);

        LocalDate beginDate = LocalDate.now();
        LocalDate endDate = beginDate.plusDays(15);

        assertThrows(RentDurationExceeded.class, () ->
                rentManager.createRent(client.getId(), book.getId(), beginDate, endDate, 0)
        );
    }

    @Test
    public void shouldRemoveRentAndUpdateBookStatus() throws BookAlreadyRentedException, TooManyException {
        Client client = clientManager.createStudent("John", "Doe", 5, 14);
        Book book = bookManager.createBook("Test Book", false);

        LocalDate beginDate = LocalDate.now();
        LocalDate endDate = beginDate.plusDays(7);

        Rent rent = rentManager.createRent(client.getId(), book.getId(), beginDate, endDate, 0);

        assertNotNull(rent);

        rentManager.removeRent(rent.getRentId());

        assertFalse(bookManager.getBookById(book.getId()).isRented());
    }

    @Test
    public void shouldThrowExceptionWhenClientReachesMaxRents() throws BookAlreadyRentedException, TooManyException {
        Client client = clientManager.createStudent("John", "Doe", 1, 14);
        Book book = bookManager.createBook("Test Book 1", false);
        Book book2 = bookManager.createBook("Test Book 2", false);

        LocalDate beginDate = LocalDate.now();
        LocalDate endDate = beginDate.plusDays(7);

        rentManager.createRent(client.getId(), book.getId(), beginDate, endDate, 0);

        assertThrows(TooManyException.class, () ->
                rentManager.createRent(client.getId(), book2.getId(), beginDate, endDate, 0)
        );
    }

    @Test
    public void shouldListRentsByClientId() throws BookAlreadyRentedException, TooManyException {
        Client client = clientManager.createStudent("John", "Doe", 5, 14);
        Book book1 = bookManager.createBook("Test Book 1", false);
        Book book2 = bookManager.createBook("Test Book 2", false);

        LocalDate beginDate = LocalDate.now();
        LocalDate endDate = beginDate.plusDays(7);

        rentManager.createRent(client.getId(), book1.getId(), beginDate, endDate, 0);
        rentManager.createRent(client.getId(), book2.getId(), beginDate, endDate, 0);

        List<Rent> rents = rentManager.listRentsByClientId(client.getId());

        assertEquals(2, rents.size());
    }
}
