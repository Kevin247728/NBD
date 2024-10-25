package org.example.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.LockModeType;
import org.example.models.Client;
import org.example.models.ClientType;

import java.util.List;
import java.util.UUID;

public class JPAClientRepository implements ClientRepository {

    private EntityManager entityManager;

    public JPAClientRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Client findById(UUID id) {
        Client client = entityManager.find(Client.class, id);
        if (client == null) {
            throw new EntityNotFoundException("Client with ID " + id + " not found");
        }
        return client;
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

            ClientType clientType = client.getClientType();
            if (clientType.getEntityId() == null) {
                entityManager.persist(clientType);
            }

            if (client.getEntityId() == null) {
                entityManager.persist(client);
            } else {
                entityManager.lock(client, LockModeType.OPTIMISTIC_FORCE_INCREMENT);
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

            Client existingClient = entityManager.find(Client.class, client.getEntityId(), LockModeType.OPTIMISTIC_FORCE_INCREMENT);
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