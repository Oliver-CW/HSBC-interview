package hsbc.interview.transaction.cache;

import hsbc.interview.transaction.model.Transaction;
import hsbc.interview.transaction.model.TransactionDetails;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static java.lang.Thread.sleep;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class TransactionCacheTest {
    @Autowired
    TransactionCache transactionCache;

    Transaction res = Transaction.builder().id(1L).transaction(
            TransactionDetails.builder().amount(new BigDecimal("100")).account("account1").build()).build();

    @Test
    void getTransactionById() {
        transactionCache.addTransaction(res);
        var res = transactionCache.getTransactionById(1L);
        Assertions.assertNotNull(res);
        Assertions.assertEquals(BigDecimal.valueOf(100), res.getTransaction().getAmount());
    }

    @Test
    void deleteTransaction() {
        transactionCache.addTransaction(res);
        Assertions.assertNotNull(transactionCache.getTransactionById(1L));
        Assertions.assertDoesNotThrow(() -> transactionCache.deleteTransaction(1L));
        Assertions.assertNull(transactionCache.getTransactionById(1L));
    }

    @Test
    void DeleteTransactionDelayed() throws InterruptedException {
        transactionCache.addTransaction(res);
        Assertions.assertNotNull(transactionCache.getTransactionById(1L));
        Assertions.assertDoesNotThrow(() -> transactionCache.deleteTransaction(1L, 3));
        Assertions.assertNotNull(transactionCache.getTransactionById(1L));
        sleep(3500);
        Assertions.assertNull(transactionCache.getTransactionById(1L));
    }

    @Test
    void addTransaction() {
        transactionCache.deleteTransaction(1L);
        Assertions.assertNull(transactionCache.getTransactionById(1L));
        Assertions.assertDoesNotThrow(() -> transactionCache.addTransaction(res));
        Assertions.assertNotNull(transactionCache.getTransactionById(1L));
    }
}