package own.watcharapon.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssetsPayload {
    private UUID id;
    private String marketSymbol;
    private String symbol;
    private Double score;
    private Double line;
    private String state;
    private String sectorName;
    private String industryName;
    private Double totalShare;
    private Double costValue;
    private Double holdingValue;
    private Double averagePrice;
    private Double costGain;
    private Double costGainValue;
    private Double dailyCostGain;
    private Double target;
    private Double actual;
    private Double different;
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
    private Double lastPriceChange;
    private Double lastPriceChangePercentage;
    private LocalDate exDate;
    private Double dividendYield;
    private Double dividendAmount;
    private Double payoutRatio;
    private String note;

    public AssetsPayload(UUID id, String marketSymbol, String symbol, Double score, Double line, String state, String sectorName, String industryName, Double totalShare, Double costValue, Double holdingValue, Double target, Double ytdPrice, Double wkPrice1, Double moPrice1, Double moPrice3, Double moPrice6, Double yearPrice1, Double yearHigh, Double yearLow, Double percentYear, Double lastPrice, Double lastPriceChange, Double lastPriceChangePercentage, LocalDate exDate, Double dividendYield, Double dividendAmount, Double payoutRatio, String note) {
        this.id = id;
        this.marketSymbol = marketSymbol;
        this.symbol = symbol;
        this.score = score;
        this.line = line;
        this.state = state;
        this.sectorName = sectorName;
        this.industryName = industryName;
        this.totalShare = totalShare;
        this.costValue = costValue;
        this.holdingValue = holdingValue;
        this.target = target;
        this.ytdPrice = ytdPrice;
        this.wkPrice1 = wkPrice1;
        this.moPrice1 = moPrice1;
        this.moPrice3 = moPrice3;
        this.moPrice6 = moPrice6;
        this.yearPrice1 = yearPrice1;
        this.yearHigh = yearHigh;
        this.yearLow = yearLow;
        this.percentYear = percentYear;
        this.lastPrice = lastPrice;
        this.lastPriceChange = lastPriceChange;
        this.lastPriceChangePercentage = lastPriceChangePercentage;
        this.exDate = exDate;
        this.dividendYield = dividendYield;
        this.dividendAmount = dividendAmount;
        this.payoutRatio = payoutRatio;
        this.note = note;
    }
}
