package product.order.cli.app.command;

import product.order.cli.app.formatter.CommandFormatter;
import product.order.cli.app.mock.MockFormatter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;

import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class CommandTest {

    @Mock
    ApplicationContext applicationContext;

    @Test
    void executeTest() {

        given(applicationContext.getBean("orderFormatter", CommandFormatter.class))
                .willReturn(new MockFormatter());

        Command.execute("o", applicationContext);
        verify(applicationContext, times(1))
                .getBean("orderFormatter", CommandFormatter.class);
    }
}