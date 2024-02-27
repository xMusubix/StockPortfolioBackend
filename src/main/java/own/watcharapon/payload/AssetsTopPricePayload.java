package own.watcharapon.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssetsTopPricePayload {
    private String symbol;
    private Double lastPriceChange;
    private Double lastPriceChangePercentage;
}
