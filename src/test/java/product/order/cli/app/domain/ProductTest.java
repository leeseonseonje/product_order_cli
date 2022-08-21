package product.order.cli.app.domain;

import product.order.cli.app.exception.SoldOutException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class ProductTest {

    Product product;

    @BeforeEach
    void init() {
        product = Product.of(1L, "productA", "10000", 100);
    }

    @Test
    @DisplayName("상품의 재고수가 입력한 수량 만큼 차감되어야 함")
    void stockQuantityUpdateSoldOutTest() {
        product.stockQuantityUpdate(95);
        assertThat(product.getStockQuantity()).isEqualTo(5);
    }

    @Test
    @DisplayName("재고수 보다 많은 수량을 입력 할 시에 SoldOutException 발생")
    void stockQuantityUpdateSoldOutExceptionTest() {
        assertThatThrownBy(() -> product.stockQuantityUpdate(101)).isInstanceOf(SoldOutException.class);
    }
}
