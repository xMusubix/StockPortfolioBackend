package own.watcharapon.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import own.watcharapon.payload.AssetsPayload;
import own.watcharapon.payload.AssetsDashboardPayload;
import own.watcharapon.payload.SummaryAssetsData;
import own.watcharapon.service.AssetsService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/assets")
@CrossOrigin(origins = "${frontend.url}")
public class AssetsController {

    private final AssetsService assetsService;

    public AssetsController(AssetsService assetsService) {
        this.assetsService = assetsService;
    }

    @PostMapping("/save")
    public ResponseEntity<String> saveSymbol(@RequestBody List<String> symbolList) {
        assetsService.saveSymbol(symbolList);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/update-target")
    public ResponseEntity<String> updateTarget(@RequestParam("id") UUID id, @RequestParam("target") Double target) {
        assetsService.updateTarget(id, target);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/load")
    public ResponseEntity<List<AssetsPayload>> getAll() {
        return ResponseEntity.ok().body(assetsService.getAllAssets());
    }

    @GetMapping("/summary")
    public ResponseEntity<SummaryAssetsData> getSummaryAssetsData() {
        return ResponseEntity.ok().body(assetsService.getSummaryAssetsData());
    }

    @GetMapping("/dashboard")
    public ResponseEntity<AssetsDashboardPayload> getDashboardDetail() {
        return ResponseEntity.ok().body(assetsService.getDashboardDetail());
    }
}
