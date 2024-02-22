package own.watcharapon.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import own.watcharapon.payload.IndustryPayload;
import own.watcharapon.service.IndustryService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/industry")
@CrossOrigin(origins = "${frontend.url}")
public class IndustryController {
    private final IndustryService industryService;

    public IndustryController(IndustryService industryService) {
        this.industryService = industryService;
    }

    @GetMapping("/load")
    public ResponseEntity<List<IndustryPayload>> getAll() {
        return ResponseEntity.ok().body(industryService.getAllIndustry());
    }
}
