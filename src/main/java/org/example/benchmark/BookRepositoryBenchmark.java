package org.example.benchmark;

import org.example.Redis.RedisConnectionManager;
import org.example.models.Book;
import org.example.repositories.MgdBookRepository;
import org.example.repositories.RedisBookRepository;
import org.example.repositories.RedisMongoBookRepositoryDecorator;
import org.openjdk.jmh.annotations.*;
import redis.clients.jedis.JedisPooled;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Thread)
public class BookRepositoryBenchmark {

    private RedisBookRepository redisBookRepository;
    private MgdBookRepository mgdBookRepository;
    private JedisPooled jedisPool;
    private RedisMongoBookRepositoryDecorator decorator;

    private Book book15;

    @Setup(Level.Trial)
    public void setup() {
        RedisConnectionManager.initConnectionFromConfig("src/main/resources/application.properties");
        jedisPool = RedisConnectionManager.getJedis();
        this.redisBookRepository = new RedisBookRepository(jedisPool);
        this.mgdBookRepository = new MgdBookRepository();
        this.decorator = new RedisMongoBookRepositoryDecorator(redisBookRepository, mgdBookRepository);

        Book book1 = new Book("TestBook1");
        decorator.create(book1);

        Book book2 = new Book("TestBook2");
        decorator.create(book2);

        Book book3 = new Book("TestBook3");
        decorator.create(book3);

        Book book4 = new Book("TestBook4");
        decorator.create(book4);

        Book book5 = new Book("TestBook5");
        decorator.create(book5);

        Book book6 = new Book("TestBook6");
        decorator.create(book6);

        Book book7 = new Book("TestBook7");
        decorator.create(book7);

        Book book8 = new Book("TestBook8");
        decorator.create(book8);

        Book book9 = new Book("TestBook9");
        decorator.create(book9);

        Book book10 = new Book("TestBook10");
        decorator.create(book10);

        Book book11 = new Book("TestBook11");
        decorator.create(book11);

        Book book12 = new Book("TestBook12");
        decorator.create(book12);

        Book book13 = new Book("TestBook13");
        decorator.create(book13);

        Book book14 = new Book("TestBook14");
        decorator.create(book14);

        book15 = new Book("TestBook15");
        decorator.create(book15);
        //decorator.getRedisRepository().clearCache();
    }

    @Benchmark
    public Book testCacheHit() {
        return decorator.findById(book15.getEntityId());
    }

//    @Benchmark
//    public Book testCacheMissMongoHit() {
//        return decorator.findById(book15.getEntityId());
//    }

    @Benchmark
    public Book testMongoHit() {
        return mgdBookRepository.findById(book15.getEntityId());
    }
}