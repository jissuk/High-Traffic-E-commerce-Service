package kr.hhplus.be.server.product.usecase;

import kr.hhplus.be.server.common.annotation.UseCase;
import kr.hhplus.be.server.product.domain.mapper.ProductRseponseMapper;
import kr.hhplus.be.server.product.domain.model.Product;
import kr.hhplus.be.server.product.domain.repository.ProductRepository;
import kr.hhplus.be.server.product.exception.ProductNotFoundException;
import kr.hhplus.be.server.product.usecase.dto.ProductResponseDTO;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class GetProductUseCase {
    private final ProductRepository productRepository;
    private final ProductRseponseMapper productRseponseMapper;

    public ProductResponseDTO execute(long productId) {
        Product product = productRepository.findById(productId);
        return productRseponseMapper.toDto(product);
    }
}
