package product.order.cli.app.mock;

import product.order.cli.app.command.Command;
import product.order.cli.app.command.CommandReader;
import product.order.cli.app.handler.CommandHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;

@RequiredArgsConstructor
public class MockHandler implements CommandHandler {

    private final CommandReader commandReader;
    private final ApplicationContext applicationContext;

    @Override
    public void handler() {
        System.out.println("Mock Handler 실행");

        String command = commandReader.read();

        if (isQuit(command)) {
            System.out.println("고객님의 주문 감사합니다.");
            return;
        }

        try {
            Command.execute(command.substring(0, 1), applicationContext);
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }

    private boolean isQuit(String command) {
        return command.equals("q") || command.equals("quit");
    }
}
