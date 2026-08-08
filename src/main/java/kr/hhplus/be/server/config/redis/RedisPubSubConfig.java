package kr.hhplus.be.server.config.redis;

import kr.hhplus.be.server.product.infrastructure.redis.keys.ProductRedisKey;
import kr.hhplus.be.server.product.infrastructure.cache.subscriber.ProductCacheSubscriber;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

// 여기 자세히 기록하기
@Configuration
@RequiredArgsConstructor
public class RedisPubSubConfig {

    private final RedisConnectionFactory redisConnectionFactory;
    private final ProductCacheSubscriber cacheSubscriber;

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer() {

        RedisMessageListenerContainer container =
                new RedisMessageListenerContainer();

        container.setConnectionFactory(redisConnectionFactory);

        container.addMessageListener(
                cacheSubscriber,
                new ChannelTopic(ProductRedisKey.POPULAR_PRODUCT_EVICT)
        );

        return container;
    }
}