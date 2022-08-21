package product.order.cli.app.service;

import org.junit.jupiter.api.BeforeEach;
import product.order.cli.app.domain.Product;
import product.order.cli.app.dto.OrderResultDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import product.order.cli.app.repository.ProductRepository;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

@Transactional
@SpringBootTest
class OrderServiceTest {

    @Autowired
    OrderService orderService;

    @Autowired
    ProductRepository productRepository;

    @BeforeEach
    void initData() {
        Product product1 = Product.of(1L, "product1", "10000", 100);
        productRepository.save(product1);

        Product product2 = Product.of(2L, "product2", "20000", 100);
        productRepository.save(product2);
    }

    @Test
    void orderTest() {

        Map<Long, Integer> orderTransactions = new HashMap<>();
        orderTransactions.put(1L, 1);
        orderTransactions.put(2L, 3);

        OrderResultDto orderResultDto = orderService.order(orderTransactions);

        assertThat(orderResultDto.getFee()).isEqualTo(new BigDecimal(0));
        assertThat(orderResultDto.getTotalOrderAmount()).isEqualTo(new BigDecimal("70000"));
    }
}