package kr.hhplus.be.server.user.presentation.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Builder;

@JsonInclude(Include.NON_EMPTY)
@Builder
public record UserResponse(
    long userId,
    long point
){}
