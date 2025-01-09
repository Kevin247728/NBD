import com.datastax.oss.driver.api.core.CqlSession;
import org.example.Cassandra.CassandraConnection;
import org.example.managers.BookManager;
import org.example.models.Book;
import org.example.repositories.CassandraBookRepository;
import org.junit.jupiter.api.*;
import java.util.List;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CassandraBookTests {

    private static CqlSession session;
    private BookManager bookManager;

    @BeforeAll
    public void setUp() {
        CassandraConnection cassandraConnection = new CassandraConnection();
        session = cassandraConnection.getSession();

        CassandraBookRepository bookRepository = new CassandraBookRepository(session);
        bookManager = new BookManager(bookRepository);
    }

    @AfterEach
    public void tearDown() {
        session.execute("TRUNCATE rent_a_book.books;");
    }

    @AfterAll
    public static void tearDownDatabase() {
        session.close();
    }

    @Test
    void createBookAndRetrieveById() {
        String title = "The Great Gatsby";
        boolean isRented = false;

        bookManager.createBook(title, isRented);
        List<Book> books = bookManager.listAllBooks();

        assertFalse(books.isEmpty());
        assertEquals(title, books.getFirst().getTitle());
        assertEquals(isRented, books.getFirst().isRented());
    }

    @Test
    void removeBook() {
        String title = "Moby Dick";
        boolean isRented = false;

        bookManager.createBook(title, isRented);
        List<Book> books = bookManager.listAllBooks();
        UUID bookId = books.getFirst().getId();

        Book removedBook = bookManager.removeBook(bookId);
        assertNotNull(removedBook);
        assertEquals(bookId, removedBook.getId());

        List<Book> remainingBooks = bookManager.listAllBooks();
        assertTrue(remainingBooks.isEmpty());
    }

    @Test
    void listAllBooks() {
        bookManager.createBook("1984", true);
        bookManager.createBook("To Kill a Mockingbird", false);

        List<Book> books = bookManager.listAllBooks();

        assertEquals(2, books.size());
        assertTrue(books.stream().anyMatch(book -> book.getTitle().equals("1984")));
        assertTrue(books.stream().anyMatch(book -> book.getTitle().equals("To Kill a Mockingbird")));
    }

    @Test
    void updateBookInfo() {
        String title = "The Odyssey";
        boolean isRented = false;

        bookManager.createBook(title, isRented);
        List<Book> books = bookManager.listAllBooks();
        UUID bookId = books.getFirst().getId();

        Book book = books.getFirst();
        book.setTitle("The Iliad");
        book.setRented(true);
        bookManager.updateBookInfo(book);

        Book updatedBook = bookManager.getBookById(bookId);

        assertNotNull(updatedBook);
        assertEquals("The Iliad", updatedBook.getTitle());
        assertTrue(updatedBook.isRented());
    }
}
