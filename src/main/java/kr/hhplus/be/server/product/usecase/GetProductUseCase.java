package kr.hhplus.be.server.product.usecase;

import kr.hhplus.be.server.common.annotation.UseCase;
import kr.hhplus.be.server.product.domain.mapper.ProductResponseMapper;
import kr.hhplus.be.server.product.domain.model.Product;
import kr.hhplus.be.server.product.domain.repository.ProductRepository;
import kr.hhplus.be.server.product.usecase.dto.ProductResponse;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class GetProductUseCase {
    private final ProductRepository productRepository;
    private final ProductResponseMapper productResponseMapper;

    public ProductResponse execute(long productId) {
        Product product = productRepository.findById(productId);
        return productResponseMapper.toDto(product);
    }
}
