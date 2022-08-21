package product.order.cli.app.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;

public class OrderTest {

    Product product;

    @BeforeEach
    void init() {
        product = Product.of(1234L, "product", "10000", 100);
    }

    @Test
    @DisplayName("주문 금액 계산")
    void OrderAmountCalcTest() {
        Order order = Order.of(product, 50);
        assertThat(order.getOrderAmount()).isEqualTo(new BigDecimal("500000"));
    }
}
