package product.order.cli.app.command;

import product.order.cli.app.formatter.CommandFormatter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.context.ApplicationContext;

import java.util.Map;
import java.util.function.Function;

import static java.util.Arrays.*;
import static java.util.stream.Collectors.*;

@Getter
@AllArgsConstructor
public enum Command {

    ORDER("o", "order",
            context -> context.getBean("orderFormatter", CommandFormatter.class).formatter());

    private String command;

    private String fullCommand;

    private CommandExecute commandExecute;

    private static Map<String, Command> commands = initCommand();

    private static Map<String, Command> initCommand() {
        commands = stream(values()).collect(toMap(e -> e.command, Function.identity()));
        commands = stream(values()).collect(toMap(e -> e.fullCommand, Function.identity()));

        return commands;
    }

    private static Command getCommand(String input) {
        String key = commands.keySet().stream().filter(e -> e.contains(input)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("명령을 다시 입력해 주세요."));
        return commands.get(key);
    }

    public static void execute(String input, ApplicationContext applicationContext) {
        Command command = getCommand(input);
        command.commandExecute.execute(applicationContext);
    }
}
