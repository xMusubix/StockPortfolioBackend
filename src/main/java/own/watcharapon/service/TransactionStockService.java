package own.watcharapon.service;

import org.springframework.stereotype.Service;
import own.watcharapon.entity.TransactionStockEntity;
import own.watcharapon.payload.TransactionStockPayload;
import own.watcharapon.repository.TransactionStockRepository;
import own.watcharapon.utils.StockTypeEnum;

import java.util.List;
import java.util.UUID;

@Service
public class TransactionStockService {
    private final TransactionStockRepository transactionStockRepository;
    private final AssetsService assetsService;

    public TransactionStockService(TransactionStockRepository transactionStockRepository, AssetsService assetsService) {
        this.transactionStockRepository = transactionStockRepository;
        this.assetsService = assetsService;
    }

    public void save(TransactionStockPayload transactionStockPayload) {
        transactionStockRepository.save(transactionStockPayload);
        assetsService.updateCostAndShare(transactionStockPayload.getMarketSymbol());
    }

    public void delete(UUID id, String marketSymbol) {
        transactionStockRepository.delete(id);
        assetsService.updateCostAndShare(marketSymbol);
    }

    public List<TransactionStockEntity> getAllByMarketSymbol(String marketSymbol) {
        return transactionStockRepository.getAllByMarketSymbolAndType(marketSymbol, StockTypeEnum.ALL);
    }
}
