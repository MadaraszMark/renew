package hu.renew.main.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import hu.renew.main.dto.ProcessorResponse;
import hu.renew.main.mapper.ProcessorMapper;
import hu.renew.main.repository.ProcessorRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProcessorService {

    private final ProcessorRepository processorRepository;
    private final ProcessorMapper processorMapper;

    public Page<ProcessorResponse> getAll(Pageable pageable) {
        return processorRepository.findAll(pageable).map(processorMapper::toResponse);
    }

    public Page<ProcessorResponse> getByManufacturer(String gyarto, Pageable pageable) {
        return processorRepository.findByGyartoIgnoreCase(gyarto, pageable).map(processorMapper::toResponse);
    }
}

