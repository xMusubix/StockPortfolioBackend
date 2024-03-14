package own.watcharapon.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import own.watcharapon.payload.SymbolDataPayload;
import own.watcharapon.payload.SymbolPayload;
import own.watcharapon.repository.WatchlistRepository;
import own.watcharapon.utils.ProcessJittaUtils;
import own.watcharapon.utils.SymbolDataUtils;

import java.util.List;
import java.util.UUID;

@Service
public class WatchlistService {
    private static final Logger LOG = LoggerFactory.getLogger(ProcessJittaUtils.class);
    private final WatchlistRepository watchlistRepository;
    private final ProcessJittaUtils processJittaUtils;
    private final SymbolDataUtils symbolDataUtils;

    public WatchlistService(WatchlistRepository watchlistRepository, ProcessJittaUtils processJittaUtils, SymbolDataUtils symbolDataUtils) {
        this.watchlistRepository = watchlistRepository;
        this.processJittaUtils = processJittaUtils;
        this.symbolDataUtils = symbolDataUtils;
    }

    public boolean checkSymbolAndSave(String symbol) {
        SymbolDataPayload symbolDataPayload = symbolDataUtils.checkSymbol(symbol.toUpperCase());
        if (symbolDataPayload == null)
            return true;
        else {
            symbolDataPayload.setSymbol(symbol.toUpperCase());
            watchlistRepository.saveSymbol(symbolDataPayload);
            updateJittaData();
            return false;
        }
    }

    public List<SymbolPayload> getAllWatchlist() {
        return watchlistRepository.getAll();
    }

    public List<String> getAllSymbol() {
        return watchlistRepository.getAllSymbolNotInAssets();
    }

    public void updateJittaData() {
        LOG.info("Start update Jitta Data");
        List<SymbolPayload> symbolPayloads = watchlistRepository.getAllFilterLastUpdateJitta();
        symbolPayloads.forEach(processJittaUtils::getJittaScore);
    }

    public boolean deleteWatchlist(UUID id) {
        return watchlistRepository.deleteWatchlist(id) != 0;
    }
}
