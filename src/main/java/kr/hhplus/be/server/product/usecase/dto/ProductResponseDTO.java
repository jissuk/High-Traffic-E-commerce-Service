package kr.hhplus.be.server.product.usecase.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponseDTO {

    long id;
    String productName;
    long price;
    long quantity;

}
