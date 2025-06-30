package hsbc.interview.demoproject.controller;


import hsbc.interview.demoproject.mapper.TransactionMapper;
import hsbc.interview.demoproject.model.TransactionDetails;
import hsbc.interview.demoproject.model.TransactionDto;
import hsbc.interview.demoproject.service.TransactionService;
import hsbc.interview.demoproject.validate.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
public class TransactionControllerV1 {

    private final TransactionService transactionService;

    @Autowired
    public TransactionControllerV1(TransactionService transactionService){
        this.transactionService = transactionService;
    }

    @GetMapping("/api/v1/transactions/{page}/{size}")
    public Page<TransactionDto> getTransactions(@PathVariable Integer page, @PathVariable Integer size){
        Validator.validate(page,size);
        return transactionService.getTransactions(page,size)
                .map(TransactionMapper::transactionToTransactionDto);
    }

    @PostMapping("/api/v1/transactions")
    public TransactionDto createTransaction(@RequestBody TransactionDetails transaction){
        Validator.validate(transaction);
        return TransactionMapper.transactionToTransactionDto(transactionService.createTransaction(transaction));
    }

    @DeleteMapping("/api/v1/transactions/{id}")
    public TransactionDto deleteTransaction(@PathVariable Long id){
        Validator.validate(id);
        return TransactionMapper.transactionToTransactionDto(transactionService.deleteTransaction(id));
    }

    @PutMapping("/api/v1/transactions/{id}/{amount}")
    public TransactionDto modifyTransactionAmount(@PathVariable BigDecimal amount, @PathVariable Long id){
        Validator.validate(amount);
        Validator.validate(id);
        return TransactionMapper.transactionToTransactionDto(transactionService.modifyTransactionAmount(amount, id));
    }

}
