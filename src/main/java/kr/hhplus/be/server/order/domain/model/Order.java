package kr.hhplus.be.server.order.domain.model;

import jakarta.persistence.*;
import kr.hhplus.be.server.coupon.domain.model.UserCoupon;
import kr.hhplus.be.server.order.usecase.command.OrderCommand;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "`ORDERS`")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Order {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    @Column
    private LocalDateTime createdAt;
    @Column
    private long totalPrice;
    @Column
    private long userId;
    @Version
    private long version;

    public static Order createPendingOrder(OrderCommand command, Product product, UserCoupon coupon) {
        long totalPrice = command.quantity() * product.getPrice() - coupon.getDiscount();

        return Order.builder()
                .orderStatus(OrderStatus.CREATED)
                .userId(command.userId())
                .createdAt(LocalDateTime.now())
                .totalPrice(totalPrice)
                .build();
    }
}