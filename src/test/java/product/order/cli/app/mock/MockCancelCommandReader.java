package product.order.cli.app.mock;

import product.order.cli.app.command.CommandReader;

public class MockCancelCommandReader implements CommandReader {

    @Override
    public String read() {
        return "q";
    }
}
