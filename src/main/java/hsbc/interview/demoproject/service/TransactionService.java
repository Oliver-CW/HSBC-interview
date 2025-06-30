package hsbc.interview.demoproject.service;


import hsbc.interview.demoproject.model.*;
import hsbc.interview.demoproject.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository){
        this.transactionRepository = transactionRepository;
    }

    public Page<Transaction> getTransactions(Integer page, Integer size){
        return transactionRepository.findAll(PageRequest.of(page,size));
    }

    public Transaction createTransaction(TransactionDetails transactionDetails){
        var transaction = new Transaction();
        transaction.setTransaction(transactionDetails);
        return transactionRepository.save(transaction);
    }

    public Transaction deleteTransaction(Long id){
        var transaction = transactionRepository
                .findById(id)
                .orElseThrow(()->new IllegalArgumentException("Transaction %s to be deleted does not exist!".formatted(id)));
        transactionRepository.deleteById(id);
        return transaction;
    }

    public Transaction modifyTransactionAmount(BigDecimal amount, Long id){
        var transaction = transactionRepository
                .findById(id)
                .orElseThrow(()->new IllegalArgumentException("Transaction %s to be updated does not exist!".formatted(id)));
        transaction.getTransaction().setAmount(amount);
        return transactionRepository.save(transaction);
    }
}
