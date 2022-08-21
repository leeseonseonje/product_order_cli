package product.order.cli;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import product.order.cli.app.domain.Product;
import product.order.cli.app.repository.ProductRepository;

@RequiredArgsConstructor
public class InitData {

    private final ProductRepository productRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void initData() {
        for (long i = 0L; i < 20L; i++) {
            int stockQuantity = (int) (Math.random() * 100);
            Product product = Product.of(i, "product" + i, "10000", stockQuantity);
            productRepository.save(product);
        }
    }
}
