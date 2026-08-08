package kr.hhplus.be.server.product.infrastructure.cache;

import kr.hhplus.be.server.product.infrastructure.cache.keys.ProductCacheName;
import kr.hhplus.be.server.product.infrastructure.cache.subscriber.ProductCacheSubscriber;
import kr.hhplus.be.server.product.infrastructure.redis.keys.ProductRedisKey;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.connection.Message;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ProductCacheSubscriberUnitTest {

    @Mock
    private CacheManager cacheManager;

    @Mock
    private Cache cache;

    @InjectMocks
    private ProductCacheSubscriber subscriber;


    @Test
    void 캐시_삭제_이벤트를_받으면_캐시를_삭제한다() {

        // given
        Message message = mock(Message.class);

        given(message.getBody())
                .willReturn(ProductRedisKey.PRODUCT_SALES_3DAYS_TOTAL.getBytes());

        given(cacheManager.getCache(ProductCacheName.POPULAR_PRODUCT))
                .willReturn(cache);

        // when
        subscriber.onMessage(message, null);

        // then
        verify(cache).evict(ProductRedisKey.PRODUCT_SALES_3DAYS_TOTAL);
    }
}