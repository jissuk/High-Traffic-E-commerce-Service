package kr.hhplus.be.server.product.domain.model;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "PRODUCTS")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ProductEntity {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String productName;

    @Column
    private long price;

    @Column
    private long quantity;

}
