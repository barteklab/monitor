package trades;

import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Data
public class Trade {
    private String tradeReference;
    private String accountNumber;
    private String stockCode;
    private Double quantity;
    private String currency;
    private Double price;
    private String broker;
    private Timestamp receivedTimestamp;

    public Trade() {
        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
        receivedTimestamp = Timestamp.valueOf(now);
    }
}
