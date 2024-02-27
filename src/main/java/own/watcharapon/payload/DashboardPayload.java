package own.watcharapon.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardPayload {
    private Double totalHoldingUSD;
    private Double totalHoldingTHB;
    private Double totalCostTHB;
    private Double totalCostUSD;
    private Double changeUSD;
    private Double changeTHB;
    private Double changePercentageUSD;
    private Double changePercentageTHB;
    private Double avgDividendYield;
    private Double avgExchangeRate;
    private Double exchangeRate;
    private List<AssetsTopPricePayload> topGainers;
    private List<AssetsTopPricePayload> topLosers;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class THB {
        private Double totalCostTHB;
        private Double avgExchangeRate;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class USD {
        private Double totalHoldingUSD;
        private Double totalCostUSD;
    }
}
