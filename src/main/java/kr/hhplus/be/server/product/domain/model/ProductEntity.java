package kr.hhplus.be.server.product.domain.model;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "TBL_PRODUCT")
@Getter
@Setter
@Builder
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


    public ProductEntity() {
    }

    public ProductEntity(long id, String productName, long price, long quantity) {
        this.id = id;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
    }

}
