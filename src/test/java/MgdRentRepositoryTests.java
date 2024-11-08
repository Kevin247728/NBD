import org.example.models.Book;
import org.example.models.Client;
import org.example.models.NonStudent;
import org.example.models.Rent;
import org.example.repositories.MgdBookRepository;
import org.example.repositories.MgdClientRepository;
import org.example.repositories.MgdRentRepository;
import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MgdRentRepositoryTests {

    private MgdRentRepository rentRepository;
    private MgdClientRepository clientRepository;
    private MgdBookRepository bookRepository;

    @BeforeAll
    public void setUp() {
        rentRepository = new MgdRentRepository();
        clientRepository = new MgdClientRepository();
        bookRepository = new MgdBookRepository();
        rentRepository.getMongoDatabase().getCollection("rents").drop();
        clientRepository.getMongoDatabase().getCollection("clients").drop();
        bookRepository.getMongoDatabase().getCollection("books").drop();
    }

    @AfterAll
    public void tearDown() {
        rentRepository.close();
    }

    @Test
    public void testCreateRent() throws Exception {
        Client client = new Client("John", "Doe", new NonStudent());
        Book book = new Book("Sample Book");

        clientRepository.create(client);
        bookRepository.create(book);

        LocalDate beginDate = LocalDate.now();
        LocalDate endDate = beginDate.plusDays(30);
        Rent rent = new Rent(client.getEntityId(), book.getEntityId(), beginDate, endDate, clientRepository);
        rentRepository.create(rent);

        Rent foundRent = rentRepository.findById(rent.getEntityId());
        assertNotNull(foundRent);
        assertEquals(client.getEntityId(), foundRent.getClientId());
        assertEquals(book.getEntityId(), foundRent.getBookId());
    }

}
