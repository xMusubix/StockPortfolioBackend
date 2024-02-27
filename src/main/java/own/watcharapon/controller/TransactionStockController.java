package own.watcharapon.controller;


import org.springframework.web.bind.annotation.*;
import own.watcharapon.entity.TransactionCashEntity;
import own.watcharapon.entity.TransactionStockEntity;
import own.watcharapon.payload.TransactionStockPayload;
import own.watcharapon.service.TransactionCashService;
import own.watcharapon.service.TransactionStockService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/trs")
@CrossOrigin(origins = "${frontend.url}")
public class TransactionStockController {
    private final TransactionStockService transactionStockService;

    public TransactionStockController(TransactionStockService transactionStockService) {
        this.transactionStockService = transactionStockService;
    }

    @PostMapping
    public void save(@RequestBody TransactionStockPayload transactionStockPayload) {
        transactionStockService.save(transactionStockPayload);
    }

    @DeleteMapping
    public void delete(@RequestParam("id") UUID id, @RequestParam("marketSymbol") String marketSymbol) {
        transactionStockService.delete(id, marketSymbol);
    }

    @GetMapping("/get-by-symbol")
    public List<TransactionStockEntity> getAllByMarketSymbol(@RequestParam("marketSymbol") String marketSymbol) {
        return transactionStockService.getAllByMarketSymbol(marketSymbol);
    }
}
