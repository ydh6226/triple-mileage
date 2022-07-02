package com.triple.mileage.configuration;

import lombok.RequiredArgsConstructor;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.redisson.config.Config;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@Profile("!test")
@Configuration
@RequiredArgsConstructor
@EnableRedisRepositories
public class RedissonConfiguration {

    private final RedisProperties redisProperties;

    @Bean
    public RedissonClient redisClient() {
        Config config = new Config();
        config.setCodec(new StringCodec());
        config.useSingleServer()
                .setAddress(getAddress())
                .setPassword(redisProperties.getPassword());

        return Redisson.create(config);
    }

    private String getAddress() {
        return String.format("redis://%s:%s", redisProperties.getHost(), redisProperties.getPort());
    }
}
