package product.order.cli;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
public class ProductOrderCliApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductOrderCliApplication.class, args);
    }
}
