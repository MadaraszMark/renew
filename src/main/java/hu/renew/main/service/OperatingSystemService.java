package hu.renew.main.service;

import hu.renew.main.dto.OperatingSystemResponse;
import hu.renew.main.mapper.OperatingSystemMapper;
import hu.renew.main.repository.OperatingSystemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OperatingSystemService {

    private final OperatingSystemRepository operatingSystemRepository;
    private final OperatingSystemMapper operatingSystemMapper;

    public Page<OperatingSystemResponse> getAll(Pageable pageable) {
        return operatingSystemRepository.findAll(pageable).map(operatingSystemMapper::toResponse);
    }

    public Page<OperatingSystemResponse> getByName(String nev, Pageable pageable) {
        return operatingSystemRepository.findByNevContainingIgnoreCase(nev, pageable)
                .map(operatingSystemMapper::toResponse);
    }
}

