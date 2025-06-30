package hsbc.interview.transaction.model;


import hsbc.interview.transaction.constant.Currency;
import hsbc.interview.transaction.constant.TransactionType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionDetails {

    private String account;
    @Enumerated(EnumType.STRING)
    private TransactionType type;
    private BigDecimal amount;
    @Enumerated(EnumType.STRING)
    private Currency currency;
    private Date date;
}
