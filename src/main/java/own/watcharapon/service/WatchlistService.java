package own.watcharapon.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Service;
import own.watcharapon.payload.SymbolDataPayload;
import own.watcharapon.payload.SymbolPayload;
import own.watcharapon.repository.WatchlistRepository;
import own.watcharapon.utils.ProcessJittaUtils;

import java.util.List;

import static own.watcharapon.utils.SymbolDataUtils.checkSymbol;

@Service
public class WatchlistService {
    private static final Logger LOG = LoggerFactory.getLogger(ProcessJittaUtils.class);
    private final WatchlistRepository watchlistRepository;
    private final ProcessJittaUtils processJittaUtils;

    public WatchlistService(WatchlistRepository watchlistRepository, ProcessJittaUtils processJittaUtils) {
        this.watchlistRepository = watchlistRepository;
        this.processJittaUtils = processJittaUtils;
    }

    public boolean checkSymbolAndSave(String symbol) {
        SymbolDataPayload symbolDataPayload = checkSymbol(symbol);
        if (symbolDataPayload == null)
            return true;
        else {
            symbolDataPayload.setSymbol(symbol);
            watchlistRepository.saveSymbol(symbolDataPayload);
            return false;
        }
    }

    public List<SymbolPayload> getAllWatchlist() {
        return watchlistRepository.getAll();
    }

    public void updateJittaData() {
        LOG.info("Start update Jitta Data");
        List<SymbolPayload> symbolPayloads = watchlistRepository.getAllFilterLastUpdateJitta();
        symbolPayloads.forEach(processJittaUtils::getJittaScore);
    }
}
