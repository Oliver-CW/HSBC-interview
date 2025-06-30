package hsbc.interview.transaction.cache;


import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import hsbc.interview.transaction.config.TransactionCacheConfig;
import hsbc.interview.transaction.model.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


@Component
@Slf4j
public class TransactionCache {

    private final Cache<Long, Transaction> transactionCache;
    private final ScheduledExecutorService scheduledExecutorService; // for delayed double deletions

    @Autowired
    public TransactionCache(TransactionCacheConfig transactionCacheConfig) {
        this.transactionCache = Caffeine
                .newBuilder()
                .expireAfterAccess(Duration.ofSeconds(transactionCacheConfig.getExpireSecondsAfterAccess()))
                .maximumSize(transactionCacheConfig.getMaxSize())
                .build();
        this.scheduledExecutorService = new ScheduledThreadPoolExecutor(Runtime.getRuntime().availableProcessors()
                , new ThreadPoolExecutor.CallerRunsPolicy());
        log.info("TransactionCache initialized,maxSize:{},expireSecondsAfterAccess:{}",
                transactionCacheConfig.getMaxSize(), transactionCacheConfig.getExpireSecondsAfterAccess());
    }

    public Transaction getTransactionById(Long id) {
        var trans = transactionCache.getIfPresent(id);
        if (Objects.nonNull(trans)) {
            log.info("got transaction from cache {}", trans);
        }
        return trans;
    }

    public void deleteTransaction(Long id) {
        transactionCache.invalidate(id);
        log.info("deleted transaction from cache by id {}", id);
    }

    public void deleteTransaction(Long id, int secondsDelayed) {
        scheduledExecutorService.schedule(() -> {
            log.info("deleting transaction from cache by id {} after {} seconds delayed", id, secondsDelayed);
            deleteTransaction(id);
        }, secondsDelayed, TimeUnit.SECONDS);
    }

    public void addTransaction(Transaction transaction) {
        this.transactionCache.put(transaction.getId(), transaction);
        log.info("added transaction to cache {}", transaction);
    }
}
