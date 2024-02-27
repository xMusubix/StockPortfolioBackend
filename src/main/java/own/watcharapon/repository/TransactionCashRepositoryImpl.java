package own.watcharapon.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import own.watcharapon.entity.TransactionCashEntity;
import own.watcharapon.payload.DashboardPayload;
import own.watcharapon.payload.TransactionCashExchangeRatePayload;

import java.util.List;
import java.util.UUID;

@Repository
public class TransactionCashRepositoryImpl implements TransactionCashRepository {
    private final JdbcTemplate jdbcTemplate;
    @PersistenceContext
    private EntityManager entityManager;

    public TransactionCashRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @Transactional
    public void save(TransactionCashEntity transactionCashEntity) {
        String queryString = """
                INSERT INTO TransactionCashEntity(
                    date, exRate, thb, usd, type, note)
                    VALUES (?1, ?2, ?3, ?4, ?5, ?6)
                """;
        entityManager.createQuery(queryString)
                .setParameter(1, transactionCashEntity.getDate())
                .setParameter(2, transactionCashEntity.getExRate())
                .setParameter(3, transactionCashEntity.getThb())
                .setParameter(4, transactionCashEntity.getUsd())
                .setParameter(5, transactionCashEntity.getType())
                .setParameter(6, transactionCashEntity.getNote())
                .executeUpdate();
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        String queryString = """
                DELETE FROM TransactionCashEntity tce
                WHERE tce.id = ?1
                """;
        entityManager.createQuery(queryString)
                .setParameter(1, id)
                .executeUpdate();
    }

    @Override
    public List<TransactionCashEntity> getAll() {
        String queryString = """
                SELECT tce
                FROM TransactionCashEntity tce
                ORDER BY tce.date DESC
                """;
        return entityManager.createQuery(queryString, TransactionCashEntity.class)
                .getResultList();
    }

    @Override
    public Double getTotalCost() {
        String queryString = """
                SELECT
                    SUM(trc.thb) AS totalCostTHB
                FROM TransactionCashEntity trc
                WHERE trc.type = 'ExchangeToUSD'
                """;
        return entityManager.createQuery(queryString, Double.class)
                .getSingleResult();
    }

    @Override
    public DashboardPayload.THB getAverageExRateAndTotalCost() {
        String queryString = """
                SELECT
                    SUM(trc.thb) as totalCostTHB,
                    SUM(trc.exRate * trc.thb) / SUM(trc.thb) AS avgExchangeRate
                FROM TransactionCashEntity trc
                WHERE trc.type = 'ExchangeToUSD'
                """;
        return entityManager.createQuery(queryString, DashboardPayload.THB.class)
                .getSingleResult();
    }
}
