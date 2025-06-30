package hsbc.interview.transaction;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TransactionApplicationTests {

    @Test
    void contextLoads() {
        Assertions.assertDoesNotThrow(() -> TransactionApplication.main(new String[]{}));
    }

}
