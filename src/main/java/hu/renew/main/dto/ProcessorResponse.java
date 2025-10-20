package hu.renew.main.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProcessorResponse {
	
    private Integer id;
    private String gyarto;
    private String tipus;
}

