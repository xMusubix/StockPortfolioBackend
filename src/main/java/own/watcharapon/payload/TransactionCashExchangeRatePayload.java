package own.watcharapon.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionCashExchangeRatePayload {
    private Double exRate;
    private Double thb;
}
