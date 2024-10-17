import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.Persistence;
import org.example.exceptions.BookAlreadyRentedException;
import org.example.exceptions.TooManyException;
import org.example.models.*;
import org.example.repositories.BookRepository;
import org.example.repositories.ClientRepository;
import org.example.repositories.RentRepository;
import static org.junit.jupiter.api.Assertions.*;

import jakarta.persistence.EntityManager;

import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.util.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RentRepositoryTests {

    private EntityManager entityManager;
    private RentRepository rentRepository;
    private BookRepository bookRepository;
    private ClientRepository clientRepository;

    @BeforeAll
    public void setUp() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("TEST_RENT_BOOKS");
        entityManager = emf.createEntityManager();
        rentRepository = new RentRepository(entityManager);
        bookRepository = new BookRepository(entityManager);
        clientRepository = new ClientRepository(entityManager);
    }

    @AfterAll
    public void tearDown() {
        if (entityManager != null) {
            entityManager.close();
        }
    }

    @Test
    public void testSaveFindByIdRent() throws TooManyException, BookAlreadyRentedException {
        ClientType studentType = new Student();
        Client client = new Client("John", "Doe", studentType);
        Book book = new Book("Effective Java");
        LocalDate beginDate = LocalDate.now();
        LocalDate endDate = beginDate.plusDays(10);

        clientRepository.save(client);
        bookRepository.save(book);

        Rent rent = new Rent(client, book, beginDate, endDate);
        rentRepository.save(rent);

        Rent foundRent = rentRepository.findById(rent.getEntityId());
        assertEquals(rent, foundRent);
    }

    @Test
    public void testFindByIdThrowsExceptionWhenRentNotFound() {
        UUID nonExistentId = UUID.randomUUID();

        assertThrows(EntityNotFoundException.class, () -> rentRepository.findById(nonExistentId));
    }

    @Test
    public void testDeleteAndDeleteWhenRentNotFound() throws TooManyException, BookAlreadyRentedException {
        ClientType studentType = new Student();
        Client client = new Client("John", "Doe", studentType);
        Book book = new Book("Effective Java");
        LocalDate beginDate = LocalDate.now();
        LocalDate endDate = beginDate.plusDays(10);

        clientRepository.save(client);
        bookRepository.save(book);

        Rent rent = new Rent(client, book, beginDate, endDate);
        rentRepository.save(rent);
        assertEquals(rent, rentRepository.findById(rent.getEntityId()));
        rentRepository.delete(rent);

        assertThrows(IllegalArgumentException.class, () -> rentRepository.delete(rent));
    }

    @Test
    public void testFindRentsForClientId() throws TooManyException, BookAlreadyRentedException {
        ClientType studentType = new Student();
        Client client = new Client("John", "Doe", studentType);
        Book book = new Book("Effective Java");
        LocalDate beginDate = LocalDate.now();
        LocalDate endDate = beginDate.plusDays(10);

        clientRepository.save(client);
        bookRepository.save(book);

        Rent rent = new Rent(client, book, beginDate, endDate);
        rentRepository.save(rent);

        Book book2 = new Book("Ineffective Java");
        LocalDate beginDate2 = LocalDate.now();
        LocalDate endDate2 = beginDate.plusDays(10);

        bookRepository.save(book);

        Rent rent2 = new Rent(client, book2, beginDate2, endDate2);
        rentRepository.save(rent2);

        List<Rent> rents = new ArrayList<>();
        rents.add(rent);
        rents.add(rent2);

        assertEquals(rents, rentRepository.findByClientId(client.getEntityId()));
    }
}
