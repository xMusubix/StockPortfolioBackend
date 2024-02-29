package own.watcharapon.repository;

import own.watcharapon.entity.TransactionCashEntity;
import own.watcharapon.payload.AssetsDashboardPayload;

import java.util.List;
import java.util.UUID;

public interface TransactionCashRepository {

    void save(TransactionCashEntity transactionCashEntity);

    void delete(UUID id);

    List<TransactionCashEntity> getAll();

    Double getTotalCost();

    AssetsDashboardPayload.THB getAverageExRateAndTotalCost();
}
