import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MgdBookRepositoryTests {

//    private MgdBookRepository bookRepository;

    @BeforeAll
    public void setUp() {
//        bookRepository = new MgdBookRepository();
//        bookRepository.getMongoDatabase().getCollection("books").drop();
    }

    @AfterAll
    public void tearDown() {
//        bookRepository.close();
    }

    @Test
    public void testCreateBook() {
    }

}