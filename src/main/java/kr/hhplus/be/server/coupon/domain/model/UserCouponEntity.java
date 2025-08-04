package kr.hhplus.be.server.coupon.domain.model;

import jakarta.persistence.*;
import kr.hhplus.be.server.user.domain.model.UserEntity;
import lombok.*;

@Entity
@Table(name = "USER_COUPONS")
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserCouponEntity {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private long discount;

    @Column
    private CouponStatus couponStatus;

    @Column
    private String description;

    @Column
    private long couponId;

    @Column
    private long userId;

}
