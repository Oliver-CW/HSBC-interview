package hsbc.interview.demoproject.repository;


import hsbc.interview.demoproject.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface TransactionRepository extends JpaRepository<Transaction,Long> {

}
