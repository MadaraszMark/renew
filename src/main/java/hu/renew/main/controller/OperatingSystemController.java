package hu.renew.main.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import hu.renew.main.dto.OperatingSystemResponse;
import hu.renew.main.service.OperatingSystemService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/os")
@RequiredArgsConstructor
public class OperatingSystemController {

    private final OperatingSystemService operatingSystemService;

    @GetMapping
    public Page<OperatingSystemResponse> getAll(@RequestParam(defaultValue = "0") int page,@RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return operatingSystemService.getAll(pageable);
    }

    @GetMapping("/search")
    public Page<OperatingSystemResponse> searchByName(@RequestParam String nev,@RequestParam(defaultValue = "0") int page,@RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return operatingSystemService.getByName(nev, pageable);
    }
}
