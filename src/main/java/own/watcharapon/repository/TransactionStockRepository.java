package own.watcharapon.repository;

import own.watcharapon.entity.TransactionStockEntity;
import own.watcharapon.payload.DividendDataPayload;
import own.watcharapon.payload.TransactionStockPayload;
import own.watcharapon.utils.StockTypeEnum;

import java.util.List;
import java.util.UUID;

public interface TransactionStockRepository {

    void save(TransactionStockPayload transactionStockPayload);

    void delete(UUID id);

    List<TransactionStockEntity> getAllByMarketSymbolAndType(String marketSymbol, StockTypeEnum type);

    List<DividendDataPayload> getDividendData();
}
