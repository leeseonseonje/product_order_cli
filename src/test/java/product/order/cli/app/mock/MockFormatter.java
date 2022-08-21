package product.order.cli.app.mock;

import product.order.cli.app.formatter.CommandFormatter;

public class MockFormatter implements CommandFormatter {

    @Override
    public void formatter() {
        System.out.println("Mock Formatter");
    }
}
