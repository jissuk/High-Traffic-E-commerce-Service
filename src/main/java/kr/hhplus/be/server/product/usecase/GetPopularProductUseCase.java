package kr.hhplus.be.server.product.usecase;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.hhplus.be.server.common.annotation.UseCase;
import kr.hhplus.be.server.product.domain.mapper.ProductRseponseMapper;
import kr.hhplus.be.server.product.domain.model.Product;
import kr.hhplus.be.server.product.domain.repository.ProductRepository;
import kr.hhplus.be.server.product.usecase.dto.ProductResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.zset.Aggregate;
import org.springframework.data.redis.connection.zset.Weights;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@UseCase
@RequiredArgsConstructor
public class GetPopularProductUseCase {

    private final ProductRseponseMapper productRseponseMapper;
    private final RedisTemplate redisTemplate;
    private final RedisTemplate<String, Object> redis;
    private final ObjectMapper objectMapper;

    private final ProductRepository productRepository;

    public List<ProductResponseDTO> execute() throws JsonProcessingException {

        List<Product> resultList = new ArrayList<>();
        LocalDate today = LocalDate.now(ZoneId.of("Asia/Seoul"));
        String prefix = "sales:";
        String key1 = prefix + today.minusDays(1);
        String key2 = prefix + today.minusDays(2);
        String key3 = prefix + today.minusDays(3);
        String destKey =  prefix + "total";

        redisTemplate.execute((RedisCallback<Void>) connection -> {
            connection.zUnionStore(
                    destKey.getBytes(),
                    Aggregate.SUM,
                    Weights.of(1, 1, 1),
                    key1.getBytes(),
                    key2.getBytes(),
                    key3.getBytes()
            );
            return null;
        });

        Set<Object> redisZSetData = redis.opsForZSet().reverseRange(destKey, 0, 2);

        if (redisZSetData == null || redisZSetData.isEmpty()) {
            List<Object[]> dbResultList = productRepository.findPopularProduct3Days();
            return dbResultList.stream()
                    .map(row -> {
                        Long productId = ((Number) row[0]).longValue();

                        Product product = productRepository.findById(productId);
                        return productRseponseMapper.toDto(product);
                    })
                    .toList();
        } else{
            for (Object obj : redisZSetData) {
                Object redisHashData = redis.opsForHash().get("PopularProduct", Long.parseLong(obj.toString()));

                Product restoredProduct = objectMapper.readValue(redisHashData.toString(), Product.class);
                resultList.add(restoredProduct);
            }
        }

        return resultList.stream().map(productRseponseMapper::toDto).toList();
    }

}
