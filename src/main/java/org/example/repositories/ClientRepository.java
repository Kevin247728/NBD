package org.example.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.example.models.Client;

import java.util.List;

public class ClientRepository implements IClientRepository {

    private EntityManager entityManager;

    public ClientRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Client findById(Long id) {
        return entityManager.find(Client.class, id);
    }

    @Override
    public List<Client> findAll() {
        return entityManager.createQuery("SELECT c FROM Client c", Client.class).getResultList();
    }

    @Override
    public void save(Client client) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            if (client.getEntityId() == null) {
                entityManager.persist(client);
            } else {
                entityManager.merge(client);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }

    @Override
    public void delete(Client client) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();

            Client existingClient = entityManager.find(Client.class, client.getEntityId());
            if (existingClient == null) {
                throw new IllegalArgumentException("Client with ID " + client.getEntityId() + " does not exist.");
            }

            entityManager.remove(client);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }
}