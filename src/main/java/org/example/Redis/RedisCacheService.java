package org.example.Redis;

import org.json.JSONObject;
import redis.clients.jedis.BuilderFactory;
import redis.clients.jedis.CommandArguments;
import redis.clients.jedis.CommandObject;
import redis.clients.jedis.JedisPooled;
import redis.clients.jedis.json.JsonProtocol;
import redis.clients.jedis.json.Path2;

public class RedisCacheService {

    private final JedisPooled pool;

    public RedisCacheService(JedisPooled pool) {
        this.pool = pool;
    }

    public void setJson(String key, JSONObject json) {
        CommandObject<String> command = new CommandObject<>(
                new CommandArguments(JsonProtocol.JsonCommand.SET)
                        .key(key)
                        .add(Path2.ROOT_PATH)
                        .add(json),
                BuilderFactory.STRING
        );
        pool.executeCommand(command);
    }

    public JSONObject getJson(String key) {
        CommandObject<String> command = new CommandObject<>(
                new CommandArguments(JsonProtocol.JsonCommand.GET)
                        .key(key)
                        .add(Path2.ROOT_PATH),
                BuilderFactory.STRING
        );
        String result = pool.executeCommand(command);
        if (result == null) {
            return null;
        }
        return new JSONObject(result); 
    }

    public void delJson(String key, String path) {
        CommandObject<Long> command = new CommandObject<>(
                new CommandArguments(JsonProtocol.JsonCommand.DEL)
                        .key(key)
                        .add(path),
                BuilderFactory.LONG
        );
        pool.executeCommand(command);
    }

    public void delete(String key) {
        pool.del(key);
    }

    public void expire(String key, long ttlInSeconds) {
        pool.expire(key, ttlInSeconds);
    }
}