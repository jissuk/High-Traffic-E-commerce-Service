package kr.hhplus.be.server.product.usecase;

import kr.hhplus.be.server.common.annotation.UseCase;
import kr.hhplus.be.server.order.usecase.command.OrderItemCommand;
import kr.hhplus.be.server.product.domain.mapper.ProductMapper;
import kr.hhplus.be.server.product.domain.model.Product;
import kr.hhplus.be.server.product.domain.model.ProductEntity;
import kr.hhplus.be.server.product.domain.repository.ProductRepository;
import kr.hhplus.be.server.order.usecase.dto.OrderItemRequestDTO;
import kr.hhplus.be.server.product.exception.ProductNotFoundException;
import kr.hhplus.be.server.product.usecase.reader.ProductReader;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class CheckProductUseCase {
    private final ProductReader productReader;

    public void execute(OrderItemCommand command) {
        Product product = productReader.findProductOrThrow(command.productId());

        product.checkQuantity(command.quantity());
    }
}
