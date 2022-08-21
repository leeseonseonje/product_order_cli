package product.order.cli.app.handler;

import product.order.cli.app.command.Command;
import product.order.cli.app.command.CommandReader;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommandHandlerImpl implements CommandHandler {

    private final CommandReader commandReader;
    private final ApplicationContext applicationContext;

    public void handler() {

        while (true) {
            System.out.print("입력(o[order]: 주문, q[quit]: 종료 : ");

            try {
                String command = commandReader.read();

                if (isQuit(command)) {
                    System.out.println("고객님의 주문 감사합니다.");
                    break;
                }
                Command.execute(command, applicationContext);
            } catch (RuntimeException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private boolean isQuit(String command) {
        return command.equals("q") || command.equals("quit");
    }
}

