package kr.hhplus.be.server.product.usecase;

import com.fasterxml.jackson.core.JsonProcessingException;
import kr.hhplus.be.server.common.annotation.UseCase;
import kr.hhplus.be.server.common.constant.RedisKey;
import kr.hhplus.be.server.product.domain.mapper.ProductRseponseMapper;
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

    private final ProductRseponseMapper productRseponseMapper;
    private final RedisTemplate<String, Long> redis;
    private final ProductRepository productRepository;


    public List<ProductResponse> execute() throws JsonProcessingException {

        int limit = 2;
        String destKey = RedisKey.Product.PRODUCT_SALES_3DAYS_TOTAL;

        Set<Long> redisZSetData = redis.opsForZSet().reverseRange(destKey, 0, limit);

        if (redisZSetData == null || redisZSetData.isEmpty()) {
            return popularProduct3DaysMysql();
        } else{
            return popularProduct3DaysRedis(redisZSetData);
        }
    }

    private List<ProductResponse> popularProduct3DaysMysql(){
        List<Product> productList = productRepository.findPopularProduct3Days();
        return productList.stream().map(productRseponseMapper::toDto).toList();
    }

    private List<ProductResponse> popularProduct3DaysRedis(Set<Long> redisZSetData ){
        List<Product> resultList = new ArrayList<>();
        for (Long productId : redisZSetData) {
            resultList.add(productRepository.findById(productId));
        }
        return resultList.stream().map(productRseponseMapper::toDto).toList();
    }
}