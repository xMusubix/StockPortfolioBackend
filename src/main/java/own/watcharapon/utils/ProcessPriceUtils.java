package own.watcharapon.utils;

import lombok.extern.java.Log;
import net.jacobpeterson.alpaca.AlpacaAPI;
import net.jacobpeterson.alpaca.model.endpoint.marketdata.common.historical.bar.enums.BarTimePeriod;
import net.jacobpeterson.alpaca.model.endpoint.marketdata.stock.historical.bar.StockBar;
import net.jacobpeterson.alpaca.model.endpoint.marketdata.stock.historical.bar.StockBarsResponse;
import net.jacobpeterson.alpaca.model.endpoint.marketdata.stock.historical.bar.enums.BarAdjustment;
import net.jacobpeterson.alpaca.model.endpoint.marketdata.stock.historical.bar.enums.BarFeed;
import net.jacobpeterson.alpaca.model.endpoint.marketdata.stock.historical.snapshot.Snapshot;
import net.jacobpeterson.alpaca.rest.AlpacaClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import own.watcharapon.payload.AssetsHistoryPricePayload;
import own.watcharapon.payload.AssetsLatestPricePayload;
import own.watcharapon.payload.UpdateLatestPricePayload;
import own.watcharapon.repository.AssetsRepository;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAmount;
import java.util.*;

@Component
public class ProcessPriceUtils {
    private static final Logger LOG = LoggerFactory.getLogger(ProcessPriceUtils.class);
    private final AlpacaAPI alpacaAPI = new AlpacaAPI();
    private final AssetsRepository assetsRepository;

    public ProcessPriceUtils(AssetsRepository assetsRepository) {
        this.assetsRepository = assetsRepository;
    }

    private static AssetsLatestPricePayload getAssetsLatestPricePayload(Snapshot value, Optional<UpdateLatestPricePayload> optionalMarketSymbol) {
        double lastPrice = value.getLatestTrade().getPrice();
        double previousPrice = value.getPrevDailyBar().getClose();
        double differencePrice = lastPrice - previousPrice;
        double percentageChange = (differencePrice / previousPrice) * 100;

        AssetsLatestPricePayload assetsLatestPricePayload = new AssetsLatestPricePayload();
        assetsLatestPricePayload.setMarketSymbol(optionalMarketSymbol.get().getMarketSymbol());
        if (optionalMarketSymbol.get().getTotalShare() != null)
            assetsLatestPricePayload.setHoldingValue(optionalMarketSymbol.get().getTotalShare() * lastPrice);
        else assetsLatestPricePayload.setHoldingValue(0.0);
        assetsLatestPricePayload.setLastPrice(lastPrice);
        assetsLatestPricePayload.setLastPriceChange(differencePrice);
        assetsLatestPricePayload.setLastPriceChangePercentage(percentageChange);

        LOG.info("Last Trade Price of {} : {}", optionalMarketSymbol.get().getMarketSymbol(), lastPrice);
        return assetsLatestPricePayload;
    }

