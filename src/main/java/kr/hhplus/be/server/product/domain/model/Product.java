package kr.hhplus.be.server.product.domain.model;

import kr.hhplus.be.server.product.exception.InsufficientStockException;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
public class Product {

    private long id;
    private String productName;
    private long price;
    private long quantity;

    public void checkQuantity(long quantity) {
        if (this.quantity < quantity) {
            throw new InsufficientStockException();
        }
    }

    public void deductQuantity(long quantity) {
        checkQuantity(quantity);
        this.quantity -= quantity;
    }
}
