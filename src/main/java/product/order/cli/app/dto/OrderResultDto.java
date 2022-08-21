package product.order.cli.app.dto;

import product.order.cli.app.domain.Order;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
public class OrderResultDto {

    private List<Order> orders;
    private BigDecimal fee;
    private BigDecimal totalOrderAmount;

    @Builder
    private OrderResultDto(List<Order> orders, BigDecimal fee, BigDecimal totalOrderAmount) {
        this.orders = orders;
        this.fee = fee;
        this.totalOrderAmount = totalOrderAmount;
    }

    public static OrderResultDto toDto(List<Order> orders, BigDecimal fee, BigDecimal totalOrderAmount) {
        return OrderResultDto.builder().orders(orders).fee(fee)
                .totalOrderAmount(totalOrderAmount).build();
    }
}
