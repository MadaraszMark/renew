package hu.renew.main.mapper;

import hu.renew.main.dto.LaptopRequest;
import hu.renew.main.dto.LaptopResponse;
import hu.renew.main.model.Laptop;
import hu.renew.main.model.Processor;
import hu.renew.main.model.OperatingSystem;
import org.springframework.stereotype.Component;

@Component
public class LaptopMapper {

    //Entity → DTO
    public LaptopResponse toResponse(Laptop entity) {
        if (entity == null) return null;

        return LaptopResponse.builder()
                .id(entity.getId())
                .gyarto(entity.getGyarto())
                .tipus(entity.getTipus())
                .kijelzo(entity.getKijelzo())
                .memoria(entity.getMemoria())
                .merevlemez(entity.getMerevlemez())
                .videoVezerlo(entity.getVideoVezerlo())
                .ar(entity.getAr())
                .db(entity.getDb())
                .processorName(
                        entity.getProcessor() != null
                                ? entity.getProcessor().getGyarto() + " " + entity.getProcessor().getTipus()
                                : null
                )
                .operatingSystemName(
                        entity.getOperatingSystem() != null
                                ? entity.getOperatingSystem().getNev()
                                : null
                )
                .build();
    }

    //DTO → Entity
    public Laptop toEntity(LaptopRequest dto, Processor proc, OperatingSystem os) {
        if (dto == null) return null;

        return Laptop.builder()
                .gyarto(dto.getGyarto())
                .tipus(dto.getTipus())
                .kijelzo(dto.getKijelzo())
                .memoria(dto.getMemoria())
                .merevlemez(dto.getMerevlemez())
                .videoVezerlo(dto.getVideoVezerlo())
                .ar(dto.getAr())
                .db(dto.getDb())
                .processor(proc)
                .operatingSystem(os)
                .build();
    }
}

