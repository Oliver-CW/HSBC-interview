package hsbc.interview.transaction.service;


import hsbc.interview.transaction.cache.TransactionCache;
import hsbc.interview.transaction.model.Transaction;
import hsbc.interview.transaction.model.TransactionDetails;
import hsbc.interview.transaction.repository.TransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Objects;

@Service
@Slf4j
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionCache transactionCache;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository, TransactionCache transactionCache) {
        this.transactionRepository = transactionRepository;
        this.transactionCache = transactionCache;
    }

    public Page<Transaction> getTransactions(Integer page, Integer size) {
        var res = transactionRepository.findAll(PageRequest.of(page, size));
        log.info("got transactions from db by page {},size {} - transactions {}", page, size, res.getContent());
        return transactionRepository.findAll(PageRequest.of(page, size));
    }

    public Transaction getTransactionById(Long id) {
        var transaction = transactionCache.getTransactionById(id);
        if (Objects.isNull(transaction)) {
            transaction = transactionRepository.findById(id).orElse(null);
            if (Objects.nonNull(transaction)) {
                log.info("got transaction from db {}", transaction);
                transactionCache.addTransaction(transaction);
            }
        }
        return transaction;
    }

    public Transaction createTransaction(TransactionDetails transactionDetails) {
        var transaction = new Transaction();
        transaction.setTransaction(transactionDetails);
        transaction = transactionRepository.save(transaction);
        transactionCache.addTransaction(transaction);
        log.info("created transaction and save it to db and cache {}", transaction);
        return transaction;
    }

    public Transaction deleteTransaction(Long id) {
        var transaction = transactionRepository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("transaction %s to be deleted does not exist!".formatted(id)));
        transactionRepository.deleteById(id);
        transactionCache.deleteTransaction(id);
        log.info("deleted transaction from db and cache {}", transaction);
        return transaction;
    }

    public Transaction modifyTransactionAmount(BigDecimal amount, Long id) {
        var transaction = transactionRepository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("transaction %s to be updated does not exist!".formatted(id)));
        transaction.getTransaction().setAmount(amount);
        transactionCache.deleteTransaction(id);
        transaction = transactionRepository.save(transaction);
        transactionCache.deleteTransaction(id, 1); //double deletions when updating
        log.info("modified transaction {}", transaction);
        return transaction;
    }
}
