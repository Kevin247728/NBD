import org.example.models.ClientType;
import org.example.models.NonStudent;
import org.example.models.Student;
import org.example.repositories.MgdClientRepository;
import org.example.models.Client;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MgdClientRepositoryTests {

    private MgdClientRepository clientRepository;

    @BeforeAll
    public void setUp() {
        clientRepository = new MgdClientRepository();
        clientRepository.getMongoDatabase().getCollection("clients").drop();
    }

    @AfterAll
    public void tearDown() {
        clientRepository.close();
    }

    @Test
    public void testCreateClient() {
        ClientType nonStudent = new NonStudent();
        Client client = new Client("Joe", "Doe", nonStudent);
        clientRepository.create(client);

        Client foundClient = clientRepository.findById(client.getEntityId());
        assertNotNull(foundClient);
        assertEquals("Joe", foundClient.getFirstName());
    }

    @Test
    public void testFindAllClients() {
        ClientType student = new Student();
        ClientType nonStudent = new NonStudent();
        Client client1 = new Client("Donald", "Trump", student);
        Client client2 = new Client("Harry", "Potter", nonStudent);
        clientRepository.create(client1);
        clientRepository.create(client2);

        List<Client> clients = clientRepository.findAll();
        assertEquals(3, clients.size());
    }

    @Test
    public void testUpdateClient() {
        ClientType student = new Student();
        Client client = new Client("John", "Original", student);
        clientRepository.create(client);

        client.setLastName("New");
        boolean updated = clientRepository.update(client);

        assertTrue(updated);
        Client foundClient = clientRepository.findById(client.getEntityId());
        assertEquals("New", foundClient.getLastName());
    }

    @Test
    public void testDeleteClient() {
        ClientType student = new Student();
        Client client = new Client("Kevin", "Delete", student);
        clientRepository.create(client);

        boolean deleted = clientRepository.delete(client);
        assertTrue(deleted);

        Client foundClient = clientRepository.findById(client.getEntityId());
        assertNull(foundClient);
    }
}