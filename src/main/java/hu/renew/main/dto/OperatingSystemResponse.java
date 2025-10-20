package hu.renew.main.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OperatingSystemResponse {
	
    private Integer id;
    private String nev;
}

