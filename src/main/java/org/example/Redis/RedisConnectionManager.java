package org.example.Redis;

import redis.clients.jedis.DefaultJedisClientConfig;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisPooled;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class RedisConnectionManager {
    private static JedisPooled pool;

    private RedisConnectionManager() {}

    public static void initConnectionFromConfig(String configFilePath) {
        Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream(configFilePath)) {
            properties.load(fis);

            String host = properties.getProperty("redis.host", "localhost");
            int port = Integer.parseInt(properties.getProperty("redis.port", "6379"));
            String password = properties.getProperty("redis.password", "");

            initConnection(host, port, 2000, password);

        } catch (IOException e) {
            throw new RuntimeException("Failed to load Redis configuration from file.", e);
        }
    }

    public static void initConnection(String host, int port, int timeout, String password) {
        if (pool == null) {
            if (password == null || password.isEmpty()) {
                pool = new JedisPooled(new HostAndPort(host, port));
            } else {
                pool = new JedisPooled(
                        new HostAndPort(host, port),
                        DefaultJedisClientConfig.builder()
                                .password(password)
                                .timeoutMillis(timeout)
                                .build()
                );
            }
        }
    }

    public static JedisPooled getJedis() {
        if (pool == null) {
            System.err.println("Redis connection has not been initialized: ");
        }
        return pool;
    }
}