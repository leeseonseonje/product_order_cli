package product.order.cli.app.formatter;

import product.order.cli.app.command.CommandReader;
import product.order.cli.app.domain.Order;
import product.order.cli.app.domain.Product;
import product.order.cli.app.dto.OrderResultDto;
import product.order.cli.app.repository.ProductRepository;
import product.order.cli.app.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Integer.*;
import static java.lang.Long.*;
import static java.math.RoundingMode.HALF_EVEN;
import static org.springframework.data.domain.Sort.*;
import static org.springframework.data.domain.Sort.Direction.*;

@Component
@RequiredArgsConstructor
public class OrderFormatter implements CommandFormatter {

    private final ProductRepository productRepository;
    private final CommandReader commandReader;
    private final OrderService orderService;

    @Override
    public void formatter() {
        List<Product> products = productRepository.findAll(by(DESC, "number"));

        productsPrint(products);

        Map<Long, Integer> map = new HashMap<>();

        while (true) {

            String numberInput = input("\n상품번호 : ");

            if (endOrder(numberInput, map)) {
                break;
            }

            String quantityInput = input("\n수량 : ");

            if (endOrder(quantityInput, map)) {
                break;
            }

            Long productNumber = parseLong(numberInput);
            Integer quantity = parseInt(quantityInput);

            createOrderTransactions(map, productNumber, quantity);
        }
    }

    private void productsPrint(List<Product> products) {
        System.out.printf("%-8s %-50s %-15s %s %n", "상품번호", "상품명", "판매가격", "재고수");
        products.forEach(p -> System.out.printf("%-10s %-50s %-15s %s %n",
                p.getNumber(),
                p.getName(),
                p.getPrice().setScale(0, HALF_EVEN),
                p.getStockQuantity()));
    }

    private String input(String message) {
        System.out.print(message);
        return commandReader.read();
    }

    private void createOrderTransactions(Map<Long, Integer> map, Long productNumber, Integer quantity) {
        Integer currentQuantity = map.getOrDefault(productNumber, 0);
        map.put(productNumber, currentQuantity + quantity);
    }

    private boolean endOrder(String command, Map<Long, Integer>orderTransactions) {
        if (command.equals(" ")) {
            orderResult(orderTransactions);
            return true;
        }
        return false;
    }

    private void orderResult(Map<Long, Integer> orderTransactions) {

        OrderResultDto result = orderService.order(orderTransactions);

        orderTransactionPrint(result);

        BigDecimal totalOrderAmount = result.getTotalOrderAmount();

        totalOrderAmountPrint(totalOrderAmount);

        BigDecimal deliveryFee = result.getFee();
        deliveryFeePrint(deliveryFee);

        paymentPrint(totalOrderAmount, deliveryFee);
    }

    private void orderTransactionPrint(OrderResultDto result) {
        System.out.println("\n주문 내역:");
        System.out.println("----------------------------------------");
        for (Order order : result.getOrders()) {
            System.out.println(order);
        }
    }

    private void totalOrderAmountPrint(BigDecimal totalOrderAmount) {
        System.out.println("----------------------------------------");
        System.out.println("주문금액: " + moneyCommaFormat(totalOrderAmount));
    }

    private void deliveryFeePrint(BigDecimal deliveryFee) {
        if (deliveryFee.compareTo(BigDecimal.ZERO) != 0) {
            System.out.println("배송비: " + moneyCommaFormat(deliveryFee));
        }
        System.out.println("----------------------------------------");
    }

    private void paymentPrint(BigDecimal totalOrderAmount, BigDecimal deliveryFee) {
        System.out.println("지불금액: " + moneyCommaFormat(payment(totalOrderAmount, deliveryFee)));
        System.out.println("----------------------------------------");
    }

    private BigDecimal payment(BigDecimal totalOrderAmount, BigDecimal deliveryFee) {
        return totalOrderAmount.add(deliveryFee);
    }

    private String moneyCommaFormat(BigDecimal money) {
        return new DecimalFormat("###,###원").format(money);
    }
}
