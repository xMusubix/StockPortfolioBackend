package own.watcharapon.repository;

import own.watcharapon.entity.TransactionCashEntity;
import own.watcharapon.entity.TransactionSavingEntity;
import own.watcharapon.payload.DashboardPayload;
import own.watcharapon.payload.SavingSummaryPayload;

import java.util.List;
import java.util.UUID;

public interface TransactionSavingRepository {

    void save(TransactionSavingEntity transactionSavingEntity);

    void delete(UUID id);

    List<TransactionSavingEntity> getAll();

    List<SavingSummaryPayload> getSumGroupApplication();
}
