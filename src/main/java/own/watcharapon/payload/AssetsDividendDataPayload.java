package own.watcharapon.payload;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AssetsDividendDataPayload {
    private String marketSymbol;
    private LocalDate exDate;
    private Double dividendYield;
    private Double dividendAmount;
    private Double payoutRatio;
}
