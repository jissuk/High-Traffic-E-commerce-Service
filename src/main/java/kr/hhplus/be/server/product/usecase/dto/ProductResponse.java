package kr.hhplus.be.server.product.usecase.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Builder;
import lombok.ToString;


@JsonInclude(Include.NON_EMPTY)
@Builder
public record ProductResponse(
        long id,
        String productName,
        long price,
        long quantity
) {}