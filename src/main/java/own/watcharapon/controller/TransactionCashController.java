package own.watcharapon.controller;


import org.springframework.web.bind.annotation.*;
import own.watcharapon.entity.TransactionCashEntity;
import own.watcharapon.service.TransactionCashService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/trc")
@CrossOrigin(origins = "${frontend.url}")
public class TransactionCashController {
    private final TransactionCashService transactionCashService;

    public TransactionCashController(TransactionCashService transactionCashService) {
        this.transactionCashService = transactionCashService;
    }

    @PostMapping
    public void save(@RequestBody TransactionCashEntity transactionCashEntity) {
        transactionCashService.save(transactionCashEntity);
    }

    @DeleteMapping
    public void delete(@RequestParam("id") UUID id) {
        transactionCashService.delete(id);
    }

    @GetMapping
    public List<TransactionCashEntity> getAll() {
        return transactionCashService.getAll();
    }
}
