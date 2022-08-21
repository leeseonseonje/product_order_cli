package product.order.cli.app.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

import static lombok.AccessLevel.*;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class Order {

    private String name;
    private int quantity;
    private BigDecimal orderAmount;

    @Builder
    private Order(String name, int quantity, BigDecimal orderAmount) {
        this.name = name;
        this.quantity = quantity;
        this.orderAmount = orderAmount;
    }

    public static Order of(Product product, int quantity) {
        return Order.builder()
                .name(product.getName())
                .quantity(quantity)
                .orderAmount(orderAmountCalculate(product.getPrice(), quantity))
                .build();
    }

    private static BigDecimal orderAmountCalculate(BigDecimal productPrice, int quantity) {
        return productPrice.multiply(new BigDecimal(quantity));
    }

    @Override
    public String toString() {
        return this.name + " - " + this.quantity + "개";
    }
}
