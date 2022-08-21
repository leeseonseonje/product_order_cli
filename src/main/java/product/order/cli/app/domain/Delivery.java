package product.order.cli.app.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

import static lombok.AccessLevel.*;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class Delivery {

    private BigDecimal fee;

    @Builder
    private Delivery(BigDecimal fee) {
        this.fee = fee;
    }

    public static Delivery of(int fee) {
        return Delivery.builder().fee(new BigDecimal(fee)).build();
    }

    public BigDecimal deliveryFeeSetup(BigDecimal totalOrderAmount) {
        if (totalOrderAmount.compareTo(new BigDecimal("50000")) >= 0) {
            this.fee = new BigDecimal(0);
        }
        return fee;
    }
}
