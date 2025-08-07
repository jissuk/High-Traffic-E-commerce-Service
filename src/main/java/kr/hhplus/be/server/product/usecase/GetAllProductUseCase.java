package kr.hhplus.be.server.product.usecase;

import kr.hhplus.be.server.common.annotation.UseCase;
import kr.hhplus.be.server.product.domain.mapper.ProductRseponseMapper;
import kr.hhplus.be.server.product.domain.model.Product;
import kr.hhplus.be.server.product.domain.repository.ProductRepository;
import kr.hhplus.be.server.product.exception.ProductNotFoundException;
import kr.hhplus.be.server.product.usecase.dto.ProductResponseDTO;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@UseCase
@RequiredArgsConstructor
public class GetAllProductUseCase {

    private final ProductRepository productRepository;
    private final ProductRseponseMapper responseMapper;

    public List<ProductResponseDTO> execute() {

        List<Product> productEntityList = productRepository.findAll();

        if (productEntityList == null || productEntityList.isEmpty()) {
            throw new ProductNotFoundException();
        }

        return productEntityList.stream()
                                .map(responseMapper::toDto)
                                .collect(Collectors.toList());
    }
}
