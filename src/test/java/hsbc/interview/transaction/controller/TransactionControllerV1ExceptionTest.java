package hsbc.interview.transaction.controller;


import hsbc.interview.transaction.exception.GlobalExceptionHandler;
import hsbc.interview.transaction.exception.TransactionException;
import hsbc.interview.transaction.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.client.MockMvcWebTestClient;

import static org.mockito.ArgumentMatchers.eq;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class TransactionControllerV1ExceptionTest {

    @MockitoBean
    TransactionService transactionService;
    @Autowired
    TransactionControllerV1 transactionControllerV1;
    @Autowired
    GlobalExceptionHandler globalExceptionHandler;

    @Test
    void getTransactionIllegalArgumentException() {
        MockMvcWebTestClient
                .bindToController(transactionControllerV1)
                .controllerAdvice(globalExceptionHandler)
                .build()
                .get()
                .uri("/api/v1/transaction/{id}", -1)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .is4xxClientError();
    }

    @Test
    void getTransactionTransactionException() {
        Mockito.when(transactionService.deleteTransaction(eq(Long.valueOf(1)))).thenThrow(new TransactionException());
        MockMvcWebTestClient
                .bindToController(transactionControllerV1)
                .controllerAdvice(globalExceptionHandler)
                .build()
                .get()
                .uri("/api/v1/transaction/{id}", 1)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .is5xxServerError();
    }

    @Test
    void getTransactionUnknownException() {
        Mockito.when(transactionService.deleteTransaction(eq(Long.valueOf(1)))).thenThrow(new RuntimeException());
        MockMvcWebTestClient
                .bindToController(transactionControllerV1)
                .controllerAdvice(globalExceptionHandler)
                .build()
                .get()
                .uri("/api/v1/transaction/{id}", 1)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .is5xxServerError();
    }

}