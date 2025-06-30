package hsbc.interview.transaction.validate;

import hsbc.interview.transaction.model.TransactionDetails;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;


class ValidatorTest {

    @Test
    void validateAmount() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> Validator.validateAmount(BigDecimal.ZERO));
        Assertions.assertThrows(IllegalArgumentException.class, () -> Validator.validateAmount(BigDecimal.valueOf(-1)));
        Assertions.assertDoesNotThrow(() -> Validator.validateAmount(BigDecimal.valueOf(1)));
    }

    @Test
    void validateId() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> Validator.validateId(-1L));
        Assertions.assertDoesNotThrow(() -> Validator.validateId(1L));
    }

    @Test
    void validatePage() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> Validator.validatePage(-1, 0));
        Assertions.assertThrows(IllegalArgumentException.class, () -> Validator.validatePage(-1, 1));
        Assertions.assertThrows(IllegalArgumentException.class, () -> Validator.validatePage(0, -1));
        Assertions.assertDoesNotThrow(() -> Validator.validatePage(0, 1));
    }

    @Test
    void validateTransaction() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> Validator.validateTransaction(
                TransactionDetails.builder().amount(BigDecimal.ONE).account("").build()));
        Assertions.assertDoesNotThrow(() -> Validator.validateTransaction(
                TransactionDetails.builder().amount(BigDecimal.ONE).account("account1").build()));
    }
}