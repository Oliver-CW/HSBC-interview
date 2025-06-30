package hsbc.interview.transaction.mapper;


import hsbc.interview.transaction.model.Transaction;
import hsbc.interview.transaction.model.TransactionDto;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TransactionMapper {

    public TransactionDto transactionToTransactionDto(Transaction transaction) {
        return TransactionDto
                .builder()
                .id(transaction.getId())
                .transaction(transaction.getTransaction())
                .build();
    }


}
