package com.example.BookManagement.redisDumper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import java.util.Map;

//@Component
public class RedisDumper {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public void dumpAllToConsole() {

        StringRedisSerializer keySerializer = new StringRedisSerializer();
        redisTemplate.execute((RedisCallback<Void>) connection -> {
            var options = org.springframework.data.redis.core.ScanOptions.scanOptions()
                    .match("*")
                    .count(500)
                    .build();

            try (var cursor = connection.scan(options)) {
                while (cursor.hasNext()) {
                    byte[] rawKey = cursor.next();
                    String key = keySerializer.deserialize(rawKey);
                    DataType type = connection.type(rawKey);

                    System.out.println("KEY: " + key + "  TYPE: " + type);

                    switch (type) {
                        case STRING -> {
                            byte[] rawVal = connection.get(rawKey);
                            Object val = redisTemplate.getValueSerializer().deserialize(rawVal);
                            System.out.println("VALUE: " + val);
                        }
                        case HASH -> {
                            Map<byte[], byte[]> rawMap = connection.hGetAll(rawKey);
                            System.out.println("HASH:");
                            rawMap.forEach((k, v) ->
                                    System.out.println("  " + keySerializer.deserialize(k) + " -> " +
                                            new String(v))); // или use value serializer
                        }
                        case LIST -> {
                            var list = connection.lRange(rawKey, 0, -1);
                            System.out.println("LIST: " + list);
                        }
                        case SET -> {
                            var members = connection.sMembers(rawKey);
                            System.out.println("SET: " + members);
                        }
                        case ZSET -> {
                            var zset = connection.zRangeWithScores(rawKey, 0, -1);
                            System.out.println("ZSET: " + zset);
                        }
                        default -> System.out.println("<unsupported or unknown type>");
                    }
                    System.out.println("-------------------------------------------------");
                }
            }
            return null;
        });
    }
}
