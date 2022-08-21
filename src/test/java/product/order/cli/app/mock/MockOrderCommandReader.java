package product.order.cli.app.mock;

import product.order.cli.app.command.CommandReader;

public class MockOrderCommandReader implements CommandReader {

    @Override
    public String read() {
        return "o";
    }
}
