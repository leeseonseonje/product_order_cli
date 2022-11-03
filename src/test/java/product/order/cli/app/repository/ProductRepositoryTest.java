package product.order.cli.app.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import product.order.cli.app.domain.Product;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    ProductRepository productRepository;

    @BeforeEach
    void init() {
        for (long i = 0L; i < 20L; i++) {
            int stockQuantity = (int) (Math.random() * 100);
            Product product = Product.of(i, "product" + i, "10000", stockQuantity);
            productRepository.save(product);
        }
    }

    @Test
    void findByNumbers() {
        Set<Long> ids = Set.of(1L, 2L, 3L, 4L, 5L);

        List<Product> products = productRepository.findByNumbers(ids);

        assertThat(products.size()).isEqualTo(5);
    }

}