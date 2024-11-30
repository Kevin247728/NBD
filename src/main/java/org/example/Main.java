package org.example;

import org.example.Redis.RedisConnectionManager;
import org.example.exceptions.BookAlreadyRentedException;
import org.example.exceptions.TooManyException;
import org.example.models.Book;
import org.example.models.Client;
import org.example.models.NonStudent;
import org.example.models.Rent;
import org.example.repositories.MgdBookRepository;
import org.example.repositories.MgdClientRepository;
import org.example.repositories.MgdRentRepository;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPooled;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
    }
}
