package kr.hhplus.be.server.coupon.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.hhplus.be.server.common.response.CommonResponse;
import kr.hhplus.be.server.coupon.facade.CouponFacade;
import kr.hhplus.be.server.coupon.usecase.dto.UserCouponRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/coupons")
@Tag(name = "coupon", description = "쿠폰 관련 API")
public class CouponController {

    private final CouponFacade couponFacade;

    @PostMapping("/issue")
    @Operation(summary = "선착순 쿠폰 발급", description = "유저는 선착순으로 제공되는 쿠폰을 발급 받아 등록합니다.", tags = {"CouponController"})
    public ResponseEntity<CommonResponse> issueCoupon(@RequestBody @Valid UserCouponRequestDTO request) {

        couponFacade.issueCoupon(request);

        return ResponseEntity
                .ok()
                .body(new CommonResponse(HttpStatus.NO_CONTENT, "success", null));
    }

}
