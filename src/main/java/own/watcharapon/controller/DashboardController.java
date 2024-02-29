package own.watcharapon.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import own.watcharapon.payload.AssetsDashboardPayload;
import own.watcharapon.payload.DashboardPayload;
import own.watcharapon.service.DashboardService;

@RestController
@RequestMapping("/api/v1/dashboard")
@CrossOrigin(origins = "${frontend.url}")
public class DashboardController {
    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping
    public ResponseEntity<DashboardPayload> getDashboardDetail() {
        return ResponseEntity.ok().body(dashboardService.getDashboardData());
    }
}
