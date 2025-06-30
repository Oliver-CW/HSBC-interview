package hsbc.interview.demoproject.model;


import hsbc.interview.demoproject.constant.Currency;
import hsbc.interview.demoproject.constant.TransactionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Table(name = "Transaction")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @CreatedDate
    @Column(name = "createAt")
    private Date createAt;

    @LastModifiedDate
    @Column(name = "updatedAt")
    private Date updatedAt;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "account", column = @Column(name = "account")),
            @AttributeOverride(name = "type", column = @Column(name = "type")),
            @AttributeOverride(name = "amount", column = @Column(name = "amount")),
            @AttributeOverride(name = "currency", column = @Column(name = "currency")),
            @AttributeOverride(name = "date", column = @Column(name = "date"))
    })
    private TransactionDetails transaction;
}
