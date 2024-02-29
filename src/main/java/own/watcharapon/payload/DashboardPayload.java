package own.watcharapon.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardPayload {
    private Double totalBalance;
    private Double totalStock;
    private Double totalStockPercent;
    private Double changePercentageTHB;
    private Double totalSavings;
    private Double totalSavingsPercent;
    private List<SavingSummaryPayload> savingSummaryPayloads;
    private List<BarChartData> barChartDataList;
    private LineChartData lineChartData;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BarChartData {
        private String year;
        private List<Double> dividendAmount;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LineChartData {
        private List<String> labels;
        private List<Double> dividendAmount;
    }
}
