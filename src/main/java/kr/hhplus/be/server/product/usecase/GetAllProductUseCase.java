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

        List<ProductEntity> productEntityList = findProductOrThrow();

        List<Product> domainList = productEntityList.stream()
                                                .map(product -> domainMapper.toDomain(product))
                                                .toList();


        List<ProductResponseDTO> response = domainList.stream()
                                            .map(product -> responseMapper.toDto(product))
                                            .collect(Collectors.toList());

        return response;
    }

    private List<ProductEntity> findProductOrThrow() {
        List<ProductEntity> productEntityList = productRepository.findAll();
        if (productEntityList.isEmpty()) {
            throw new ProductNotFoundException();
        }
        return productEntityList;
    }

}
