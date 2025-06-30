package hsbc.interview.transaction.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "cache")
@Data
public class TransactionCacheConfig {
    private Integer expireSecondsAfterAccess;
    private Integer maxSize;
}
