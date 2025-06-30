package hsbc.interview.transaction.controller;


import hsbc.interview.transaction.model.Transaction;
import hsbc.interview.transaction.model.TransactionDetails;
import hsbc.interview.transaction.model.TransactionDto;
import hsbc.interview.transaction.service.TransactionService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.client.MockMvcWebTestClient;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
class TransactionControllerV1Test {

    @MockitoBean
    TransactionService transactionService;
    @Autowired
    TransactionControllerV1 transactionControllerV1;
    @Autowired
    private MockMvc mockMvc;

    Transaction res = Transaction.builder().id(1L).transaction(
            TransactionDetails.builder().amount(new BigDecimal("100")).account("account1").build()).build();

    @Test
    void getTransaction() {
        Mockito.when(transactionService.getTransactionById(eq(Long.valueOf(1)))).thenReturn(res);
        MockMvcWebTestClient
                .bindTo(mockMvc)
                .build()
                .get()
                .uri("/api/v1/transaction/{id}", 1)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(TransactionDto.class)
                .consumeWith(response -> {
                    var tx = response.getResponseBody();
                    Assertions.assertNotNull(tx);
                    Assertions.assertEquals(1L, tx.getId());
                    Assertions.assertEquals(BigDecimal.valueOf(100), tx.getTransaction().getAmount());
                });
    }

    @Test
    void deleteTransaction() {
        Mockito.when(transactionService.deleteTransaction(eq(Long.valueOf(1)))).thenReturn(res);
        MockMvcWebTestClient
                .bindTo(mockMvc)
                .build()
                .delete()
                .uri("/api/v1/transaction/{id}", 1)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(TransactionDto.class)
                .consumeWith(response -> {
                    var tx = response.getResponseBody();
                    Assertions.assertNotNull(tx);
                    Assertions.assertEquals(1L, tx.getId());
                    Assertions.assertEquals(BigDecimal.valueOf(100), tx.getTransaction().getAmount());
                });
    }

    @Test
    void modifyTransactionAmount() {
        Transaction modified = Transaction.builder().id(1L).transaction(
                TransactionDetails.builder().amount(new BigDecimal("300")).account("account1").build()).build();
        Mockito.when(transactionService.modifyTransactionAmount(eq(BigDecimal.valueOf(300)), eq(Long.valueOf(1)))).thenReturn(modified);
        MockMvcWebTestClient
                .bindTo(mockMvc)
                .build()
                .put()
                .uri("/api/v1/transaction/{id}/{amount}", 1, 300)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(TransactionDto.class)
                .consumeWith(response -> {
                    var tx = response.getResponseBody();
                    Assertions.assertNotNull(tx);
                    Assertions.assertEquals(1L, tx.getId());
                    Assertions.assertEquals(BigDecimal.valueOf(300), tx.getTransaction().getAmount());
                });
    }

    @Test
    void createTransaction() {
        Mockito.when(transactionService.createTransaction(any())).thenReturn(res);
        MockMvcWebTestClient
                .bindTo(mockMvc)
                .build()
                .post()
                .uri("/api/v1/transaction")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("""
                        {
                          "account": "account1",
                          "type": "DEPOSIT",
                          "amount": 100,
                          "currency": "USD",
                          "date": "2025-06-30"
                        }
                        """)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(TransactionDto.class)
                .consumeWith(response -> {
                    var tx = response.getResponseBody();
                    Assertions.assertNotNull(tx);
                    Assertions.assertEquals(1L, tx.getId());
                    Assertions.assertEquals(BigDecimal.valueOf(100), tx.getTransaction().getAmount());
                });
    }

    @Test
    void getTransactions() {
        Mockito.when(transactionService.getTransactions(eq(0), eq(1))).thenReturn(new PageImpl<>(List.of(res)));
        var tx = transactionControllerV1.getTransactions(0, 1).getContent().get(0);
        Assertions.assertNotNull(tx);
        Assertions.assertEquals(1L, tx.getId());
        Assertions.assertEquals(BigDecimal.valueOf(100), tx.getTransaction().getAmount());

    }
}