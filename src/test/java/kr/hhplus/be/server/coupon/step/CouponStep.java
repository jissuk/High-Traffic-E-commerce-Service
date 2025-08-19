package kr.hhplus.be.server.coupon.step;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.hhplus.be.server.coupon.domain.model.*;
import kr.hhplus.be.server.coupon.usecase.command.UserCouponCommand;
import kr.hhplus.be.server.coupon.usecase.dto.UserCouponRequest;
import kr.hhplus.be.server.user.domain.model.UserEntity;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

public class CouponStep {

    private static String PATH_URL = "/coupons";

    public static UserCouponCommand 유저쿠폰커맨드_기본값(){
        return new UserCouponCommand(1L, 1L);
    }

    public static UserCouponRequest 유저쿠폰요청_기본값(){
        return UserCouponRequest.builder()
                .userId(1L)
                .couponId(1L)
                .build();
    }

    public static UserCouponRequest 유저쿠폰요청_유저ID지정(long userId){
        return UserCouponRequest.builder()
                .userId(userId)
                .couponId(1L)
                .build();
    }

    public static UserCouponRequest 유저쿠폰요청_쿠폰ID지정(long couponId){
        return UserCouponRequest.builder()
                .userId(1L)
                .couponId(couponId)
                .build();
    }

    public static Coupon 쿠폰_기본값(){
        return Coupon.builder()
                        .discount(2000L)
                        .description("여름특별할인쿠폰")
                        .quantity(500L)
                        .expiredAt(LocalDateTime.now().plusMonths(3))
                        .build();
    }

    public static UserCoupon 유저쿠폰_기본값(long userId, long couponId){
        return UserCoupon.builder()
                .discount(3000L)
                .couponStatus(CouponStatus.ISSUED)
                .description("여름 특별 할인 쿠폰")
                .userId(userId)
                .couponId(couponId)
                .build();
    }


    public static CouponEntity 쿠폰엔티티_기본값(){
        return CouponEntity.builder()
                        .discount(3000L)
                        .description("여름특별할인쿠폰")
                        .quantity(500L)
                        .expiredAt(LocalDateTime.now().plusMonths(3))
                        .build();
    }

    public static UserCouponEntity 유저쿠폰엔티티_기본값(UserEntity user, CouponEntity coupon){
        return UserCouponEntity.builder()
                .discount(3000L)
                .couponStatus(CouponStatus.ISSUED)
                .description("여름 특별 할인 쿠폰")
                .couponId(coupon.getId())
                .userId(user.getId())
                .build();
    }


    public static ResultActions 선착순쿠폰발급요청(MockMvc mockMvc, ObjectMapper objectMapper, UserCouponRequest request) throws Exception {
        return mockMvc.perform(post(PATH_URL + "/issue")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                        .andDo(print());
    }
}
