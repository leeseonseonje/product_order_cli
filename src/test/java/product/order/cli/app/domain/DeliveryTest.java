package product.order.cli.app.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;

public class DeliveryTest {

    Delivery delivery;

    @BeforeEach
    void init() {
        delivery = Delivery.of(2500);
    }

    @Test
    @DisplayName("배송비 책정, 총 주문금액이 5만원 미만이면 2500원의 배송비 추가")
    void deliveryFeeTest() {
        BigDecimal deliveryFee = delivery.deliveryFeeSetup(new BigDecimal(49999));
        assertThat(deliveryFee).isEqualTo(new BigDecimal(2500));
    }

    @Test
    @DisplayName("배송비 책정, 총 주문금액이 5만원 이상이면 배송비 무료")
    void deliveryFeeFreeTest() {
        BigDecimal deliveryFeeA = delivery.deliveryFeeSetup(new BigDecimal(50000));
        assertThat(deliveryFeeA).isEqualTo(new BigDecimal(0));

        BigDecimal deliveryFeeB = delivery.deliveryFeeSetup(new BigDecimal(50001));
        assertThat(deliveryFeeB).isEqualTo(new BigDecimal(0));
    }
}
