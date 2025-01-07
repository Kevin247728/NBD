import com.datastax.oss.driver.api.core.CqlSession;
import org.example.Cassandra.CassandraConnection;
import org.example.managers.ClientManager;
import org.example.models.Client;
import org.example.models.NonStudent;
import org.example.models.Student;
import org.example.repositories.CassandraClientRepository;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CassandraClientTests {

    private static CqlSession session;
    private CassandraConnection cassandraConnection;
    private ClientManager clientManager;

    @BeforeAll
    public void setUp() {
        cassandraConnection = new CassandraConnection();
        session = cassandraConnection.getSession();

        CassandraClientRepository repository = new CassandraClientRepository(session);
        clientManager = new ClientManager(repository);
    }

    @AfterEach
    public void tearDown() {
        session.execute("TRUNCATE rent_a_book.clients;");
    }

    @AfterAll
    public static void tearDownDatabase() {
        session.close();
    }

    @Test
    void createStudentAndRetrieveById() {
        String firstName = "John";
        String lastName = "Doe";
        int maxBooks = 5;
        int maxRentDays = 30;

        clientManager.createStudent(firstName, lastName, maxBooks, maxRentDays);
        List<Client> students = clientManager.listAllStudents();

        assertFalse(students.isEmpty());
        assertInstanceOf(Student.class, students.getFirst());
        assertEquals(firstName, students.getFirst().getFirstName());
        assertEquals(lastName, students.getFirst().getLastName());
    }

    @Test
    void removeClient() {
        String firstName = "Michael";
        String lastName = "Brown";
        int maxBooks = 5;
        int maxRentDays = 30;

        clientManager.createStudent(firstName, lastName, maxBooks, maxRentDays);
        List<Client> students = clientManager.listAllStudents();
        UUID clientId = students.getFirst().getId();

        Client removedClient = clientManager.removeClient(clientId);
        assertNotNull(removedClient);
        assertEquals(clientId, removedClient.getId());

        List<Client> remainingStudents = clientManager.listAllStudents();
        assertTrue(remainingStudents.isEmpty());
    }

    @Test
    void listAllClients() {
        clientManager.createStudent("Tom", "Hanks", 5, 15);
        clientManager.createNonStudent("Emma", "Watson", 12.0f, 2, 10);

        List<Client> clients = clientManager.listAllClients();

        assertEquals(2, clients.size());
        assertTrue(clients.stream().anyMatch(client -> client instanceof Student));
        assertTrue(clients.stream().anyMatch(client -> client instanceof NonStudent));
    }

    @Test
    void updateClientInfo() {
        String firstName = "John";
        String lastName = "Doe";
        int maxBooks = 5;
        int maxRentDays = 30;

        clientManager.createStudent(firstName, lastName, maxBooks, maxRentDays);
        List<Client> students = clientManager.listAllStudents();
        UUID clientId = students.getFirst().getId();

        Client student = students.getFirst();
        student.setFirstName("Johnny");
        clientManager.updateClientInfo(student);

        Client updatedClient = clientManager.getClientById(clientId);

        assertNotNull(updatedClient);
        assertEquals("Johnny", updatedClient.getFirstName());
    }
}