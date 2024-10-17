package org.example.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.LockModeType;
import org.example.exceptions.BookAlreadyRentedException;
import org.example.exceptions.TooManyException;
import org.example.models.Book;
import org.example.models.Client;
import org.example.models.Rent;

import java.util.List;
import java.util.UUID;

public class RentRepository implements IRentRepository {

    private EntityManager entityManager;

    public RentRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Rent findById(UUID id) {
        Rent rent = entityManager.find(Rent.class, id);
        if (rent == null) {
            throw new EntityNotFoundException("Rent with ID " + id + " not found");
        }
        return rent;
    }

    @Override
    public List<Rent> findAll() {
        return entityManager.createQuery("SELECT r FROM Rent r", Rent.class).getResultList();
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
    public List<Rent> findByClientId(UUID clientId) {
        String jpql = "SELECT r FROM Rent r WHERE r.client.id = :clientId";
        return entityManager.createQuery(jpql, Rent.class)
                .setParameter("clientId", clientId)
                .getResultList();
    }

    @Override
    public void save(Rent rent) throws TooManyException, BookAlreadyRentedException {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        try {
            Client client = entityManager.find(Client.class, rent.getClient().getEntityId(), LockModeType.OPTIMISTIC_FORCE_INCREMENT);
            if (client == null) {
                throw new IllegalArgumentException("Client not found");
            }

            int currentRentCount = getCurrentRentCount(client.getEntityId());
            if (currentRentCount >= client.getClientType().getMaxBooks()) {
                throw new TooManyException("Client has already rented the maximum number of books.");
            }

            Book book = entityManager.find(Book.class, rent.getBook().getEntityId(), LockModeType.OPTIMISTIC_FORCE_INCREMENT);
            if (book == null) {
                throw new IllegalArgumentException("Book not found");
            }

            if (isBookCurrentlyRented(book.getEntityId())) {
                throw new BookAlreadyRentedException("The book is already rented.");
            }

            if (rent.getEntityId() == null) {
                entityManager.persist(rent);
            } else {
                entityManager.lock(rent, LockModeType.OPTIMISTIC_FORCE_INCREMENT);
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
    public int getCurrentRentCount(UUID clientId) {
        entityManager.clear();
        String jpql = "SELECT COUNT(r) FROM Rent r WHERE r.client.id = :clientId";
        return entityManager.createQuery(jpql, Long.class)
                .setParameter("clientId", clientId)
                .getSingleResult()
                .intValue();
    }

    @Override
    public boolean isBookCurrentlyRented(UUID bookId) {
        entityManager.clear();
        String query = "SELECT COUNT(r) FROM Rent r WHERE r.book.entityId = :bookId AND r.endDate > CURRENT_DATE";
        Long count = entityManager.createQuery(query, Long.class)
                .setParameter("bookId", bookId)
                .getSingleResult();
        return count > 0;
    }
}
