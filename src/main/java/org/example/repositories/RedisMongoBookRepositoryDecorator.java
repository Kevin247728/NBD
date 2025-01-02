//package org.example.repositories;
//
//import org.example.models.Book;
//import org.example.models.UniqueIdMgd;
//
//import java.util.List;
//
//public class RedisMongoBookRepositoryDecorator implements BookRepository {
//    private final RedisBookRepository redisRepository;
//    private final MgdBookRepository mgdRepository;
//
//    public RedisMongoBookRepositoryDecorator(RedisBookRepository redisRepository, MgdBookRepository mgdRepository) {
//        this.redisRepository = redisRepository;
//        this.mgdRepository = mgdRepository;
//    }
//
//    /** We add the book to cache when it is searched for, but does not exist in cache **/
//    @Override
//    public Book findById(UniqueIdMgd id) {
//        try {
//            Book book = redisRepository.findById(id);
//
//            if (book != null) {
//                return book;
//            }
//
//            book = mgdRepository.findById(id);
//            if (book != null) {
//                try {
//                    redisRepository.create(book);
//                } catch (Exception e) {
//                    System.err.println("Failed to add book to Redis cache: " + e.getMessage());
//                }
//            }
//
//            return book;
//        } catch (Exception e) {
//            throw new RuntimeException("Error during findById operation", e);
//        }
//    }
//
//    @Override
//    public List<Book> findAll() {
//        try {
//            List<Book> books = redisRepository.findAll();
//
//            if (books.isEmpty()) {
//                books = mgdRepository.findAll();
//            }
//
//            return books;
//        } catch (Exception e) {
//            throw new RuntimeException("Error during findAll operation", e);
//        }
//    }
//
//    @Override
//    public void create(Book book) {
//        try {
//            redisRepository.create(book);
//        } catch (Exception e) {
//            System.err.println("Failed to create book in Redis cache: " + e.getMessage());
//        }
//
//        try {
//            mgdRepository.create(book);
//        } catch (Exception e) {
//            throw new RuntimeException("Failed to create book in MongoDB", e);
//        }
//    }
//
//    /** We update the book in cache when it gets rented or returned **/
//    @Override
//    public boolean update(Book book) {
//        boolean isUpdatedInMongoDB = false;
//        boolean isUpdatedInRedis = false;
//
//        try {
//            isUpdatedInMongoDB = mgdRepository.update(book);
//        } catch (Exception e) {
//            System.err.println("Failed to update book in MongoDB: " + e.getMessage());
//        }
//
//        try {
//            isUpdatedInRedis = redisRepository.update(book);
//        } catch (Exception e) {
//            System.err.println("Failed to update book in Redis cache: " + e.getMessage());
//        }
//
//        return isUpdatedInMongoDB && isUpdatedInRedis;
//    }
//
//    @Override
//    public boolean delete(Book book) {
//        boolean isDeletedInMongoDB = false;
//        boolean isDeletedInRedis = false;
//
//        try {
//            isDeletedInMongoDB = mgdRepository.delete(book);
//        } catch (Exception e) {
//            System.err.println("Failed to delete book from MongoDB: " + e.getMessage());
//        }
//
//        try {
//            isDeletedInRedis = redisRepository.delete(book);
//        } catch (Exception e) {
//            System.err.println("Failed to delete book from Redis cache: " + e.getMessage());
//        }
//
//        return isDeletedInMongoDB && isDeletedInRedis;
//    }
//
//    public RedisBookRepository getRedisRepository() {
//        return redisRepository;
//    }
//}
//
