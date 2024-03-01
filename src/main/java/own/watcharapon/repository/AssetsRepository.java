package own.watcharapon.repository;

import own.watcharapon.payload.*;

import java.util.List;
import java.util.UUID;

public interface AssetsRepository {

    void saveAllAssets(List<String> symbolList);

    void updateTarget(UUID id, Double target);

    void updateHistoryPrice(AssetsHistoryPricePayload assetsHistoryPricePayload);

    void updateLatestPrice(List<AssetsLatestPricePayload> assetsLatestPricePayloadList);

    void updateDividendPrice(AssetsDividendDataPayload assetsHistoryPricePayload);

    void updateCostAndTotalShare(CostAndSharePayload costAndSharePayload);

    List<String> getListToUpdateHistoryPrice();

    List<UpdateLatestPricePayload> getListToUpdateLatestPrice();

    List<String> getListToUpdateDividendData();

    List<String> getListToCostAndTotalShare();

    List<AssetsPayload> getAll();

    SummaryAssetsData getSummaryAssetsData();

    AssetsDashboardPayload.USD getTotalHoldingAndCost();

    Double getAvgDividendYield();

    List<AssetsTopPricePayload> getTopGainers();

    List<AssetsTopPricePayload> getTopLosers();

    List<SumTargetBySector> getSumTargetBySector();
}
