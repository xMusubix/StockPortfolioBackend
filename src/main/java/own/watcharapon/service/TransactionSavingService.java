package own.watcharapon.service;

import org.springframework.stereotype.Service;
import own.watcharapon.entity.TransactionCashEntity;
import own.watcharapon.entity.TransactionSavingEntity;
import own.watcharapon.payload.SavingSummaryPayload;
import own.watcharapon.repository.TransactionCashRepository;
import own.watcharapon.repository.TransactionSavingRepository;

import java.util.List;
import java.util.UUID;

@Service
public class TransactionSavingService {
    private final TransactionSavingRepository transactionSavingRepository;

    public TransactionSavingService(TransactionSavingRepository transactionSavingRepository) {
        this.transactionSavingRepository = transactionSavingRepository;
    }


    public void save(TransactionSavingEntity transactionSavingEntity) {
        transactionSavingRepository.save(transactionSavingEntity);
    }

    public void delete(UUID id) {
        transactionSavingRepository.delete(id);
    }

    public List<TransactionSavingEntity> getAll() {
        return transactionSavingRepository.getAll();
    }

    public List<SavingSummaryPayload> getSumGroupApplication() {
        return transactionSavingRepository.getSumGroupApplication();
    }
}
