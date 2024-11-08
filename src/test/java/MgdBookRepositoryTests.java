import org.example.models.Book;
import org.example.repositories.MgdBookRepository;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MgdBookRepositoryTests {

    private MgdBookRepository bookRepository;

    @BeforeAll
    public void setUp() {
        bookRepository = new MgdBookRepository();
        bookRepository.getMongoDatabase().getCollection("books").drop();
    }

    @AfterAll
    public void tearDown() {
        bookRepository.close();
    }

    @Test
    public void testCreateBook() {
        Book book = new Book("Test Title");
        bookRepository.create(book);

        Book foundBook = bookRepository.findById(book.getEntityId());
        assertNotNull(foundBook);
        assertEquals("Test Title", foundBook.getTitle());
    }

    @Test
    public void testFindAllBooks() {
        Book book1 = new Book("Title 1");
        Book book2 = new Book("Title 2");
        bookRepository.create(book1);
        bookRepository.create(book2);

        List<Book> books = bookRepository.findAll();
        assertEquals(2, books.size());
    }

    @Test
    public void testUpdateBook() {
        Book book = new Book("Original Title");
        bookRepository.create(book);

        book.setTitle("Updated Title");
        boolean updated = bookRepository.update(book);

        assertTrue(updated);
        Book foundBook = bookRepository.findById(book.getEntityId());
        assertEquals("Updated Title", foundBook.getTitle());
    }

    @Test
    public void testDeleteBook() {
        Book book = new Book("Delete Me");
        bookRepository.create(book);

        boolean deleted = bookRepository.delete(book);
        assertTrue(deleted);

        Book foundBook = bookRepository.findById(book.getEntityId());
        assertNull(foundBook);
    }
}