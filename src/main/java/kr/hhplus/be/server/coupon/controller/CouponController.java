package kr.hhplus.be.server.coupon.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/coupons")
@Tag(name = "coupon", description = "쿠폰 관련 API")
public class CouponController {

    @GetMapping
    @Operation(summary = "쿠폰 조회", description = "유저가 현재 사용할 수 있는 쿠폰을 조회합니다.", tags = {"CouponController"})
    public ResponseEntity<?> getAllCoupons(){
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{couponId}/issue")
    @Operation(summary = "선착순 쿠폰 발급", description = "유저는 선착순으로 제공되는 쿠폰을 발급 받아 등록합니다.", tags = {"CouponController"})
    public ResponseEntity<?> issueCoupon(){
        return ResponseEntity.ok().build();
    }

}
