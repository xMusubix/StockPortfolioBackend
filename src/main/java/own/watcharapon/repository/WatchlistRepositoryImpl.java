package own.watcharapon.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import own.watcharapon.entity.IndustryEntity;
import own.watcharapon.entity.SectorEntity;
import own.watcharapon.entity.WatchlistEntity;
import own.watcharapon.payload.JittaPayload;
import own.watcharapon.payload.SymbolDataPayload;
import own.watcharapon.payload.SymbolPayload;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class WatchlistRepositoryImpl implements WatchlistRepository {
    private final JdbcTemplate jdbcTemplate;
    @PersistenceContext
    private EntityManager entityManager;

    public WatchlistRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @Transactional
    public void saveSymbol(SymbolDataPayload symbolDataPayload) {
        SectorEntity sectorEntity = findOrCreateSector(symbolDataPayload.getSector());
        IndustryEntity industryEntity = findOrCreateIndustry(symbolDataPayload.getIndustry(), sectorEntity);
        findOrCreateWatchlist(symbolDataPayload, industryEntity);
    }

    private SectorEntity findOrCreateSector(String sectorName) {
        try {
            return getSectorEntity(sectorName);
        } catch (NoResultException e) {
            insertSector(sectorName);
            return getSectorEntity(sectorName);
        }
    }

    private void insertSector(String sectorName) {
        String insertQuery = """
                INSERT INTO SectorEntity (sectorName)
                VALUES (?1)
                """;
        entityManager.createQuery(insertQuery)
                .setParameter(1, sectorName)
                .executeUpdate();
    }

    private SectorEntity getSectorEntity(String sectorName) {
        String queryString = """
                SELECT se.id,se.sectorName
                FROM SectorEntity se
                WHERE se.sectorName = ?1
                """;
        return entityManager.createQuery(queryString, SectorEntity.class)
                .setParameter(1, sectorName)
                .getSingleResult();
    }

    private IndustryEntity findOrCreateIndustry(String industryName, SectorEntity sectorEntity) {
        try {
            return getIndustryEntity(industryName);
        } catch (NoResultException e) {
            insertIndustry(industryName, sectorEntity);
            return getIndustryEntity(industryName);
        }
    }

    private void insertIndustry(String industryName, SectorEntity sectorEntity) {
        String insertQuery = """
                INSERT INTO industry_table (industry_name,sector_name)
                VALUES (?1,?2)
                """;
        entityManager.createNativeQuery(insertQuery)
                .setParameter(1, industryName)
                .setParameter(2, sectorEntity.getSectorName())
                .executeUpdate();
    }

    private IndustryEntity getIndustryEntity(String industryName) {
        String queryString = """
                SELECT ie.id,ie.industryName,ie.sectorName
                FROM IndustryEntity ie
                WHERE ie.industryName = ?1
                """;
        return entityManager.createQuery(queryString, IndustryEntity.class)
                .setParameter(1, industryName)
                .getSingleResult();
    }

    private WatchlistEntity findOrCreateWatchlist(SymbolDataPayload symbolDataPayload, IndustryEntity industryEntity) {
        String watchlistId = String.join("#", symbolDataPayload.getMarket(), symbolDataPayload.getSymbol());
        try {
            return getWatchlistEntity(watchlistId);
        } catch (NoResultException e) {
            insertWatchlist(watchlistId, symbolDataPayload, industryEntity);
            return getWatchlistEntity(watchlistId);
        }
    }

    private void insertWatchlist(String watchlistId, SymbolDataPayload symbolDataPayload, IndustryEntity industryEntity) {
        String insertQuery = """
                INSERT INTO watchlist_table (market_symbol,market,symbol,industry)
                VALUES (?1,?2,?3,?4)
                ON CONFLICT DO NOTHING
                """;
        entityManager.createNativeQuery(insertQuery)
                .setParameter(1, watchlistId)
                .setParameter(2, symbolDataPayload.getMarket())
                .setParameter(3, symbolDataPayload.getSymbol())
                .setParameter(4, industryEntity.getIndustryName())
                .executeUpdate();
    }

    private WatchlistEntity getWatchlistEntity(String marketSymbol) {
        String queryString = """
                SELECT we.id,we.marketSymbol
                FROM WatchlistEntity we
                WHERE we.marketSymbol = ?1
                """;
        return entityManager.createQuery(queryString, WatchlistEntity.class)
                .setParameter(1, marketSymbol)
                .getSingleResult();
    }

    @Override
    public List<SymbolPayload> getAll() {
        String queryString = """
                SELECT we.id,we.market,we.symbol
                FROM WatchlistEntity we
                ORDER BY we.symbol ASC
                """;
        return entityManager.createQuery(queryString, SymbolPayload.class)
                .getResultList();
    }

    @Override
    public List<SymbolPayload> getAllFilterLastUpdateJitta() {
        String queryString = """
                SELECT we.id,we.market,we.symbol
                FROM WatchlistEntity we
                WHERE lastUpdateJitta <= ?1 OR lastUpdateJitta IS NULL
                ORDER BY we.symbol ASC
                """;
        return entityManager.createQuery(queryString, SymbolPayload.class)
                .setParameter(1, LocalDateTime.now().minusDays(7))
                .getResultList();
    }

    @Override
    public List<String> getAllSymbolNotInAssets() {
        String queryString = """
                SELECT we.symbol
                FROM WatchlistEntity we
                WHERE we.marketSymbol NOT IN (
                    SELECT ae.marketSymbol.marketSymbol
                    FROM AssetsEntity ae
                    WHERE ae.marketSymbol IS NOT NULL
                )
                ORDER BY we.symbol ASC
                """;
        return entityManager.createQuery(queryString, String.class)
                .getResultList();
    }

    @Override
    @Transactional
    public void updateJittaData(SymbolPayload symbolPayload, JittaPayload jittaPayload) {
        String queryString = """
                UPDATE WatchlistEntity we
                SET we.line = ?2,
                    we.score = ?3,
                    we.state = ?4,
                    we.lastUpdateJitta = CURRENT_TIMESTAMP
                WHERE we.id = ?1
                """;
        entityManager.createQuery(queryString)
                .setParameter(1, symbolPayload.getId())
                .setParameter(2, jittaPayload.getLine())
                .setParameter(3, jittaPayload.getScore())
                .setParameter(4, jittaPayload.getState())
                .executeUpdate();
    }


}
