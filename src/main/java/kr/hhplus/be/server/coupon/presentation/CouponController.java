package kr.hhplus.be.server.coupon.presentation;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.hhplus.be.server.coupon.application.usecase.IssueCouponUseCase;
import kr.hhplus.be.server.coupon.application.command.UserCouponCommand;
import kr.hhplus.be.server.coupon.presentation.dto.UserCouponRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/coupons")
@Tag(name = "coupon", description = "쿠폰 관련 API")
public class CouponController {

    private final IssueCouponUseCase registerUserCouponUseCase;

    @PostMapping("/issue")
    @Operation(summary = "선착순 쿠폰 발급", description = "유저는 선착순으로 제공되는 쿠폰을 발급 받아 등록합니다.", tags = {"CouponController"})
    public ResponseEntity<Void> issueCoupon(@RequestBody @Valid UserCouponRequest request){

        UserCouponCommand command = UserCouponCommand.from(request);
        registerUserCouponUseCase.execute(command);

        return ResponseEntity.noContent().build();
    }
}
