package own.watcharapon.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import own.watcharapon.payload.IndustryPlainPayload;

import java.util.List;

@Repository


public class IndustryRepositoryImpl implements IndustryRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<IndustryPlainPayload> getAll() {
        String queryString = """
                SELECT
                    se.id AS sectorId,
                    se.sectorName,
                    ie.id AS industryId,
                    ie.industryName
                FROM
                    SectorEntity se
                INNER JOIN IndustryEntity ie ON ie.sectorName.sectorName = se.sectorName
                ORDER BY
                    se.sectorName ASC
                """;
        return entityManager.createQuery(queryString, IndustryPlainPayload.class)
                .getResultList();
    }
}
