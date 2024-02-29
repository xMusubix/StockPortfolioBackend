package own.watcharapon.service;

import org.springframework.stereotype.Service;
import own.watcharapon.payload.AssetsDashboardPayload;
import own.watcharapon.payload.DashboardPayload;
import own.watcharapon.payload.DividendDataPayload;
import own.watcharapon.payload.SavingSummaryPayload;
import own.watcharapon.repository.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DashboardService {
    private static final String[] MONTH_LABELS = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    private final TransactionCashRepository transactionCashRepository;
    private final AssetsRepository assetsRepository;
    private final ExchangeRateRepository exchangeRateRepository;
    private final TransactionSavingRepository transactionSavingRepository;
    private final TransactionStockRepository transactionStockRepository;

    public DashboardService(TransactionCashRepository transactionCashRepository, AssetsRepository assetsRepository, ExchangeRateRepository exchangeRateRepository, TransactionSavingRepository transactionSavingRepository, TransactionStockRepository transactionStockRepository) {
        this.transactionStockRepository = transactionStockRepository;
        this.transactionCashRepository = transactionCashRepository;
        this.assetsRepository = assetsRepository;
        this.exchangeRateRepository = exchangeRateRepository;
        this.transactionSavingRepository = transactionSavingRepository;
    }

    private Map<String, Double> convertToMap(List<DividendDataPayload> list) {
        Map<String, Double> map = new HashMap<>();
        for (DividendDataPayload payload : list) {
            String key = String.format("%s-%s", MONTH_LABELS[payload.getMonth() - 1], payload.getYear());
            map.put(key, payload.getTotalPrice() - payload.getTotalFee());
        }
        return map;
    }

    public DashboardPayload getDashboardData() {
        List<DividendDataPayload> dividendDataPayloadList = transactionStockRepository.getDividendData();
        AssetsDashboardPayload.USD usdData = assetsRepository.getTotalHoldingAndCost();
        AssetsDashboardPayload.THB thbData = transactionCashRepository.getAverageExRateAndTotalCost();
        Double exchangeRate = exchangeRateRepository.getLastExchangeRate() - 0.1;
        Double totalStock = usdData.getTotalHoldingUSD() * exchangeRate;
        List<SavingSummaryPayload> savingSummaryPayloadList = transactionSavingRepository.getSumGroupApplication();
        Double totalSavings = savingSummaryPayloadList.stream()
                .mapToDouble(SavingSummaryPayload::getAmount)
                .sum();
        Double totalBalance = totalStock + totalSavings;
        double percentageTotalStock = (totalStock / totalBalance) * 100;
        double percentageTotalSavings = (totalSavings / totalBalance) * 100;
        double changePercentageTHB = calculatePriceChangePercentage(totalStock, thbData.getTotalCostTHB());
        List<DashboardPayload.BarChartData> barChartDataList = convertToBarChartData(dividendDataPayloadList);
        DashboardPayload.LineChartData lineChartData = convertToLineChartData(dividendDataPayloadList);

        DashboardPayload dashboardPayload = new DashboardPayload();
        dashboardPayload.setTotalStock(totalStock);
        dashboardPayload.setTotalStockPercent(percentageTotalStock);
        dashboardPayload.setTotalSavings(totalSavings);
        dashboardPayload.setTotalSavingsPercent(percentageTotalSavings);
        dashboardPayload.setTotalBalance(totalBalance);
        dashboardPayload.setChangePercentageTHB(changePercentageTHB);
        dashboardPayload.setSavingSummaryPayloads(savingSummaryPayloadList);
        dashboardPayload.setBarChartDataList(barChartDataList);
        dashboardPayload.setLineChartData(lineChartData);

        return dashboardPayload;
    }

    private double calculatePriceChangePercentage(double lastPrice, double previousPrice) {
        double differenceYtdPrice = lastPrice - previousPrice;
        return (differenceYtdPrice / previousPrice) * 100;
    }

    public List<DashboardPayload.BarChartData> convertToBarChartData(List<DividendDataPayload> dividendDataPayloadList) {
        LocalDate localDate = LocalDate.now();
        int firstYear = dividendDataPayloadList.get(0).getYear();
        int lastYear = dividendDataPayloadList.get(dividendDataPayloadList.size() - 1).getYear();

        Map<Integer, List<Double>> yearToDividendAmountMap = new HashMap<>();
        for (int year = firstYear; year <= lastYear; year++) {
            int maxMonth = (localDate.getYear() == year) ? localDate.getMonthValue() : 12;
            List<Double> monthlyDividends = new ArrayList<>(maxMonth);
            for (int month = 1; month <= maxMonth; month++) {
                monthlyDividends.add(0.0);
            }
            yearToDividendAmountMap.put(year, monthlyDividends);
        }

        for (DividendDataPayload dividendData : dividendDataPayloadList) {
            int year = dividendData.getYear();
            int month = dividendData.getMonth();
            double totalDividend = dividendData.getTotalPrice() - dividendData.getTotalFee();

            List<Double> monthlyDividends = yearToDividendAmountMap.get(year);
            monthlyDividends.set(month - 1, totalDividend);
        }

        List<DashboardPayload.BarChartData> barChartDataList = new ArrayList<>();
        for (Map.Entry<Integer, List<Double>> entry : yearToDividendAmountMap.entrySet()) {
            int year = entry.getKey();
            List<Double> dividendAmounts = entry.getValue();
            barChartDataList.add(new DashboardPayload.BarChartData(String.valueOf(year), dividendAmounts));
        }

        return barChartDataList;
    }

    public DashboardPayload.LineChartData convertToLineChartData(List<DividendDataPayload> dividendDataPayloadList) {
        LocalDate localDate = LocalDate.now();
        Map<String, Double> dividendAmountsMap = convertToMap(dividendDataPayloadList);
        List<String> labels = new ArrayList<>();
        List<Double> dividendAmounts = new ArrayList<>();

        int firstYear = dividendDataPayloadList.get(0).getYear();
        int lastYear = dividendDataPayloadList.get(dividendDataPayloadList.size() - 1).getYear();

        for (int year = firstYear; year <= lastYear; year++) {
            int minMonth = (firstYear == year) ? dividendDataPayloadList.get(0).getMonth() : 1;
            int maxMonth = (localDate.getYear() == year) ? localDate.getMonthValue() : 12;

            for (int month = minMonth; month <= maxMonth; month++) {
                String label = String.join("-", MONTH_LABELS[month - 1], String.valueOf(year));
                labels.add(label);
                dividendAmounts.add(dividendAmountsMap.getOrDefault(label, 0.0));
            }
        }

        return new DashboardPayload.LineChartData(labels, dividendAmounts);
    }
}
