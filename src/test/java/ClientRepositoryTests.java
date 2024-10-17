import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.Persistence;
import org.example.models.*;
import org.example.repositories.ClientRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ClientRepositoryTests {

    private EntityManager entityManager;
    private ClientRepository clientRepository;

    @BeforeAll
    public void setUp() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("TEST_RENT_BOOKS");
        entityManager = emf.createEntityManager();
        clientRepository = new ClientRepository(entityManager);
    }

    @AfterAll
    public void tearDown() {
        if (entityManager != null) {
            entityManager.close();
        }
    }

    @Test
    public void testSaveAndFindById() {
        ClientType studentType = new Student();
        Client client = new Client("John", "Doe", studentType);

        clientRepository.save(client);
        Client foundClient = clientRepository.findById(client.getEntityId());

        assertEquals(client, foundClient);
    }

    @Test
    public void testFindByIdThrowsExceptionWhenClientNotFound() {
        UUID nonExistentId = UUID.randomUUID();

        assertThrows(EntityNotFoundException.class, () -> clientRepository.findById(nonExistentId));
    }

    @Test
    public void testFindAllReturnsAllClients() {
        ClientType studentType = new Student();
        ClientType nonStudentType = new NonStudent();

        Client client1 = new Client("John", "Doe", studentType);
        clientRepository.save(client1);

        Client client2 = new Client("Jane", "Smith", nonStudentType);
        clientRepository.save(client2);

        List<Client> allClients = clientRepository.findAll();
        assertEquals(2, allClients.size());
    }

    @Test
    public void testDeleteNonExistentClientThrowsException() {
        ClientType nonStudentType = new NonStudent();

        Client nonExistentClient = new Client("Ghost", "User", nonStudentType);
        clientRepository.save(nonExistentClient);
        assertEquals(nonExistentClient, clientRepository.findById(nonExistentClient.getEntityId()));
        clientRepository.delete(nonExistentClient);

        assertThrows(IllegalArgumentException.class, () -> clientRepository.delete(nonExistentClient));
    }
}