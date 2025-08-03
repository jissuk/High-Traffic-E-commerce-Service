package kr.hhplus.be.server.coupon.domain.model;

import jakarta.persistence.*;
import kr.hhplus.be.server.user.domain.model.UserEntity;
import lombok.*;

@Entity
@Table(name = "TBL_USER_COUPON")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private CouponEntity coupon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private UserEntity user;

}
