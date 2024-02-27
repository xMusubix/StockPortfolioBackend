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

public interface ExchangeRateRepository {
    LocalDate getLastUpdate();

    Double getLastExchangeRate();

    void save(ExchangeRateEntity exchangeRateEntity);
}
