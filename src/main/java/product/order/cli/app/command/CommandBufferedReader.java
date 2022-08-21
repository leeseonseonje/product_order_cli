package product.order.cli.app.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Component
@RequiredArgsConstructor
public class CommandBufferedReader implements CommandReader {

    @Override
    public String read() {

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            String readLine = reader.readLine();

            if (readLine.equals("")) {
                throw new IllegalStateException("명령어를 다시 입력해 주세요.");
            } else {
                return readLine;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
