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

import java.util.List;
import java.util.stream.Collectors;

@UseCase
@RequiredArgsConstructor
public class GetAllProductUseCase {

    private final ProductRepository productRepository;
    private final ProductMapper domainMapper;
    private final ProductRseponseMapper responseMapper;

    public List<ProductResponseDTO> execute() {

        List<ProductEntity> productEntityList = productRepository.findAll();

        if (productEntityList == null) {
            throw new ProductNotFoundException();
        }

        List<Product> domainList = productEntityList.stream()
                                                .map(domainMapper::toDomain)
                                                .toList();

        List<ProductResponseDTO> response = domainList.stream()
                                            .map(responseMapper::toDto)
                                            .collect(Collectors.toList());

        return response;
    }
}
