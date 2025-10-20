package hu.renew.main.service;

import hu.renew.main.dto.LaptopRequest;
import hu.renew.main.dto.LaptopResponse;
import hu.renew.main.mapper.LaptopMapper;
import hu.renew.main.model.Laptop;
import hu.renew.main.model.Processor;
import hu.renew.main.model.OperatingSystem;
import hu.renew.main.repository.LaptopRepository;
import hu.renew.main.repository.ProcessorRepository;
import hu.renew.main.repository.OperatingSystemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class LaptopService {

    private final LaptopRepository laptopRepository;
    private final ProcessorRepository processorRepository;
    private final OperatingSystemRepository operatingSystemRepository;
    private final LaptopMapper laptopMapper;

    public Page<LaptopResponse> getAll(Pageable pageable) {
        return laptopRepository.findAll(pageable).map(laptopMapper::toResponse);
    }

    public Page<LaptopResponse> getByManufacturer(String gyarto, Pageable pageable) {
        return laptopRepository.findByGyartoIgnoreCase(gyarto, pageable).map(laptopMapper::toResponse);
    }

    public LaptopResponse getById(Integer id) {
        Laptop laptop = laptopRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Laptop nem található"));
        return laptopMapper.toResponse(laptop);
    }

    public LaptopResponse create(LaptopRequest req) {
        Processor proc = processorRepository.findById(req.getProcessorId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Processzor nem található"));

        OperatingSystem os = operatingSystemRepository.findById(req.getOperatingSystemId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Operációs rendszer nem található"));

        Laptop newLaptop = laptopMapper.toEntity(req, proc, os);
        Laptop saved = laptopRepository.save(newLaptop);

        return laptopMapper.toResponse(saved);
    }

    public void delete(Integer id) {
        if (!laptopRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Laptop nem található");
        }
        laptopRepository.deleteById(id);
    }
}


