package org.example.db;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class DatabaseManager {
    private EntityManagerFactory entityManagerFactory;

    public DatabaseManager() {
        entityManagerFactory = Persistence.createEntityManagerFactory("TEST_RENT_BOOKS");
    }

    public EntityManager getEntityManager() {
        return entityManagerFactory.createEntityManager();
    }

    public void close() {
        if (entityManagerFactory != null) {
            entityManagerFactory.close();
        }
    }
}
