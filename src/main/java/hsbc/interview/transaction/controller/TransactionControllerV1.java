package hsbc.interview.transaction.controller;


import hsbc.interview.transaction.mapper.TransactionMapper;
import hsbc.interview.transaction.model.TransactionDetails;
import hsbc.interview.transaction.model.TransactionDto;
import hsbc.interview.transaction.service.TransactionService;
import hsbc.interview.transaction.validate.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
public class TransactionControllerV1 {

    private final TransactionService transactionService;

    @Autowired
    public TransactionControllerV1(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("/api/v1/transactions/{page}/{size}")
    public Page<TransactionDto> getTransactions(@PathVariable Integer page, @PathVariable Integer size) {
        Validator.validatePage(page, size);
        return transactionService.getTransactions(page, size)
                .map(TransactionMapper::transactionToTransactionDto);
    }

    @GetMapping("/api/v1/transaction/{id}")
    public TransactionDto getTransaction(@PathVariable Long id) {
        Validator.validateId(id);
        return TransactionMapper.transactionToTransactionDto(transactionService.getTransactionById(id));
    }

    @PostMapping("/api/v1/transaction")
    public TransactionDto createTransaction(@RequestBody TransactionDetails transaction) {
        Validator.validateTransaction(transaction);
        return TransactionMapper.transactionToTransactionDto(transactionService.createTransaction(transaction));
    }

    @DeleteMapping("/api/v1/transaction/{id}")
    public TransactionDto deleteTransaction(@PathVariable Long id) {
        Validator.validateId(id);
        return TransactionMapper.transactionToTransactionDto(transactionService.deleteTransaction(id));
    }

    @PutMapping("/api/v1/transaction/{id}/{amount}")
    public TransactionDto modifyTransactionAmount(@PathVariable BigDecimal amount, @PathVariable Long id) {
        Validator.validateAmount(amount);
        Validator.validateId(id);
        return TransactionMapper.transactionToTransactionDto(transactionService.modifyTransactionAmount(amount, id));
    }

}
