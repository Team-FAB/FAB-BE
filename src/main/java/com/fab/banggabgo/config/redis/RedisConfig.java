package com.fab.banggabgo.config.redis;

import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableCaching
@EnableRedisRepositories
public class RedisConfig extends CachingConfigurerSupport {

  @Value("${spring.redis.port}")
  private int port;
  @Value("${spring.redis.host}")
  private String host;

  @Value("${spring.redis.timeout}")
  private Long timeout;
  @Bean
  public LettuceConnectionFactory lettuceConnectionFactory(){
    LettuceClientConfiguration lettuceClientConfiguration = LettuceClientConfiguration.builder()
        .build();

    RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration(host,port);

    return new LettuceConnectionFactory(redisStandaloneConfiguration,lettuceClientConfiguration);
  }

  @Bean
  public RedisTemplate<?,?> redisTemplate(){
    RedisTemplate<String,String>  template = new RedisTemplate<>();
    template.setValueSerializer(new StringRedisSerializer());
    template.setKeySerializer(new StringRedisSerializer());
    template.setConnectionFactory(lettuceConnectionFactory());
    return template;
  }

  @Bean
  @Override
  public CacheManager cacheManager() {

    RedisCacheManager.RedisCacheManagerBuilder builder = RedisCacheManager
        .RedisCacheManagerBuilder
        .fromConnectionFactory(lettuceConnectionFactory());

    RedisCacheConfiguration configuration = RedisCacheConfiguration.defaultCacheConfig()
        .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
        .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()))
        .entryTtl(Duration.ofHours(timeout));

    builder.cacheDefaults(configuration);

    return builder.build();
  }

}
