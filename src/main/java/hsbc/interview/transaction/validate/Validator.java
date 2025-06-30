package hsbc.interview.transaction.validate;


import hsbc.interview.transaction.model.TransactionDetails;
import io.micrometer.common.util.StringUtils;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;

@UtilityClass
public class Validator {

    public static void validateAmount(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.valueOf(0)) <= 0) {
            throw new IllegalArgumentException("transaction amount can not be 0 or negative value");
        }
    }

    public static void validateId(Long id) {
        if (id <= 0) {
            throw new IllegalArgumentException("transaction id can not be 0 or negative value");
        }
    }

    public static void validatePage(Integer page, Integer size) {
        if (page < 0 || size <= 0) {
            throw new IllegalArgumentException("page number and page size can not be negative value");
        }
    }

    public static void validateTransaction(TransactionDetails transaction) {
        validateAmount(transaction.getAmount());
        if (StringUtils.isEmpty(transaction.getAccount())) {
            throw new IllegalArgumentException("account can not null or empty value");
        }
    }

}
