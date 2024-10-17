package org.example.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.LockModeType;
import org.example.models.Book;

import java.util.List;
import java.util.UUID;

public class BookRepository implements IBookRepository {

    private EntityManager entityManager;

    public BookRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Book findById(UUID id) {
        Book book = entityManager.find(Book.class, id);
        if (book == null) {
            throw new EntityNotFoundException("Book with ID " + id + " not found");
        }
        return book;
    }

    @Override
    public List<Book> findAll() {
        return entityManager.createQuery("SELECT b FROM Book b", Book.class).getResultList();
    }

    @Override
    public void save(Book book) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            if (book.getEntityId() == null) {
                entityManager.persist(book);
            } else {
                entityManager.lock(book, LockModeType.OPTIMISTIC_FORCE_INCREMENT);
                entityManager.merge(book);
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
    public void delete(Book book) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();

            Book existingBook = entityManager.find(Book.class, book.getEntityId(), LockModeType.OPTIMISTIC_FORCE_INCREMENT);
            if (existingBook == null) {
                throw new IllegalArgumentException("Book with ID " + book.getEntityId() + " does not exist.");
            }

            entityManager.remove(book);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }
}
