package product.order.cli.app.multithread;

import product.order.cli.app.domain.Product;
import product.order.cli.app.exception.SoldOutException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

public class MultiThreadUnitTest {

    @Test
    @DisplayName("멀티 스레드 요청으로 SoldOutException 테스트")
    void soldOutExceptionTest() throws InterruptedException {
        Product product = Product.of(1L, "name", "10000", 3000);

        soldOutException(product);
    }

    private void soldOutException(Product product) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(1000);

        for (int i = 0; i < 1000; i++) {
            executorService.execute(() -> {
                int quantity = (int) (Math.random() * 10) + 1;
                try {
                    product.stockQuantityUpdate(quantity);
                } catch (Exception e) {
                    assertThat(quantity).isGreaterThan(product.getStockQuantity());
                    assertThat(e).isInstanceOf(SoldOutException.class);
                }
                latch.countDown();
            });
        }
        latch.await();
    }

    @Test
    @DisplayName("마지막 쓰레드의 요청에 SoldOutException 발생")
    void lastSoldOutExceptionTest() throws InterruptedException {
        Product product = Product.of(1L, "name", "10000", 100);

        lastSoldOutException(product);
    }

    private void lastSoldOutException(Product product) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(1);

        for (int i = 0; i < 100; i++) {
            executorService.execute(() -> {
                int quantity = (int) (Math.random() * 10) + 1;
                try {
                    product.stockQuantityUpdate(quantity);
                } catch (Exception e) {
                    assertThat(quantity).isGreaterThan(product.getStockQuantity());
                    assertThat(e).isInstanceOf(SoldOutException.class);
                    latch.countDown();
                }
            });
        }
        latch.await();
    }
}
