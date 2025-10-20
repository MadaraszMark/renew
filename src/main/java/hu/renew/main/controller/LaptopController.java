package hu.renew.main.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import hu.renew.main.dto.LaptopRequest;
import hu.renew.main.dto.LaptopResponse;
import hu.renew.main.service.LaptopService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/laptops")
@RequiredArgsConstructor
@Validated
public class LaptopController {

    private final LaptopService laptopService;

    @GetMapping
    public Page<LaptopResponse> getAll(@RequestParam(defaultValue = "0") int page,@RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return laptopService.getAll(pageable);
    }

    @GetMapping("/manufacturer/{gyarto}")
    public Page<LaptopResponse> getByManufacturer(@PathVariable String gyarto,@RequestParam(defaultValue = "0") int page,@RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return laptopService.getByManufacturer(gyarto, pageable);
    }

    @GetMapping("/{id}")
    public LaptopResponse getById(@PathVariable Integer id) {
        return laptopService.getById(id);
    }

    @PostMapping
    public ResponseEntity<LaptopResponse> create(@Valid @RequestBody LaptopRequest request) {
        LaptopResponse created = laptopService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        laptopService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

