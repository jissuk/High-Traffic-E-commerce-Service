package kr.hhplus.be.server.product.usecase;

import kr.hhplus.be.server.common.annotation.UseCase;
import kr.hhplus.be.server.product.domain.mapper.ProductRseponseMapper;
import kr.hhplus.be.server.product.domain.model.Product;
import kr.hhplus.be.server.product.usecase.dto.ProductResponseDTO;
import kr.hhplus.be.server.product.usecase.reader.ProductReader;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class GetProductUseCase {
    private final ProductReader productReader;
    private final ProductRseponseMapper productRseponseMapper;

    public ProductResponseDTO execute(long productId) {
        Product product = productReader.findProductOrThrow(productId);

        return productRseponseMapper.toDto(product);
    }
}
