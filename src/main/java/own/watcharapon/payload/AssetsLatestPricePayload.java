package own.watcharapon.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssetsLatestPricePayload {
    private String marketSymbol;
    private Double holdingValue;
    private Double lastPrice;
    private Double lastPriceChange;
    private Double lastPriceChangePercentage;
}