    public void updateHistoryPrice(String marketSymbol) {
        try {
            String symbol = marketSymbol.split("#")[1];
            LocalDate localDate = LocalDate.now();
            DayOfWeek dayOfWeek = localDate.getDayOfWeek();
            if (dayOfWeek == DayOfWeek.SUNDAY) {
                localDate = localDate.minusDays(2);
            } else if (dayOfWeek == DayOfWeek.SATURDAY) {
                localDate = localDate.minusDays(1);
            }
            LocalDate yearAgo = localDate.minusYears(1).minusDays(3);
            StockBarsResponse stockBarsResponse = alpacaAPI.stockMarketData().getBars(
                    symbol,
                    ZonedDateTime.of(yearAgo.getYear(), yearAgo.getMonthValue(), yearAgo.getDayOfMonth(), 0, 0, 0, 0, ZoneId.of("America/New_York")),
                    ZonedDateTime.of(localDate.getYear(), localDate.getMonthValue(), localDate.getDayOfMonth(), 0, 0, 0, 0, ZoneId.of("America/New_York")),
                    null,
                    null,
                    1,
                    BarTimePeriod.DAY,
                    BarAdjustment.SPLIT,
                    BarFeed.IEX);
            List<StockBar> bars = stockBarsResponse.getBars();

            Map<LocalDate, StockBar> priceMap = new LinkedHashMap<>();
            for (StockBar stockBar : bars) {
                priceMap.put(stockBar.getTimestamp().toLocalDate(), stockBar);
            }
            LocalDate wk1DateTime = localDate.minusDays(7);
            LocalDate mo1DateTime = localDate.minusMonths(1);
            LocalDate mo3DateTime = localDate.minusMonths(3);
            LocalDate mo6DateTime = localDate.minusMonths(6);
            LocalDate ytdDateTime = localDate.withDayOfYear(1);
            LocalDate yearDateTime = localDate.minusYears(1);

            StockBar lastPrice = bars.get(bars.size() - 1);
            StockBar wkPrice1 = getPriceForDate(priceMap, wk1DateTime);
            StockBar moPrice1 = getPriceForDate(priceMap, mo1DateTime);
            StockBar moPrice3 = getPriceForDate(priceMap, mo3DateTime);
            StockBar moPrice6 = getPriceForDate(priceMap, mo6DateTime);
            StockBar ytdPrice = getPriceForDate(priceMap, ytdDateTime);
            StockBar yearPrice1 = getPriceForDate(priceMap, yearDateTime);
            StockBar yearHigh = getYearHigh(priceMap, yearDateTime, localDate);
            StockBar yearLow = getYearLow(priceMap, yearDateTime, localDate);

            LOG.info("Last Time : {}\tPrice: {}", lastPrice.getTimestamp(), lastPrice.getClose());
            LOG.info("1 Week Time : {}\tPrice: {}", wkPrice1.getTimestamp(), wkPrice1.getClose());
            LOG.info("1 Month Time : {}\tPrice: {}", moPrice1.getTimestamp(), moPrice1.getClose());
            LOG.info("3 Month Time : {}\tPrice: {}", moPrice3.getTimestamp(), moPrice3.getClose());
            LOG.info("6 Month Time : {}\tPrice: {}", moPrice6.getTimestamp(), moPrice6.getClose());
            LOG.info("Year Time : {}\tPrice: {}", yearPrice1.getTimestamp(), yearPrice1.getClose());
            LOG.info("YTD Time : {}\tPrice: {}", ytdPrice.getTimestamp(), ytdPrice.getClose());
            LOG.info("Year High Time : {}\tPrice: {}", yearHigh.getTimestamp(), yearHigh.getClose());
            LOG.info("Year Low Time : {}\tPrice: {}", yearLow.getTimestamp(), yearLow.getClose());

            double lastPriceValue = lastPrice.getClose();
            double yearHighPriceValue = yearHigh.getClose();
            double yearLowPriceValue = yearLow.getClose();

            AssetsHistoryPricePayload assetsHistoryPricePayload = new AssetsHistoryPricePayload();
            assetsHistoryPricePayload.setMarketSymbol(marketSymbol);
            assetsHistoryPricePayload.setLastPrice(lastPriceValue);
            assetsHistoryPricePayload.setWkPrice1(wkPrice1.getClose());
            assetsHistoryPricePayload.setMoPrice1(moPrice1.getClose());
            assetsHistoryPricePayload.setMoPrice3(moPrice3.getClose());
            assetsHistoryPricePayload.setMoPrice6(moPrice6.getClose());
            assetsHistoryPricePayload.setYearPrice1(yearPrice1.getClose());
            assetsHistoryPricePayload.setYtdPrice(ytdPrice.getClose());
            assetsHistoryPricePayload.setYearHigh(yearHighPriceValue);
            assetsHistoryPricePayload.setYearLow(yearLowPriceValue);

            double percentageYear = calculatePercentage(yearLowPriceValue, yearHighPriceValue, lastPriceValue);
            assetsHistoryPricePayload.setPercentYear(percentageYear);

            assetsRepository.updateHistoryPrice(assetsHistoryPricePayload);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public double calculatePercentage(double min, double max, double value) {
        if (max <= min) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        return ((value - min) / (max - min)) * 100;
    }

    private StockBar getPriceForDate(Map<LocalDate, StockBar> priceMap, LocalDate targetDate) {
        StockBar defaultBar = new StockBar();
        for (Map.Entry<LocalDate, StockBar> entry : priceMap.entrySet()) {
            LocalDate timestamp = entry.getKey();
            if (timestamp.equals(targetDate)) {
                return entry.getValue();
            } else if (timestamp.isAfter(targetDate.minusDays(4)) && timestamp.isBefore(targetDate)) {
                defaultBar = entry.getValue();
            }
        }
        return defaultBar;
    }

    private StockBar getYearHigh(Map<LocalDate, StockBar> priceMap, LocalDate targetDate, LocalDate nowDate) {
        StockBar defaultBar = new StockBar();
        defaultBar.setClose(0.0);
        for (Map.Entry<LocalDate, StockBar> entry : priceMap.entrySet()) {
            LocalDate timestamp = entry.getKey();
            if (((timestamp.isAfter(targetDate.minusDays(4)) && timestamp.isBefore(nowDate)) || timestamp.equals(nowDate)) &&
                    (Double.compare(entry.getValue().getClose(), Math.max(defaultBar.getClose(), entry.getValue().getClose())) == 0)) {
                defaultBar = entry.getValue();
            }
        }
        return defaultBar;
    }

    private StockBar getYearLow(Map<LocalDate, StockBar> priceMap, LocalDate targetDate, LocalDate nowDate) {
        StockBar defaultBar = new StockBar();
        defaultBar.setClose(99999.0);
        for (Map.Entry<LocalDate, StockBar> entry : priceMap.entrySet()) {
            LocalDate timestamp = entry.getKey();
            if (((timestamp.isAfter(targetDate.minusDays(4)) && timestamp.isBefore(nowDate)) || timestamp.equals(nowDate)) &&
                    (Double.compare(entry.getValue().getClose(), Math.min(defaultBar.getClose(), entry.getValue().getClose())) == 0)) {
                defaultBar = entry.getValue();
            }
        }
        return defaultBar;
    }

    public void updateLatestPrice(List<UpdateLatestPricePayload> updateLatestPricePayloads) {
        try {
            List<String> symbols = updateLatestPricePayloads.stream()
                    .map(item -> item.getMarketSymbol().split("#")[1])
                    .toList();
            List<AssetsLatestPricePayload> assetsLatestPricePayloadList = new ArrayList<>();

            Map<String, Snapshot> snapshotMap = alpacaAPI.stockMarketData().getSnapshots(symbols);
            for (Map.Entry<String, Snapshot> entry : snapshotMap.entrySet()) {
                String key = entry.getKey();
                Snapshot value = entry.getValue();

                Optional<UpdateLatestPricePayload> optionalMarketSymbol = updateLatestPricePayloads.stream()
                        .filter(symbol -> symbol.getMarketSymbol().split("#")[1].equals(key))
                        .findFirst();

                if (optionalMarketSymbol.isPresent()) {
                    AssetsLatestPricePayload assetsLatestPricePayload = getAssetsLatestPricePayload(value, optionalMarketSymbol);

                    assetsLatestPricePayloadList.add(assetsLatestPricePayload);
                }
            }

            assetsRepository.updateLatestPrice(assetsLatestPricePayloadList);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

}
