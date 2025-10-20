package hu.renew.main.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import hu.renew.main.dto.ProcessorResponse;
import hu.renew.main.service.ProcessorService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/processors")
@RequiredArgsConstructor
public class ProcessorController {

    private final ProcessorService processorService;

    @GetMapping
    public Page<ProcessorResponse> getAll(@RequestParam(defaultValue = "0") int page,@RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return processorService.getAll(pageable);
    }

    @GetMapping("/manufacturer/{gyarto}")
    public Page<ProcessorResponse> getByManufacturer(@PathVariable String gyarto,@RequestParam(defaultValue = "0") int page,@RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return processorService.getByManufacturer(gyarto, pageable);
    }
}

