package own.watcharapon.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import own.watcharapon.entity.TransactionCashEntity;
import own.watcharapon.entity.TransactionSavingEntity;
import own.watcharapon.payload.DashboardPayload;
import own.watcharapon.payload.SavingSummaryPayload;

import java.util.List;
import java.util.UUID;

@Repository
public class TransactionSavingRepositoryImpl implements TransactionSavingRepository {
    private final JdbcTemplate jdbcTemplate;
    @PersistenceContext
    private EntityManager entityManager;

    public TransactionSavingRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @Transactional
    public void save(TransactionSavingEntity transactionSavingEntity) {
        String queryString = """
                INSERT INTO TransactionSavingEntity(
                    date, type, amount, application, note)
                    VALUES (?1, ?2, ?3, ?4, ?5)
                """;
        entityManager.createQuery(queryString)
                .setParameter(1, transactionSavingEntity.getDate())
                .setParameter(2, transactionSavingEntity.getType())
                .setParameter(3, transactionSavingEntity.getAmount())
                .setParameter(4, transactionSavingEntity.getApplication())
                .setParameter(5, transactionSavingEntity.getNote())
                .executeUpdate();
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        String queryString = """
                DELETE FROM TransactionSavingEntity tse
                WHERE tse.id = ?1
                """;
        entityManager.createQuery(queryString)
                .setParameter(1, id)
                .executeUpdate();
    }

    @Override
    public List<TransactionSavingEntity> getAll() {
        String queryString = """
                SELECT tse
                FROM TransactionSavingEntity tse
                ORDER BY tse.date DESC
                """;
        return entityManager.createQuery(queryString, TransactionSavingEntity.class)
                .getResultList();
    }

    @Override
    public List<SavingSummaryPayload> getSumGroupApplication() {
        String queryString = """
                SELECT
                    tse.application,
                    SUM(tse.amount) - COALESCE(SUM(tse2.amount), 0) AS amount
                FROM
                    TransactionSavingEntity tse
                LEFT JOIN
                    TransactionSavingEntity tse2 ON tse2.type = 'Withdraw' AND tse2.application = tse.application
                WHERE
                    tse.type = 'Deposit'
                GROUP BY
                    tse.application
                ORDER BY
                    tse.application ASC
                """;
        return entityManager.createQuery(queryString, SavingSummaryPayload.class)
                .getResultList();
    }
}
