import jakarta.persistence.*;
import org.example.models.Book;
import org.example.repositories.BookRepository;

import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BookRepositoryTests {

    private EntityManager entityManager;
    private BookRepository bookRepository;

    @BeforeAll
    public void setUp() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("TEST_RENT_BOOKS");
        entityManager = emf.createEntityManager();
        bookRepository = new BookRepository(entityManager);
    }

    @AfterAll
    public void tearDown() {
        if (entityManager != null) {
            entityManager.close();
        }
    }

    @Test
    public void testSaveAndFindById() {
        Book book = new Book();

        bookRepository.save(book);
        Book foundBook = bookRepository.findById(book.getEntityId());

        assertEquals(book, foundBook);
    }

    @Test
    public void testFindByIdThrowsExceptionWhenBookNotFound() {
        UUID nonExistentId = UUID.randomUUID();

        assertThrows(EntityNotFoundException.class, () -> bookRepository.findById(nonExistentId));
    }

    @Test
    public void testFindAllReturnsAllBooks() {

        Book book1 = new Book();
        bookRepository.save(book1);

        Book book2 = new Book();
        bookRepository.save(book2);

        List<Book> allBooks = bookRepository.findAll();
        assertEquals(2, allBooks.size());
    }

    @Test
    public void testDeleteNonExistentBookThrowsException() {
        Book nonExistentBook = new Book();
        bookRepository.save(nonExistentBook);
        assertEquals(nonExistentBook, bookRepository.findById(nonExistentBook.getEntityId()));
        bookRepository.delete(nonExistentBook);

        assertThrows(IllegalArgumentException.class, () -> bookRepository.delete(nonExistentBook));
    }

    @Test
    public void testFindAllReturnsNoBooksAfterDeletion() {
        Book book1 = new Book();
        bookRepository.save(book1);

        Book book2 = new Book();
        bookRepository.save(book2);

        bookRepository.delete(book1);
        bookRepository.delete(book2);

        List<Book> allBooks = bookRepository.findAll();
        assertTrue(allBooks.isEmpty());
    }
}