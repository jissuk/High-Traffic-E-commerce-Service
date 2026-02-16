package kr.hhplus.be.server.product.domain.model;

import jakarta.persistence.*;
import kr.hhplus.be.server.product.exception.InsufficientStockException;
import lombok.*;

@Entity
@Table(name = "PRODUCTS")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Product {
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

    public void deductQuantity(long quantity) {
        checkQuantity(quantity);
        this.quantity -= quantity;
    }

    public void checkQuantity(Long quantity) {
        if (this.quantity < quantity) {
            throw new InsufficientStockException();
        }
    }
}
