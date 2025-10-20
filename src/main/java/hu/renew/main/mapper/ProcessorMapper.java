package hu.renew.main.mapper;

import hu.renew.main.dto.ProcessorResponse;
import hu.renew.main.model.Processor;
import org.springframework.stereotype.Component;

@Component
public class ProcessorMapper {

    public ProcessorResponse toResponse(Processor entity) {
        if (entity == null) return null;

        return ProcessorResponse.builder()
                .id(entity.getId())
                .gyarto(entity.getGyarto())
                .tipus(entity.getTipus())
                .build();
    }
}

