package kr.hhplus.be.server.product.usecase;

import com.fasterxml.jackson.core.JsonProcessingException;
import kr.hhplus.be.server.common.annotation.UseCase;
import kr.hhplus.be.server.product.domain.mapper.ProductResponseMapper;
import kr.hhplus.be.server.product.domain.model.Product;
import kr.hhplus.be.server.product.domain.repository.ProductRepository;
import kr.hhplus.be.server.product.usecase.dto.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@UseCase
@RequiredArgsConstructor
public class GetPopularProductUseCase {

    private final ProductResponseMapper productResponseMapper;
    private final RedisTemplate<String, Long> redis;
    private final ProductRepository productRepository;

    public static final String PRODUCT_SALES_3DAYS_TOTAL  = "product:sales:3days:total";
    public static final long TOP_PRODUCT_SALES_LIMIT = 2;

    public List<ProductResponse> execute() throws JsonProcessingException {

        Set<Long> redisZSetData = redis.opsForZSet().reverseRange(PRODUCT_SALES_3DAYS_TOTAL, 0, TOP_PRODUCT_SALES_LIMIT);

        if (redisZSetData == null || redisZSetData.isEmpty()) {
            return popularProduct3DaysMysql();
        } else{
            return popularProduct3DaysRedis(redisZSetData);
        }
    }

    private List<ProductResponse> popularProduct3DaysMysql(){
        List<Product> productList = productRepository.findPopularProduct3Days();
        return productList.stream().map(productResponseMapper::toDto).toList();
    }

    private List<ProductResponse> popularProduct3DaysRedis(Set<Long> redisZSetData ){
        List<Product> resultList = new ArrayList<>();
        for (Long productId : redisZSetData) {
            resultList.add(productRepository.findById(productId));
        }
        return resultList.stream().map(productResponseMapper::toDto).toList();
    }
}