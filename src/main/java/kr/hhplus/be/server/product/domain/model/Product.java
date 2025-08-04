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
        this.quantity -= quantity;
        if(this.quantity < 0) {
            throw new InsufficientStockException();
        }
    }
}
