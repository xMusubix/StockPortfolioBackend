package own.watcharapon.payload;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssetsHistoryPricePayload {
    private String marketSymbol;
    private Double ytdPrice;
    private Double wkPrice1;
    private Double moPrice1;
    private Double moPrice3;
    private Double moPrice6;
    private Double yearPrice1;
    private Double yearHigh;
    private Double yearLow;
    private Double percentYear;
    private Double lastPrice;
}
