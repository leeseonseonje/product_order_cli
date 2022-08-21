package product.order.cli.app.domain;

import product.order.cli.app.exception.SoldOutException;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import java.math.BigDecimal;

import static lombok.AccessLevel.*;

@Getter
@Entity
@NoArgsConstructor(access = PROTECTED)
public class Product {

    @Id
    private Long number;

    private String name;

    private BigDecimal price;

    private int stockQuantity;

    @Builder
    private Product(Long number, String name, BigDecimal price, int stockQuantity) {
        this.number = number;
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }

    public static Product of(Long number, String name, String price, int stockQuantity) {
        return Product.builder()
                .number(number)
                .name(name)
                .price(new BigDecimal(price))
                .stockQuantity(stockQuantity)
                .build();
    }

    public void stockQuantityUpdate(int quantity) {
        checkStockQuantity(quantity);
        this.stockQuantity -= quantity;
    }

    private void checkStockQuantity(int quantity) {
        if (quantity > this.stockQuantity) {
            throw new SoldOutException("SoldOutException 발생, 주문한 상품량이 재고량보다 큽니다.");
        }
    }
}