package hu.renew.main.mapper;

import hu.renew.main.dto.OperatingSystemResponse;
import hu.renew.main.model.OperatingSystem;
import org.springframework.stereotype.Component;

@Component
public class OperatingSystemMapper {

    public OperatingSystemResponse toResponse(OperatingSystem entity) {
        if (entity == null) return null;

        return OperatingSystemResponse.builder()
                .id(entity.getId())
                .nev(entity.getNev())
                .build();
    }
}

