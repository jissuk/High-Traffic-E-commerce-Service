package kr.hhplus.be.server.product.domain.model;

import kr.hhplus.be.server.product.exception.InsufficientStockException;
import lombok.*;

@Getter
@Setter
@Builder
public class Product {

    long id;
    String productName;
    long price;
    long quantity;

    public void checkQuantity(long quantity) {
        this.quantity -= quantity;
        if(this.quantity < 0) {
            throw new InsufficientStockException();
        }
    }

    public void increaseStock(long quantity) {
        this.quantity += quantity;
    }

    public Product() {
    }

    public Product(long id, String productName, long price, long quantity) {
        this.id = id;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", productName='" + productName + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                '}';
    }

}
