package hsbc.interview.demoproject.model;


import hsbc.interview.demoproject.constant.Currency;
import hsbc.interview.demoproject.constant.TransactionType;
import jakarta.persistence.*;
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
