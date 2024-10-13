package org.example.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.LockModeType;
import org.example.exceptions.TooManyException;
import org.example.models.Book;
import org.example.models.Client;
import org.example.models.Rent;

import java.time.LocalDate;
import java.util.List;

public class RentRepository implements IRentRepository {

    private EntityManager entityManager;

    public RentRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Rent findById(Long id) {
        return entityManager.find(Rent.class, id);
    }

    @Override
    public List<Rent> findAll() {
        return entityManager.createQuery("SELECT r FROM Rent r", Rent.class).getResultList();
    }

    @Override
    public void save(Rent rent) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            if (rent.getEntityId() == null) {
                entityManager.persist(rent);
            } else {
                entityManager.merge(rent);
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
    public void delete(Rent rent) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();

            Rent existingRent = entityManager.find(Rent.class, rent.getEntityId());
            if (existingRent == null) {
                throw new IllegalArgumentException("Rent with ID " + rent.getEntityId() + " does not exist.");
            }

            entityManager.remove(rent);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }

    @Override
    public List<Rent> findByClientId(Long clientId) {
        String jpql = "SELECT r FROM Rent r WHERE r.client.id = :clientId";
        return entityManager.createQuery(jpql, Rent.class)
                .setParameter("clientId", clientId)
                .getResultList();
    }

    @Override
    public Rent rentBook(Long clientId, Long bookId, LocalDate beginDate, LocalDate endDate) throws TooManyException {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        try {
            Client client = entityManager.find(Client.class, clientId, LockModeType.OPTIMISTIC_FORCE_INCREMENT);
            int currentRentCount = getCurrentRentCount(clientId);

            if (currentRentCount >= client.getClientType().getMaxBooks()) {
                throw new TooManyException("Client has already rented the maximum number of books.");
            }

            Book book = entityManager.find(Book.class, bookId);
            Rent rent = new Rent(client, book, beginDate, endDate);
            
            entityManager.persist(rent);
            transaction.commit();
            return rent;
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }

    @Override
    public int getCurrentRentCount(Long clientId) {
        String jpql = "SELECT COUNT(r) FROM Rent r WHERE r.client.id = :clientId";
        return entityManager.createQuery(jpql, Long.class)
                .setParameter("clientId", clientId)
                .getSingleResult()
                .intValue();
    }
}
