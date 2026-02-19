package kr.hhplus.be.server.coupon.step;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.hhplus.be.server.coupon.domain.model.*;
import kr.hhplus.be.server.coupon.usecase.command.UserCouponCommand;
import kr.hhplus.be.server.coupon.usecase.dto.UserCouponRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

public class CouponStep {
    private static String PATH_URL = "/coupons";
    private static final Long DEFAULT_USER_ID = 1L;
    private static final Long DEFAULT_COUPON_ID = 1L;
    private static final Long DEFAULT_COUPON_DISCOUNT = 3000L;
    private static final Long DEFAULT_COUPON_QUANTITY = 500L;
    private static final String DEFAULT_COUPON_DESCRIPTION = "여름 특별 할인 쿠폰";

    public static UserCouponCommand defaultUserCouponCommand(){
        return new UserCouponCommand(DEFAULT_USER_ID, DEFAULT_COUPON_ID);
    }
    public static UserCouponCommand userCouponCommandWithUserId(Long userId){
        return new UserCouponCommand(userId, DEFAULT_COUPON_ID);
    }

    public static UserCouponRequest defaultUserCouponRequest(){
        return UserCouponRequest.builder()
                .userId(DEFAULT_USER_ID)
                .couponId(DEFAULT_COUPON_ID)
                .build();
    }

    public static Coupon defaultCoupon(){
        return Coupon.builder()
                        .discount(DEFAULT_COUPON_DISCOUNT)
                        .description(DEFAULT_COUPON_DESCRIPTION)
                        .quantity(DEFAULT_COUPON_QUANTITY)
                        .expiredAt(LocalDateTime.now().plusMonths(3))
                        .build();
    }

    public static UserCoupon defaultUserCoupon(long userId, long couponId){
        return UserCoupon.builder()
                .discount(DEFAULT_COUPON_DISCOUNT)
                .couponStatus(CouponStatus.ISSUED)
                .description(DEFAULT_COUPON_DESCRIPTION)
                .userId(userId)
                .couponId(couponId)
                .build();
    }

    public static Coupon defaultCouponEntity(){
        return Coupon.builder()
                        .discount(DEFAULT_COUPON_DISCOUNT)
                        .description(DEFAULT_COUPON_DESCRIPTION)
                        .quantity(DEFAULT_COUPON_QUANTITY)
                        .expiredAt(LocalDateTime.now().plusMonths(3))
                        .build();
    }

    public static ResultActions issueCouponRequest(MockMvc mockMvc, ObjectMapper objectMapper, UserCouponRequest request) throws Exception {
        return mockMvc.perform(post(PATH_URL + "/issue")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                        .andDo(print());
    }
}
