package hu.renew.main.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import hu.renew.main.dto.LaptopRequest;
import hu.renew.main.dto.LaptopResponse;
import hu.renew.main.mapper.LaptopMapper;
import hu.renew.main.model.Laptop;
import hu.renew.main.model.OperatingSystem;
import hu.renew.main.model.Processor;
import hu.renew.main.repository.LaptopRepository;
import hu.renew.main.repository.OperatingSystemRepository;
import hu.renew.main.repository.ProcessorRepository;
import lombok.RequiredArgsConstructor;

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

        // 🔧 Ha nincs megadva videovezérlő → alapértelmezett
        if (newLaptop.getVideoVezerlo() == null || newLaptop.getVideoVezerlo().isBlank()) {
            newLaptop.setVideoVezerlo("Integrált GPU");
        }

        Laptop saved = laptopRepository.save(newLaptop);

        return laptopMapper.toResponse(saved);
    }
    
    public LaptopResponse update(Integer id, LaptopRequest request) {
        Laptop entity = laptopRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Laptop nem található ID: " + id));

        Processor proc = processorRepository.findById(request.getProcessorId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Processzor nem található"));

        OperatingSystem os = operatingSystemRepository.findById(request.getOperatingSystemId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Operációs rendszer nem található"));

        entity.setGyarto(request.getGyarto());
        entity.setTipus(request.getTipus());
        entity.setKijelzo(request.getKijelzo());
        entity.setMemoria(request.getMemoria());
        entity.setMerevlemez(request.getMerevlemez());
        entity.setVideoVezerlo(request.getVideoVezerlo());
        entity.setAr(request.getAr());
        entity.setDb(request.getDb());
        entity.setProcessor(proc);
        entity.setOperatingSystem(os);

        Laptop updated = laptopRepository.save(entity);

        return laptopMapper.toResponse(updated);
    }


    public void delete(Integer id) {
        if (!laptopRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Laptop nem található");
        }
        laptopRepository.deleteById(id);
    }
}


