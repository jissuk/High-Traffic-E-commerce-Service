package kr.hhplus.be.server.product.usecase;

import kr.hhplus.be.server.common.annotation.UseCase;
import kr.hhplus.be.server.product.domain.mapper.ProductMapper;
import kr.hhplus.be.server.product.domain.mapper.ProductRseponseMapper;
import kr.hhplus.be.server.product.domain.model.Product;
import kr.hhplus.be.server.product.domain.model.ProductEntity;
import kr.hhplus.be.server.product.domain.repository.ProductRepository;
import kr.hhplus.be.server.product.exception.ProductNotFoundException;
import kr.hhplus.be.server.product.usecase.dto.ProductResponseDTO;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class GetProductUseCase {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final ProductRseponseMapper productRseponseMapper;

    public ProductResponseDTO execute(long productId) {

        ProductEntity productEntity = findProductOrThrow(productId);

        Product prdouct = productMapper.toDomain(productEntity);
        ProductResponseDTO response = productRseponseMapper.toDto(prdouct);

        return response;
    }

    private ProductEntity findProductOrThrow(long id) {
        ProductEntity product = productRepository.findById(id);
        if (product == null) {
            throw new ProductNotFoundException();
        }
        return product;
    }
}
