package product.order.cli.app.multithread;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import product.order.cli.app.domain.Product;
import product.order.cli.app.exception.SoldOutException;
import product.order.cli.app.repository.ProductRepository;
import product.order.cli.app.service.OrderService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
public class MultiThreadIntegrationTest {

    @Autowired
    OrderService orderService;

    @TestConfiguration
    static class Config {

        @Bean
        public InitData initData(ProductRepository productRepository) {
            return new InitData(productRepository);
        }
    }

    @RequiredArgsConstructor
    static class InitData {

        private final ProductRepository productRepository;

        @EventListener(ApplicationReadyEvent.class)
        public void initData() {
            Product product1 = Product.of(1L, "product1", "10000", 99);
            productRepository.save(product1);

            Product product2 = Product.of(2L, "product2", "10000", 50);
            productRepository.save(product2);
        }
    }


    @Test
    @DisplayName("select for update 문으로 잠금 획득, 재고가 0일 경우 SoleOutException 발생")
    void updateMultiThreadTest() throws InterruptedException {
        Map<Long, Integer> orderTransactions = new HashMap<>();
        orderTransactions.put(1L, 1);

        updateMultiThread(orderTransactions);
    }

    private void updateMultiThread(Map<Long, Integer> orderTransactions) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(1);

        for (int i = 0; i < 100; i++) {
            executorService.execute(() -> {
                try {
                    orderService.order(orderTransactions);
                } catch (SoldOutException e) {
                    assertThat(e).isInstanceOf(SoldOutException.class);
                    latch.countDown();
                }
            });
        }
        latch.await();
    }

    @Test
    @DisplayName("비관적 락으로 인하여 각 스레드의 트랜잭션이 순차적으로 커밋되어야 한다.")
    void selectForUpdateTest() throws InterruptedException {
        Map<Long, Integer> orderTransactions = new HashMap<>();
        orderTransactions.put(2L, 1);

        selectForUpdate(orderTransactions);
    }
    private void selectForUpdate(Map<Long, Integer> orderTransactions) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        CountDownLatch latch = new CountDownLatch(50);
        AtomicInteger count = new AtomicInteger();

        for (int i = 0; i < 50; i++) {
            executorService.execute(() -> {
                    orderService.order(orderTransactions);
                    latch.countDown();
                    int i1 = count.addAndGet(1);
                    assertThat(latch.getCount() + count.get()).isEqualTo(50);
            });
        }
        latch.await();
    }
}
