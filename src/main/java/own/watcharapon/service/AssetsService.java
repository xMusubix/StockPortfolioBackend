package own.watcharapon.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import own.watcharapon.entity.TransactionStockEntity;
import own.watcharapon.payload.*;
import own.watcharapon.repository.AssetsRepository;
import own.watcharapon.repository.ExchangeRateRepository;
import own.watcharapon.repository.TransactionCashRepository;
import own.watcharapon.repository.TransactionStockRepository;
import own.watcharapon.utils.ProcessDividendUtils;
import own.watcharapon.utils.ProcessPriceUtils;
import own.watcharapon.utils.StockTypeEnum;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Service
public class AssetsService {
    private static final Logger LOG = LoggerFactory.getLogger(AssetsService.class);
    private final AssetsRepository assetsRepository;
    private final TransactionStockRepository transactionStockRepository;
    private final ProcessPriceUtils processPriceUtils;
    private final ProcessDividendUtils processDividendUtils;
    private final TransactionCashRepository transactionCashRepository;
    private final ExchangeRateRepository exchangeRateRepository;

    public AssetsService(AssetsRepository assetsRepository, TransactionStockRepository transactionStockRepository, ProcessPriceUtils processPriceUtils, ProcessDividendUtils processDividendUtils, TransactionCashRepository transactionCashRepository, ExchangeRateRepository exchangeRateRepository) {
        this.assetsRepository = assetsRepository;
        this.transactionStockRepository = transactionStockRepository;
        this.processPriceUtils = processPriceUtils;
        this.processDividendUtils = processDividendUtils;
        this.transactionCashRepository = transactionCashRepository;
        this.exchangeRateRepository = exchangeRateRepository;
    }

    public void saveSymbol(List<String> symbolList) {
        assetsRepository.saveAllAssets(symbolList);
        updateHistoryPriceData();
        updateLatestPriceData(true);
        updateDividendData();
    }

    public void updateTarget(UUID id, Double target) {
        assetsRepository.updateTarget(id, target);
    }


    public void updateHistoryPriceData() {
        LOG.info("Start Update History Price Data");
        List<String> marketSymbolList = assetsRepository.getListToUpdateHistoryPrice();
        marketSymbolList.forEach(processPriceUtils::updateHistoryPrice);
    }

    public void updateLatestPriceData(boolean isStartup) {
        LOG.info("Start Update Latest Price Data");
        if (isWithinMarketHours() || isStartup) {
            List<UpdateLatestPricePayload> updateLatestPricePayloadList = assetsRepository.getListToUpdateLatestPrice();
            if (!updateLatestPricePayloadList.isEmpty()) {
                processPriceUtils.updateLatestPrice(updateLatestPricePayloadList);
            }
        }
    }

    public void updateDividendData() {
        LOG.info("Start Update Dividend Data");
        List<String> marketSymbolList = assetsRepository.getListToUpdateDividendData();
        processDividendUtils.updateDividendData(marketSymbolList);
    }

    private boolean isWithinMarketHours() {
        LocalTime localTime = LocalTime.now();
        LocalDate localDate = LocalDate.now();
        LocalTime openMarket = LocalTime.of(20, 25);
        LocalTime closeMarket = LocalTime.of(4, 5);
        DayOfWeek dayOfWeek = localDate.getDayOfWeek();
        if (dayOfWeek != DayOfWeek.SUNDAY && dayOfWeek != DayOfWeek.SATURDAY) {
            if (closeMarket.isAfter(openMarket)) {
                return !localTime.isBefore(openMarket) && !localTime.isAfter(closeMarket);
            } else {
                return !localTime.isBefore(openMarket) || !localTime.isAfter(closeMarket);
            }
        }
        return false;
    }

    public List<AssetsPayload> getAllAssets() {
        Double sumActualValue = 0.0;
        List<AssetsPayload> assetsPayloads = assetsRepository.getAll();
        for (AssetsPayload assetsPayload : assetsPayloads) {
            double lastPrice = assetsPayload.getLastPrice();
            if (assetsPayload.getYtdPrice() != null)
                assetsPayload.setYtdPrice(calculatePriceChangePercentage(lastPrice, assetsPayload.getYtdPrice()));
            else assetsPayload.setYtdPrice(0.0);
            if (assetsPayload.getWkPrice1() != null)
                assetsPayload.setWkPrice1(calculatePriceChangePercentage(lastPrice, assetsPayload.getWkPrice1()));
            else assetsPayload.setWkPrice1(0.0);
            if (assetsPayload.getMoPrice1() != null)
                assetsPayload.setMoPrice1(calculatePriceChangePercentage(lastPrice, assetsPayload.getMoPrice1()));
            else assetsPayload.setMoPrice1(0.0);
            if (assetsPayload.getMoPrice3() != null)
                assetsPayload.setMoPrice3(calculatePriceChangePercentage(lastPrice, assetsPayload.getMoPrice3()));
            else assetsPayload.setMoPrice3(0.0);
            if (assetsPayload.getMoPrice6() != null)
                assetsPayload.setMoPrice6(calculatePriceChangePercentage(lastPrice, assetsPayload.getMoPrice6()));
            else assetsPayload.setMoPrice6(0.0);
            if (assetsPayload.getYearPrice1() != null)
                assetsPayload.setYearPrice1(calculatePriceChangePercentage(lastPrice, assetsPayload.getYearPrice1()));
            else assetsPayload.setYearPrice1(0.0);
            if ((assetsPayload.getCostValue() != null && assetsPayload.getTotalShare() != null) &&
                    (assetsPayload.getCostValue().compareTo(0.0) != 0 && assetsPayload.getTotalShare().compareTo(0.0) != 0)) {
                assetsPayload.setAveragePrice(assetsPayload.getCostValue() / assetsPayload.getTotalShare());
                sumActualValue += assetsPayload.getHoldingValue();
                assetsPayload.setCostGain(calculatePriceChangePercentage(assetsPayload.getHoldingValue(), assetsPayload.getCostValue()));
                assetsPayload.setCostGainValue(assetsPayload.getHoldingValue() - assetsPayload.getCostValue());
                assetsPayload.setDailyCostGain((assetsPayload.getLastPriceChangePercentage() / 100) * assetsPayload.getCostValue());
            } else {
                assetsPayload.setAveragePrice(0.0);
                assetsPayload.setCostGain(0.0);
                assetsPayload.setCostGainValue(0.0);
                assetsPayload.setDailyCostGain(0.0);
            }
        }

        for (AssetsPayload assetsPayload : assetsPayloads) {
            assetsPayload.setActual((assetsPayload.getHoldingValue() / sumActualValue) * 100);
            assetsPayload.setDifferent(assetsPayload.getHoldingValue() - (sumActualValue * (assetsPayload.getTarget() / 100)));
        }

        return assetsPayloads;
    }

