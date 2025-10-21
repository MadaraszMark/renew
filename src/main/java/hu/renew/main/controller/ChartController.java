package hu.renew.main.controller;

import hu.renew.main.service.ChartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/chart")
@RequiredArgsConstructor
public class ChartController {

    private final ChartService chartService;

    @GetMapping("/laptops-by-manufacturer")
    public ResponseEntity<List<Map<String, Object>>> getLaptopStats() {
        return ResponseEntity.ok(chartService.getLaptopStatsByManufacturer());
    }
}

