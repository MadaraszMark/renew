package hu.renew.main.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "processzor")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Processor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 100)
    private String gyarto;

    @Column(nullable = false, length = 100)
    private String tipus;
}

