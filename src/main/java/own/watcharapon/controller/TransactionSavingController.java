package own.watcharapon.controller;


import org.springframework.web.bind.annotation.*;
import own.watcharapon.entity.TransactionCashEntity;
import own.watcharapon.entity.TransactionSavingEntity;
import own.watcharapon.payload.SavingSummaryPayload;
import own.watcharapon.service.TransactionCashService;
import own.watcharapon.service.TransactionSavingService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/trsaving")
@CrossOrigin(origins = "${frontend.url}")
public class TransactionSavingController {
    private final TransactionSavingService transactionSavingService;

    public TransactionSavingController(TransactionSavingService transactionSavingService) {
        this.transactionSavingService = transactionSavingService;
    }


    @PostMapping
    public void save(@RequestBody TransactionSavingEntity transactionCashEntity) {
        transactionSavingService.save(transactionCashEntity);
    }

    @DeleteMapping
    public void delete(@RequestParam("id") UUID id) {
        transactionSavingService.delete(id);
    }

    @GetMapping
    public List<TransactionSavingEntity> getAll() {
        return transactionSavingService.getAll();
    }

    @GetMapping("/summary")
    public List<SavingSummaryPayload> getSumGroupApplication() {
        return transactionSavingService.getSumGroupApplication();
    }
}
