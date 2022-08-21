package product.order.cli.app.command;

import org.springframework.context.ApplicationContext;

public interface CommandExecute {

    void execute(ApplicationContext applicationContext);
}
