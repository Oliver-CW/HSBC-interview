package hsbc.interview.demoproject.mapper;


import hsbc.interview.demoproject.model.Transaction;
import hsbc.interview.demoproject.model.TransactionDto;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TransactionMapper {

    public TransactionDto transactionToTransactionDto(Transaction transaction){
        return TransactionDto
                .builder()
                .id(transaction.getId())
                .transaction(transaction.getTransaction())
                .build();
    }


}
