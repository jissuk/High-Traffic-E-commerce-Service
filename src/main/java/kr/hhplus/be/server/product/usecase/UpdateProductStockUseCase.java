package kr.hhplus.be.server.product.usecase;

import kr.hhplus.be.server.common.annotation.UseCase;
import kr.hhplus.be.server.order.domain.model.OrderItemEntity;
import kr.hhplus.be.server.order.domain.repository.OrderItemRepository;
import kr.hhplus.be.server.order.exception.OrderItemNotFoundException;
import kr.hhplus.be.server.payment.usecase.dto.PaymentRequestDTO;
import kr.hhplus.be.server.product.domain.mapper.ProductMapper;
import kr.hhplus.be.server.product.domain.model.Product;
import kr.hhplus.be.server.product.domain.model.ProductEntity;
import kr.hhplus.be.server.product.domain.repository.ProductRepository;
import kr.hhplus.be.server.product.exception.ProductNotFoundException;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class UpdateProductStockUseCase {

    private final ProductRepository productRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductMapper productMapper;

    public void execute(PaymentRequestDTO request) {

        ProductEntity productEntity = findProductOrThrow(request.getProductId());
        OrderItemEntity orderItemEntity = findOrderItemOrThrow(request.getOrderItemId());

        Product product = productMapper.toDomain(productEntity);
        product.checkQuantity(orderItemEntity.getQuantity());

        ProductEntity updateProduct = productMapper.toEntity(product);
        productRepository.update(updateProduct);
    }

    private ProductEntity findProductOrThrow(long id) {
        ProductEntity product = productRepository.findById(id);
        if (product == null) {
            throw new ProductNotFoundException();
        }
        return product;
    }

    private OrderItemEntity findOrderItemOrThrow(long id) {
        OrderItemEntity orderItem = orderItemRepository.findById(id);
        if (orderItem == null) {
            throw new OrderItemNotFoundException();
        }
        return orderItem;
    }

    public void compensate(PaymentRequestDTO request) {
        ProductEntity productEntity = findProductOrThrow(request.getProductId());
        OrderItemEntity orderItemEntity = findOrderItemOrThrow(request.getOrderItemId());
        Product product = productMapper.toDomain(productEntity);

        product.increaseStock(orderItemEntity.getQuantity());

        ProductEntity updateProduct = productMapper.toEntity(product);
        productRepository.update(updateProduct);

        System.out.println("상품 재고 롤백 완료: productId=" + request.getProductId() + ", 복구 수량= " + orderItemEntity.getQuantity());
    }
}
