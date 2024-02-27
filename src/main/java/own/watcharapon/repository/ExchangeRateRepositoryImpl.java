package own.watcharapon.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import own.watcharapon.entity.ExchangeRateEntity;
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
public class ExchangeRateRepositoryImpl implements ExchangeRateRepository {
    private final JdbcTemplate jdbcTemplate;
    @PersistenceContext
    private EntityManager entityManager;

    public ExchangeRateRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public LocalDate getLastUpdate() {
        try {
            String queryString = """
                    SELECT MAX(er.lastUpdate)
                    FROM ExchangeRateEntity er
                    """;
            return entityManager.createQuery(queryString, LocalDate.class)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public Double getLastExchangeRate() {
        try {
            String queryString = """
                    SELECT er.exchangeRate
                    FROM ExchangeRateEntity er
                    WHERE er.date = (
                        SELECT MAX(er2.date)
                        FROM ExchangeRateEntity er2
                    )
                    ORDER BY er.date
                    """;
            return entityManager.createQuery(queryString, Double.class)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    @Transactional
    public void save(ExchangeRateEntity exchangeRateEntity) {
        String queryString = """
                INSERT INTO exchange_rate_table(last_update,date,exchange_rate)
                VALUES (?1,?2,?3)
                ON CONFLICT (date)
                DO UPDATE
                SET
                    last_update = ?1
                """;
        entityManager.createNativeQuery(queryString)
                .setParameter(1, LocalDate.now())
                .setParameter(2, exchangeRateEntity.getDate())
                .setParameter(3, exchangeRateEntity.getExchangeRate())
                .executeUpdate();
    }
}
