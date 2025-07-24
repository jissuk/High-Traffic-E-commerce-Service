package kr.hhplus.be.server.order.usecase;

import kr.hhplus.be.server.common.annotation.UseCase;
import kr.hhplus.be.server.order.domain.mapper.OrderItemMapper;
import kr.hhplus.be.server.order.domain.model.OrderEntity;
import kr.hhplus.be.server.order.domain.model.OrderItem;
import kr.hhplus.be.server.order.domain.model.OrderItemEntity;
import kr.hhplus.be.server.order.domain.repository.OrderItemRepository;
import kr.hhplus.be.server.order.domain.repository.OrderRepositroy;
import kr.hhplus.be.server.order.exception.OrderNotFoundException;
import kr.hhplus.be.server.order.usecase.dto.OrderItemRequestDTO;
import kr.hhplus.be.server.product.domain.model.ProductEntity;
import kr.hhplus.be.server.product.domain.repository.ProductRepository;
import kr.hhplus.be.server.product.exception.ProductNotFoundException;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class CreateOrderItemUseCase {

    private final ProductRepository productRepository;
    private final OrderRepositroy orderRepositroy;
    private final OrderItemRepository orderItemRepository;

    private final OrderItemMapper orderItemMapper;

    public void execute(OrderItemRequestDTO request) {
        ProductEntity product = findProductOrThrow(request.getProductId());
        OrderEntity order = findOrderOrThrow(request.getOrderId());

        OrderItem orderItem = OrderItem.createBeforeOrderItem(request);
        OrderItemEntity orderItemEntity = orderItemMapper.toEntity(orderItem);

        orderItemEntity.setProduct(product);
        orderItemEntity.setOrder(order);
        orderItemRepository.save(orderItemEntity);
    }

    private ProductEntity findProductOrThrow(long id) {
        ProductEntity product = productRepository.findById(id);
        if (product == null) {
            throw new ProductNotFoundException();
        }
        return product;
    }

    private OrderEntity findOrderOrThrow(long id) {
        OrderEntity order = orderRepositroy.findById(id);
        if (order == null) {
            throw new OrderNotFoundException();
        }
        return order;
    }

}
