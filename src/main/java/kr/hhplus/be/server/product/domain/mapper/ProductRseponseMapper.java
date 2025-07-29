package kr.hhplus.be.server.product.domain.mapper;

import kr.hhplus.be.server.product.domain.model.Product;
import kr.hhplus.be.server.product.usecase.dto.ProductResponseDTO;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductRseponseMapper {
    ProductResponseDTO toDto(Product domain);
}