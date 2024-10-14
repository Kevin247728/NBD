import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.example.models.Rent;
import org.example.repositories.RentRepository;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RentRepositoryTests {

    private EntityManager entityManager;
    private RentRepository rentRepository;

    @BeforeAll
    public void setUp() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("testPU");
        entityManager = emf.createEntityManager();
        rentRepository = new RentRepository(entityManager);
    }

    @AfterAll
    public void tearDown() {
        if (entityManager != null) {
            entityManager.close();
        }
    }

    @BeforeEach
    public void beginTransaction() {
        entityManager.getTransaction().begin();
    }

    @AfterEach
    public void rollbackTransaction() {
        if (entityManager.getTransaction().isActive()) {
            entityManager.getTransaction().rollback();
        }
    }

}