    public SummaryAssetsData getSummaryAssetsData() {
        return assetsRepository.getSummaryAssetsData();
    }

    private double calculatePriceChangePercentage(double lastPrice, double previousPrice) {
        double differenceYtdPrice = lastPrice - previousPrice;
        return (differenceYtdPrice / previousPrice) * 100;
    }

    public void updateCostAndShare(String marketSymbol) {
        double costValue = 0.0;
        double totalShare = 0.0;
        List<TransactionStockEntity> transactionStockEntityList = transactionStockRepository.getAllByMarketSymbolAndType(marketSymbol, StockTypeEnum.BUY_AND_SELL);
        for (TransactionStockEntity transactionStockEntity : transactionStockEntityList) {
            if (transactionStockEntity.getType().equals("Buy")) {
                costValue += (transactionStockEntity.getShare() * transactionStockEntity.getPrice()) + transactionStockEntity.getFee();
                totalShare += transactionStockEntity.getShare();
            } else if (transactionStockEntity.getType().equals("Sell")) {
                costValue -= (transactionStockEntity.getShare() * transactionStockEntity.getPrice()) + transactionStockEntity.getFee();
                totalShare -= transactionStockEntity.getShare();
            }
        }
        assetsRepository.updateCostAndTotalShare(new CostAndSharePayload(marketSymbol, costValue, totalShare));
    }

    public void updateAllCostAndShare() {
        LOG.info("Start Update All Cost And Share");
        List<String> marketSymbolList = assetsRepository.getListToCostAndTotalShare();
        marketSymbolList.forEach(this::updateCostAndShare);
    }

    public AssetsDashboardPayload getDashboardDetail() {
        AssetsDashboardPayload.USD usdData = assetsRepository.getTotalHoldingAndCost();
        AssetsDashboardPayload.THB thbData = transactionCashRepository.getAverageExRateAndTotalCost();
        AssetsDashboardPayload assetsDashboardPayload = new AssetsDashboardPayload();
        Double exchangeRate = exchangeRateRepository.getLastExchangeRate() - 0.1;
        Double avgDividendYield = assetsRepository.getAvgDividendYield();

        List<AssetsTopPricePayload> topGainers = assetsRepository.getTopGainers();
        List<AssetsTopPricePayload> topLosers = assetsRepository.getTopLosers();

        assetsDashboardPayload.setTotalHoldingUSD(usdData.getTotalHoldingUSD());
        assetsDashboardPayload.setTotalCostUSD(usdData.getTotalCostUSD());
        assetsDashboardPayload.setTotalCostTHB(thbData.getTotalCostTHB());
        assetsDashboardPayload.setAvgExchangeRate(thbData.getAvgExchangeRate());
        assetsDashboardPayload.setExchangeRate(exchangeRate);
        assetsDashboardPayload.setTotalHoldingTHB(usdData.getTotalHoldingUSD() * exchangeRate);
        assetsDashboardPayload.setChangeUSD(assetsDashboardPayload.getTotalHoldingUSD() - assetsDashboardPayload.getTotalCostUSD());
        assetsDashboardPayload.setChangeTHB(assetsDashboardPayload.getTotalHoldingTHB() - assetsDashboardPayload.getTotalCostTHB());
        assetsDashboardPayload.setChangePercentageUSD(calculatePriceChangePercentage(assetsDashboardPayload.getTotalHoldingUSD(), assetsDashboardPayload.getTotalCostUSD()));
        assetsDashboardPayload.setChangePercentageTHB(calculatePriceChangePercentage(assetsDashboardPayload.getTotalHoldingTHB(), assetsDashboardPayload.getTotalCostTHB()));
        assetsDashboardPayload.setAvgDividendYield(avgDividendYield);
        assetsDashboardPayload.setTopGainers(topGainers);
        assetsDashboardPayload.setTopLosers(topLosers);
        return assetsDashboardPayload;
    }

    public List<SumTargetBySector> getSumTargetBySector() {
        return assetsRepository.getSumTargetBySector();
    }
}
