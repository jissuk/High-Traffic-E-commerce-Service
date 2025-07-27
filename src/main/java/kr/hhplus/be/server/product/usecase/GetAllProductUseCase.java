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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@UseCase
@RequiredArgsConstructor
public class GetAllProductUseCase {

    private final ProductRepository productRepository;
    private final ProductMapper domainMapper;
    private final ProductRseponseMapper responseMapper;

    public List<ProductResponseDTO> execute() {

        Map<Long, ProductEntity> productMap = findProductOrThrow();
        List<ProductEntity> entityList = new ArrayList<>(productMap.values());

        List<Product> domainList = entityList.stream()
                                                .map(product -> domainMapper.toDomain(product))
                                                .collect(Collectors.toList());


        List<ProductResponseDTO> response = domainList.stream()
                                            .map(product -> responseMapper.toDto(product))
                                            .collect(Collectors.toList());

        return response;
    }

    private Map<Long, ProductEntity> findProductOrThrow() {
        Map<Long, ProductEntity> productMap = productRepository.findAll();
        if (productMap == null) {
            throw new ProductNotFoundException();
        }
        return productMap;
    }

}
