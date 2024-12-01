package org.example.repositories;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import org.example.models.Book;
import org.example.models.UniqueIdMgd;
import redis.clients.jedis.JedisPooled;
import redis.clients.jedis.params.ScanParams;
import redis.clients.jedis.resps.ScanResult;

import java.util.ArrayList;
import java.util.List;

public class RedisBookRepository implements BookRepository {

    private final JedisPooled pool;
    private final Jsonb jsonb;
    private static final int CACHE_TTL_SECONDS = 10;
    private static final String CACHE_KEY_PATTERN = "book:*";

    public RedisBookRepository(JedisPooled pool) {
        this.pool = pool;
        this.jsonb = JsonbBuilder.create();
    }

    @Override
    public Book findById(UniqueIdMgd id) {
        try {
            String cacheKey = "book: " + id.getId().toString();
            String json = pool.get(cacheKey);
            if (json == null) {
                return null;
            }
            return jsonb.fromJson(json, Book.class);
        } catch (Exception e) {
            System.err.println("Redis unavailable for findById: " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<Book> findAll() {
        List<Book> books = new ArrayList<>();

        try {
            String cursor = "0";
            ScanParams scanParams = new ScanParams().match(CACHE_KEY_PATTERN);
            do {
                ScanResult<String> scanResult = pool.scan(cursor, scanParams);
                cursor = scanResult.getCursor();
                for (String cacheKey : scanResult.getResult()) {
                    String json = pool.get(cacheKey);
                    if (json != null) {
                        Book book = jsonb.fromJson(json, Book.class);
                        books.add(book);
                    }
                }
            } while (!"0".equals(cursor));
        } catch (Exception e) {
            System.err.println("Redis unavailable for findAll: " + e.getMessage());
        }

        return books;
    }

    @Override
    public void create(Book book) {
        try {
            String cacheKey = "book: " + book.getEntityId().getId().toString();
            String json = jsonb.toJson(book);
            pool.set(cacheKey, json);
            pool.expire(cacheKey, CACHE_TTL_SECONDS);
        } catch (Exception e) {
            System.err.println("Redis unavailable for create: " + e.getMessage());
        }
    }

    @Override
    public boolean delete(Book book) {
        try {
            String cacheKey = "book: " + book.getEntityId().getId().toString();
            Long result = pool.del(cacheKey);
            return result > 0;
        } catch (Exception e) {
            System.err.println("Redis unavailable for delete: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean update(Book book) {
        try {
            String cacheKey = "book: " + book.getEntityId().getId().toString();
            String json = jsonb.toJson(book);
            pool.set(cacheKey, json);
            pool.expire(cacheKey, CACHE_TTL_SECONDS);
            return true;
        } catch (Exception e) {
            System.err.println("Redis unavailable for update: " + e.getMessage());
            return false;
        }
    }

    public void clearCache() {
        try {
            pool.flushAll();
        } catch (Exception e) {
            System.err.println("Redis unavailable for clearCache: " + e.getMessage());
        }
    }
}