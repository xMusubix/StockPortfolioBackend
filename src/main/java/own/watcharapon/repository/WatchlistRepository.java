package own.watcharapon.repository;

import own.watcharapon.payload.JittaPayload;
import own.watcharapon.payload.SymbolDataPayload;
import own.watcharapon.payload.SymbolPayload;

import java.util.List;

public interface WatchlistRepository {

    void saveSymbol(SymbolDataPayload symbolDataPayload);

    List<SymbolPayload> getAll();

    List<SymbolPayload> getAllFilterLastUpdateJitta();

    void updateJittaData(SymbolPayload symbolPayload, JittaPayload jittaPayload);
}
