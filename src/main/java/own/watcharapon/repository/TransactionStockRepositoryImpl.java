package own.watcharapon.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import own.watcharapon.entity.TransactionCashEntity;
import own.watcharapon.entity.TransactionStockEntity;
import own.watcharapon.payload.TransactionStockPayload;
import own.watcharapon.utils.StockTypeEnum;

import java.util.List;
import java.util.UUID;

@Repository
public class TransactionStockRepositoryImpl implements TransactionStockRepository {
    private final JdbcTemplate jdbcTemplate;
    @PersistenceContext
    private EntityManager entityManager;

    public TransactionStockRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @Transactional
    public void save(TransactionStockPayload transactionStockPayload) {
        String queryString = """
                INSERT INTO transaction_stock_table(
                    market_symbol ,date, type, share, price, fee, note)
                    VALUES (?1, ?2, ?3, ?4, ?5, ?6, ?7)
                """;
        entityManager.createNativeQuery(queryString)
                .setParameter(1, transactionStockPayload.getMarketSymbol())
                .setParameter(2, transactionStockPayload.getDate())
                .setParameter(3, transactionStockPayload.getType())
                .setParameter(4, transactionStockPayload.getShare())
                .setParameter(5, transactionStockPayload.getPrice())
                .setParameter(6, transactionStockPayload.getFee())
                .setParameter(7, transactionStockPayload.getNote())
                .executeUpdate();
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        String queryString = """
                DELETE FROM TransactionStockEntity tse
                WHERE tse.id = ?1
                """;
        entityManager.createQuery(queryString)
                .setParameter(1, id)
                .executeUpdate();
    }

    @Override
    public List<TransactionStockEntity> getAllByMarketSymbolAndType(String marketSymbol, StockTypeEnum type) {
        String selectType = "";
        switch (type) {
            case BUY_AND_SELL -> selectType = "AND tse.type != 'Dividend'";
            case BUY -> selectType = "AND tse.type = 'Buy'";
            case SELL -> selectType = "AND tse.type = 'Sell'";
            case DIVIDEND -> selectType = "AND tse.type = 'Dividend'";
            case ALL -> selectType = "";
        }
        String queryString = String.format("""
                SELECT tse
                FROM TransactionStockEntity tse
                WHERE tse.marketSymbol.marketSymbol = ?1 %s
                ORDER BY tse.date DESC
                """, selectType);
        return entityManager.createQuery(queryString, TransactionStockEntity.class)
                .setParameter(1, marketSymbol)
                .getResultList();
    }
}
