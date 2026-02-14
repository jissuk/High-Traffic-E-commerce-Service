package kr.hhplus.be.server.product.domain.model;

import kr.hhplus.be.server.product.exception.InsufficientStockException;
import lombok.*;

import java.io.Serializable;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product implements Serializable {

    private long id;
    private String productName;
    private long price;
    private long quantity;

    public void deductQuantity(long quantity) {
        if (this.quantity < quantity) {
            throw new InsufficientStockException();
        }

        this.quantity -= quantity;
    }

    public void checkQuantity(Long quantity) {
        if (this.quantity < quantity) {
            throw new InsufficientStockException();
        }
    }
}
