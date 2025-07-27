package kr.hhplus.be.server.product.usecase;

import kr.hhplus.be.server.common.annotation.UseCase;
import kr.hhplus.be.server.product.domain.mapper.ProductMapper;
import kr.hhplus.be.server.product.domain.model.Product;
import kr.hhplus.be.server.product.domain.model.ProductEntity;
import kr.hhplus.be.server.product.domain.repository.ProductRepository;
import kr.hhplus.be.server.order.usecase.dto.OrderItemRequestDTO;
import kr.hhplus.be.server.product.exception.ProductNotFoundException;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class CheckProductUseCase {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public void execute(OrderItemRequestDTO request) {
        ProductEntity productEntity = findProductOrThrow(request.getProductId());
        Product product = productMapper.toDomain(productEntity);
        product.checkQuantity(request.getQuantity());
    }

    private ProductEntity findProductOrThrow(long id) {
        ProductEntity product = productRepository.findById(id);
        if (product == null) {
            throw new ProductNotFoundException();
        }
        return product;
    }
}
