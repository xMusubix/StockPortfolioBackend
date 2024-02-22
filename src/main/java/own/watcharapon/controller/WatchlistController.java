package own.watcharapon.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import own.watcharapon.payload.SymbolPayload;
import own.watcharapon.service.WatchlistService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/watchlist")
@CrossOrigin(origins = "${frontend.url}")
public class WatchlistController {

    private final WatchlistService watchlistService;

    public WatchlistController(WatchlistService watchlistService) {
        this.watchlistService = watchlistService;
    }

    @PostMapping("/save")
    public ResponseEntity<String> saveSymbol(@RequestParam("symbol") String symbol) {
        if (watchlistService.checkSymbolAndSave(symbol)) {
            return ResponseEntity.badRequest().body("Can't Find Symbol");
        } else return ResponseEntity.ok().build();
    }

    @GetMapping("/load")
    public ResponseEntity<List<SymbolPayload>> getAll() {
        return ResponseEntity.ok().body(watchlistService.getAllWatchlist());
    }

    @GetMapping("/update-jitta")
    public ResponseEntity<String> updateJittaData() {
        watchlistService.updateJittaData();
        return ResponseEntity.ok().body("Success");
    }
}
