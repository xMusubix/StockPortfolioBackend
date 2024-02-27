package own.watcharapon.service;

import org.springframework.stereotype.Service;
import own.watcharapon.entity.TransactionCashEntity;
import own.watcharapon.repository.TransactionCashRepository;

import java.util.List;
import java.util.UUID;

@Service
public class TransactionCashService {
    private final TransactionCashRepository transactionCashRepository;

    public TransactionCashService(TransactionCashRepository transactionCashRepository) {
        this.transactionCashRepository = transactionCashRepository;
    }

    public void save(TransactionCashEntity transactionCashEntity) {
        transactionCashRepository.save(transactionCashEntity);
    }

    public void delete(UUID id) {
        transactionCashRepository.delete(id);
    }

    public List<TransactionCashEntity> getAll() {
        return transactionCashRepository.getAll();
    }
}
