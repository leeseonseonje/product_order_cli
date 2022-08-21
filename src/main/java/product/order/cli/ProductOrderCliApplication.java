package product.order.cli;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import product.order.cli.app.command.CommandBufferedReader;
import product.order.cli.app.command.CommandReader;
import product.order.cli.app.handler.CommandHandler;
import product.order.cli.app.handler.CommandHandlerImpl;
import product.order.cli.app.repository.ProductRepository;

@SpringBootApplication
public class ProductOrderCliApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(ProductOrderCliApplication.class, args);
        CommandReader commandReader = new CommandBufferedReader();
        CommandHandler commandHandler = new CommandHandlerImpl(commandReader, context);
        commandHandler.handler();
    }

    @Bean
    @Profile("application")
    public InitData initData(ProductRepository productRepository) {
        return new InitData(productRepository);
    }
}
