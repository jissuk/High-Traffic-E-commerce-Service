package kr.hhplus.be.server.order.domain.model;

import jakarta.persistence.*;
import kr.hhplus.be.server.product.domain.model.ProductEntity;
import lombok.*;

@Entity
@Table(name = "ORDER_ITEMS")
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class OrderItemEntity {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private long quantity;

    @Column
    private long price;

    @Column
    private long totalPrice;


    @Column
    private long orderId;

    @Column
    private long productId;

}
