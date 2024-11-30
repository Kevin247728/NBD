package org.example.repositories;

import org.example.models.Book;
import org.example.models.UniqueIdMgd;

import java.util.List;

public class RedisMongoBookRepositoryDecorator implements BookRepository {
    private final RedisBookRepository redisRepository;
    private final MgdBookRepository mgdRepository;

    public RedisMongoBookRepositoryDecorator(RedisBookRepository redisRepository, MgdBookRepository mgdRepository) {
        this.redisRepository = redisRepository;
        this.mgdRepository = mgdRepository;
    }

    /** We add the book to cache when it is searched for, but does not exist in cache **/
    @Override
    public Book findById(UniqueIdMgd id) {
        Book book = redisRepository.findById(id);

        if (book != null) {
            return book;
        }

        book = mgdRepository.findById(id);
        if (book != null) {
            redisRepository.create(book);
        }

        return book;
    }

    @Override
    public List<Book> findAll() {
        List<Book> books = redisRepository.findAll();

        if (books.isEmpty()) {
            books = mgdRepository.findAll();
        }

        return books;
    }

    @Override
    public void create(Book book) {
        redisRepository.create(book);

        mgdRepository.create(book);
    }

    /** We update the book in cache when it gets rented or returned **/
    @Override
    public boolean update(Book book) {
        boolean isUpdatedInMongoDB = mgdRepository.update(book);
        boolean isUpdatedInRedis = redisRepository.update(book);

        return isUpdatedInMongoDB && isUpdatedInRedis;
    }

    @Override
    public boolean delete(Book book) {
        boolean isDeletedInMongoDB = mgdRepository.delete(book);
        boolean isDeletedInRedis = redisRepository.delete(book);

        return isDeletedInMongoDB && isDeletedInRedis;
    }

    public RedisBookRepository getRedisRepository() {
        return redisRepository;
    }
}

