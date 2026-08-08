package kr.hhplus.be.server.product.infrastructure.cache.subscriber;

import kr.hhplus.be.server.product.infrastructure.cache.keys.ProductCacheName;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductCacheSubscriber implements MessageListener {

    private final CacheManager cacheManager;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String key = new String(message.getBody());

        Cache cache = cacheManager.getCache(ProductCacheName.POPULAR_PRODUCT);

        if (cache != null) {
            cache.evict(key);
        }
    }
}
