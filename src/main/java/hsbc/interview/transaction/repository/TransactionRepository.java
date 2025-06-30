package hsbc.interview.transaction.repository;


import hsbc.interview.transaction.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TransactionRepository extends JpaRepository<Transaction, Long> {

}
