package product.order.cli.app.handler;

import product.order.cli.app.formatter.CommandFormatter;
import product.order.cli.app.mock.MockCancelCommandReader;
import product.order.cli.app.mock.MockFormatter;
import product.order.cli.app.mock.MockHandler;
import product.order.cli.app.mock.MockOrderCommandReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CommandHandlerTest {

    @Mock
    ApplicationContext applicationContext;

    @BeforeEach
    void init() {
        given(applicationContext.getBean("orderFormatter", CommandFormatter.class))
                .willReturn(new MockFormatter());
    }

    @Test
    @DisplayName("CommandHandler 실행, 'order' 명령어를 입력 받았을때, 포매터가 실행되면 성공")
    void orderHandlerTest() {
        CommandHandler commandHandler = new MockHandler(new MockOrderCommandReader(), applicationContext);
        commandHandler.handler();

        verify(applicationContext, times(1))
                .getBean("orderFormatter", CommandFormatter.class);
    }

    @Test
    @DisplayName("'quit' 명령어를 입력 받았을때, 프로그램이 종료된다(포매터가 실행되지 않으면 성공)")
    void quitHandlerTest() {

        lenient().when(applicationContext.getBean("orderFormatter", CommandFormatter.class)).thenReturn(new MockFormatter());
        CommandHandler commandHandler = new MockHandler(new MockCancelCommandReader(), null);

        verify(applicationContext, times(0))
                .getBean("orderFormatter", CommandFormatter.class);
    }

}