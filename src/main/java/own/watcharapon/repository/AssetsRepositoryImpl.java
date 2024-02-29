package own.watcharapon.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import own.watcharapon.payload.*;

import java.sql.PreparedStatement;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public class AssetsRepositoryImpl implements AssetsRepository {
    private final JdbcTemplate jdbcTemplate;
    @PersistenceContext
    private EntityManager entityManager;

    public AssetsRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @Transactional
    public void saveAllAssets(List<String> symbolList) {
        jdbcTemplate.batchUpdate("""
                        INSERT INTO assets_table (market_symbol,target)
                        SELECT market_symbol, 0 as target
                        FROM watchlist_table
                        WHERE symbol = ? ;
                        """,
                symbolList,
                1000,
                (PreparedStatement ps, String symbol) -> ps.setString(1, symbol));
    }

    @Override
    @Transactional
    public void updateTarget(UUID id, Double target) {
        String queryString = """
                UPDATE AssetsEntity ae
                SET ae.target = ?1
                WHERE ae.id = ?2
                """;
        entityManager.createQuery(queryString)
                .setParameter(1, target)
                .setParameter(2, id)
                .executeUpdate();
    }

    @Override
    @Transactional
    public void updateHistoryPrice(AssetsHistoryPricePayload assetsHistoryPricePayload) {
        String queryString = """
                UPDATE AssetsEntity ae
                SET
                    ae.lastUpdateHistoryPrice = CURRENT_TIMESTAMP,
                    ae.ytdPrice = ?1,
                    ae.wkPrice1 = ?2,
                    ae.moPrice1 = ?3,
                    ae.moPrice3 = ?4,
                    ae.moPrice6 = ?5,
                    ae.yearPrice1 = ?6,
                    ae.lastPrice = ?7,
                    ae.yearHigh = ?8,
                    ae.yearLow = ?9,
                    ae.percentYear = ?10
                WHERE ae.marketSymbol.marketSymbol = ?11
                """;
        entityManager.createQuery(queryString)
                .setParameter(1, assetsHistoryPricePayload.getYtdPrice())
                .setParameter(2, assetsHistoryPricePayload.getWkPrice1())
                .setParameter(3, assetsHistoryPricePayload.getMoPrice1())
                .setParameter(4, assetsHistoryPricePayload.getMoPrice3())
                .setParameter(5, assetsHistoryPricePayload.getMoPrice6())
                .setParameter(6, assetsHistoryPricePayload.getYearPrice1())
                .setParameter(7, assetsHistoryPricePayload.getLastPrice())
                .setParameter(8, assetsHistoryPricePayload.getYearHigh())
                .setParameter(9, assetsHistoryPricePayload.getYearLow())
                .setParameter(10, assetsHistoryPricePayload.getPercentYear())
                .setParameter(11, assetsHistoryPricePayload.getMarketSymbol())
                .executeUpdate();
    }

    @Override
    @Transactional
    public void updateLatestPrice(List<AssetsLatestPricePayload> assetsLatestPricePayloadList) {
        jdbcTemplate.batchUpdate("""
                        UPDATE assets_table
                        SET
                            last_update_last_price = CURRENT_TIMESTAMP,
                            last_price = ?,
                            last_price_change = ?,
                            last_price_change_percentage = ?,
                            holding_value = ?
                        WHERE market_symbol = ?
                        """, assetsLatestPricePayloadList, 1000,
                (PreparedStatement ps, AssetsLatestPricePayload payload) -> {
                    ps.setDouble(1, payload.getLastPrice());
                    ps.setDouble(2, payload.getLastPriceChange());
                    ps.setDouble(3, payload.getLastPriceChangePercentage());
                    ps.setDouble(4, payload.getHoldingValue());
                    ps.setString(5, payload.getMarketSymbol());
                });
    }

    @Override
    @Transactional
    public void updateDividendPrice(AssetsDividendDataPayload assetsDividendDataPayload) {
        String queryString = """
                UPDATE AssetsEntity ae
                SET
                    ae.lastUpdateDividend = CURRENT_TIMESTAMP,
                    ae.exDate = ?1,
                    ae.dividendYield = ?2,
                    ae.dividendAmount = ?3,
                    ae.payoutRatio = ?4
                WHERE ae.marketSymbol.marketSymbol = ?5
                """;
        entityManager.createQuery(queryString)
                .setParameter(1, assetsDividendDataPayload.getExDate())
                .setParameter(2, assetsDividendDataPayload.getDividendYield())
                .setParameter(3, assetsDividendDataPayload.getDividendAmount())
                .setParameter(4, assetsDividendDataPayload.getPayoutRatio())
                .setParameter(5, assetsDividendDataPayload.getMarketSymbol())
                .executeUpdate();
    }

    @Override
    @Transactional
    public void updateCostAndTotalShare(CostAndSharePayload costAndSharePayload) {
        String queryString = """
                UPDATE AssetsEntity ae
                SET
                    ae.totalShare = ?1,
                    ae.costValue = ?2
                WHERE ae.marketSymbol.marketSymbol = ?3
                """;
        entityManager.createQuery(queryString)
                .setParameter(1, costAndSharePayload.getTotalShare())
                .setParameter(2, costAndSharePayload.getCostValue())
                .setParameter(3, costAndSharePayload.getMarketSymbol())
                .executeUpdate();
    }

    @Override
    public List<String> getListToUpdateHistoryPrice() {
        String queryString = """
                SELECT ae.marketSymbol.marketSymbol
                FROM AssetsEntity ae
                WHERE lastUpdateHistoryPrice <= ?1 OR lastUpdateHistoryPrice IS NULL
                ORDER BY ae.marketSymbol ASC
                """;
        return entityManager.createQuery(queryString, String.class)
                .setParameter(1, LocalDateTime.now().minusDays(1))
                .getResultList();
    }

    @Override
    public List<UpdateLatestPricePayload> getListToUpdateLatestPrice() {
        String queryString = """
                SELECT ae.marketSymbol.marketSymbol,ae.totalShare
                FROM AssetsEntity ae
                WHERE lastUpdateLastPrice <= ?1 OR lastUpdateLastPrice IS NULL
                ORDER BY ae.marketSymbol ASC
                """;
        return entityManager.createQuery(queryString, UpdateLatestPricePayload.class)
                .setParameter(1, LocalDateTime.now().minusMinutes(1))
                .getResultList();
    }

    @Override
    public List<String> getListToUpdateDividendData() {
        String queryString = """
                SELECT ae.marketSymbol.marketSymbol
                FROM AssetsEntity ae
                WHERE lastUpdateDividend <= ?1 OR lastUpdateDividend IS NULL
                ORDER BY ae.marketSymbol ASC
                """;
        return entityManager.createQuery(queryString, String.class)
                .setParameter(1, LocalDateTime.now().minusDays(7))
                .getResultList();
    }

    @Override
    public List<String> getListToCostAndTotalShare() {
        String queryString = """
                SELECT ae.marketSymbol.marketSymbol
                FROM AssetsEntity ae
                ORDER BY ae.marketSymbol ASC
                """;
        return entityManager.createQuery(queryString, String.class)
                .getResultList();
    }

    @Override
    public List<AssetsPayload> getAll() {
        String queryString = """
                SELECT
                    ass.id,
                    wt.marketSymbol,
                    wt.symbol,
                    wt.score,
                    wt.line,
                    wt.state,
                    st.sectorName,
                    it.industryName,
                    ass.totalShare,
                    ass.costValue,
                    ass.holdingValue,
                    ass.target,
                    ass.ytdPrice,
                    ass.wkPrice1,
                    ass.moPrice1,
                    ass.moPrice3,
                    ass.moPrice6,
                    ass.yearPrice1,
                    ass.yearHigh,
                    ass.yearLow,
                    ass.percentYear,
                    ass.lastPrice,
                    ass.lastPriceChange,
                    ass.lastPriceChangePercentage,
                    ass.exDate,
                    ass.dividendYield,
                    ass.dividendAmount,
                    ass.payoutRatio,
                    ass.note
                FROM AssetsEntity ass
                INNER JOIN WatchlistEntity wt ON wt.marketSymbol = ass.marketSymbol.marketSymbol
                INNER JOIN IndustryEntity it ON it.industryName = wt.industry.industryName
                INNER JOIN SectorEntity st ON st.sectorName = it.sectorName.sectorName
                ORDER BY ass.holdingValue DESC, wt.symbol ASC
                """;
        return entityManager.createQuery(queryString, AssetsPayload.class)
                .getResultList();
    }

    @Override
    public SummaryAssetsData getSummaryAssetsData() {
        String queryString = """
                SELECT
                    COUNT(ass.marketSymbol.marketSymbol) as assetsCount,
                    SUM(ass.target) as target,
                    MIN(we.score) as minScore,
                    MAX(we.score) as maxScore
                FROM AssetsEntity ass
                INNER JOIN WatchlistEntity we ON we.marketSymbol = ass.marketSymbol.marketSymbol
                """;
        return entityManager.createQuery(queryString, SummaryAssetsData.class)
                .getSingleResult();
    }

    @Override
    public AssetsDashboardPayload.USD getTotalHoldingAndCost() {
        String queryString = """
                SELECT
                    SUM(ass.holdingValue) AS totalHoldingUSD,
                    SUM(ass.costValue) AS totalCostUSD
                FROM AssetsEntity ass
                """;
        return entityManager.createQuery(queryString, AssetsDashboardPayload.USD.class)
                .getSingleResult();
    }

    @Override
    public Double getAvgDividendYield() {
        String queryString = """
                SELECT
                    SUM(ass.dividendYield * ass.holdingValue) / SUM(ass.holdingValue) AS avgDividendYield
                FROM AssetsEntity ass
                """;
        return entityManager.createQuery(queryString, Double.class)
                .getSingleResult();
    }

    @Override
    public List<AssetsTopPricePayload> getTopGainers() {
        String queryString = """
                SELECT
                    ass.marketSymbol.symbol,
                    ass.lastPriceChange,
                    ass.lastPriceChangePercentage
                FROM AssetsEntity ass
                ORDER BY ass.lastPriceChangePercentage DESC
                LIMIT 8
                """;
        return entityManager.createQuery(queryString, AssetsTopPricePayload.class)
                .getResultList();
    }

    @Override
    public List<AssetsTopPricePayload> getTopLosers() {
        String queryString = """
                SELECT
                    ass.marketSymbol.symbol,
                    ass.lastPriceChange,
                    ass.lastPriceChangePercentage
                FROM AssetsEntity ass
                ORDER BY ass.lastPriceChangePercentage ASC
                LIMIT 8
                """;
        return entityManager.createQuery(queryString, AssetsTopPricePayload.class)
                .getResultList();
    }
}
