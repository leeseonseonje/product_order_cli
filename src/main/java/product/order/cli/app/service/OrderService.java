package product.order.cli.app.service;

import product.order.cli.app.domain.Delivery;
import product.order.cli.app.domain.Order;
import product.order.cli.app.domain.Product;
import product.order.cli.app.dto.OrderResultDto;
import product.order.cli.app.formatter.OrderTransactions;
import product.order.cli.app.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final ProductRepository productRepository;

    public OrderResultDto order(OrderTransactions orderTransactions) {

        List<Product> products = productRepository.findByNumbers(orderTransactions.productNumbers());
        productIsEmpty(products);

        return orderResult(new ArrayList<>(orderTransactions.orderQuantities()), products);
    }

    private void productIsEmpty(List<Product> products) {
        if (products.isEmpty()) {
            throw new IllegalStateException("존재하지 않는 상품입니다.");
        }
    }

    private OrderResultDto orderResult(List<Integer> orderQuantities, List<Product> products) {

        List<Order> orders = orderInfo(orderQuantities, products);

        BigDecimal totalOrderAmount = getTotalOrderAmount(orders);

        BigDecimal deliveryFee = deliveryFeeSetup(totalOrderAmount);

        return OrderResultDto.toDto(orders, deliveryFee, totalOrderAmount);
    }

    private List<Order> orderInfo(List<Integer> orderQuantities, List<Product> products) {
        List<Order> orders = new ArrayList<>();

        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            int quantity = orderQuantities.get(i);

            product.stockQuantityUpdate(quantity);
            orders.add(Order.of(product, quantity));
        }
        return orders;
    }

    private BigDecimal deliveryFeeSetup(BigDecimal totalOrderAmount) {
        Delivery delivery = Delivery.of(2500);
        return delivery.deliveryFeeSetup(totalOrderAmount);
    }

    private BigDecimal getTotalOrderAmount(List<Order> orders) {
        BigDecimal totalOrderAmount = new BigDecimal("0");

        for (Order order : orders) {
            totalOrderAmount = totalOrderAmount.add(order.getOrderAmount());
        }
        return totalOrderAmount;
    }
}
