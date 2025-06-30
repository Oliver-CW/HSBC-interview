package hsbc.interview.demoproject.validate;


import ch.qos.logback.core.util.StringUtil;
import hsbc.interview.demoproject.model.Transaction;
import hsbc.interview.demoproject.model.TransactionDetails;
import io.micrometer.common.util.StringUtils;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;

@UtilityClass
public class Validator {

    public static void validate(BigDecimal amount){
        if(amount.compareTo(BigDecimal.valueOf(0)) <= 0){
            throw new IllegalArgumentException("Transaction amount can not be 0 or negative value");
        }
    }

    public static void validate(Long id){
        if(id <= 0){
            throw new IllegalArgumentException("Transaction id can not be 0 or negative value");
        }
    }

    public static void validate(Integer page, Integer size){
        if(page < 0 || size <= 0){
            throw new IllegalArgumentException("page number and page size can not be negative value");
        }
    }

    public static void validate(TransactionDetails transaction){
        validate(transaction.getAmount());
        if(StringUtils.isEmpty(transaction.getAccount())){
            throw new IllegalArgumentException("account can not null or empty value");
        }
    }

}
