package org.example;

import jakarta.persistence.EntityManager;
import org.example.db.DatabaseManager;
import org.example.models.Author;

public class Main {
    public static void main(String[] args) {
        DatabaseManager dbManager = new DatabaseManager();
        EntityManager entityManager = dbManager.getEntityManager();

        try {
            entityManager.getTransaction().begin();

            Author author = new Author(1, "John", "Doe");

            entityManager.persist(author);

            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            entityManager.close();
            dbManager.close();
        }
    }
}
