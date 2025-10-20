package hu.renew.main.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LaptopResponse {

    private Integer id;
    private String gyarto;
    private String tipus;
    private Integer kijelzo;
    private Integer memoria;
    private Integer merevlemez;
    private String videoVezerlo;
    private Integer ar;
    private Integer db;

    private String processorName;
    private String operatingSystemName;
}

