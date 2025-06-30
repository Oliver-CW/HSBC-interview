package hsbc.interview.transaction.service;

import hsbc.interview.transaction.cache.TransactionCache;
import hsbc.interview.transaction.model.Transaction;
import hsbc.interview.transaction.model.TransactionDetails;
import hsbc.interview.transaction.repository.TransactionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Autowired
    TransactionService transactionService;
    @MockitoBean
    TransactionCache transactionCache;
    @MockitoBean
    TransactionRepository transactionRepository;

    Transaction res = Transaction.builder().id(1L).transaction(
            TransactionDetails.builder().amount(new BigDecimal("100")).account("account1").build()).build();

    @Test
    void getTransactions() {
        Mockito.when(transactionRepository.findAll(eq(PageRequest.of(0, 1)))).thenReturn(new PageImpl<>(List.of(res)));
        var result = transactionService.getTransactions(0, 1).getContent();
        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(BigDecimal.valueOf(100), result.getFirst().getTransaction().getAmount());
    }

    @Test
    void getTransactionById() {
        Mockito.when(transactionCache.getTransactionById(eq(Long.valueOf(1)))).thenReturn(res);
        Assertions.assertEquals(BigDecimal.valueOf(100), transactionService.getTransactionById(1L).getTransaction().getAmount());

        Mockito.when(transactionCache.getTransactionById(eq(Long.valueOf(1)))).thenReturn(null);
        Mockito.when(transactionRepository.findById(eq(Long.valueOf(1)))).thenReturn(Optional.of(res));
        Assertions.assertEquals(BigDecimal.valueOf(100), transactionService.getTransactionById(1L).getTransaction().getAmount());

        Mockito.when(transactionCache.getTransactionById(eq(Long.valueOf(1)))).thenReturn(null);
        Mockito.when(transactionRepository.findById(eq(Long.valueOf(1)))).thenReturn(Optional.empty());
        Assertions.assertNull(transactionService.getTransactionById(1L));
    }

    @Test
    void createTransaction() {
        Mockito.doNothing().when(transactionCache).addTransaction(res);
        Mockito.when(transactionRepository.save(any())).thenReturn(res);
        Assertions.assertEquals(BigDecimal.valueOf(100), transactionService.createTransaction(res.getTransaction()).getTransaction().getAmount());
    }

    @Test
    void deleteTransaction() {
        Mockito.when(transactionRepository.findById(eq(Long.valueOf(2)))).thenReturn(Optional.empty());
        Assertions.assertThrows(IllegalArgumentException.class, () -> transactionService.deleteTransaction(2L));

        Mockito.doNothing().when(transactionCache).deleteTransaction(eq(Long.valueOf(1)));
        Mockito.when(transactionRepository.findById(eq(Long.valueOf(1)))).thenReturn(Optional.of(res));
        Mockito.doNothing().when(transactionRepository).deleteById(eq(Long.valueOf(1)));
        Assertions.assertEquals(BigDecimal.valueOf(100), transactionService.deleteTransaction(1L).getTransaction().getAmount());
    }

    @Test
    void modifyTransactionAmount() {
        Mockito.when(transactionRepository.findById(eq(Long.valueOf(2)))).thenReturn(Optional.empty());
        Assertions.assertThrows(IllegalArgumentException.class, () -> transactionService.modifyTransactionAmount(BigDecimal.valueOf(300), 2L));

        Transaction modified = Transaction.builder().id(1L).transaction(
                TransactionDetails.builder().amount(new BigDecimal("300")).account("account1").build()).build();
        Mockito.when(transactionRepository.findById(eq(Long.valueOf(1)))).thenReturn(Optional.of(res));
        Mockito.doNothing().when(transactionCache).deleteTransaction(eq(Long.valueOf(1)));
        Mockito.doNothing().when(transactionCache).deleteTransaction(eq(Long.valueOf(1)), eq(1));
        Mockito.when(transactionRepository.save(eq(modified))).thenReturn(modified);
        Assertions.assertEquals(BigDecimal.valueOf(300), transactionService.modifyTransactionAmount(BigDecimal.valueOf(300), 1L).getTransaction().getAmount());
    }
}