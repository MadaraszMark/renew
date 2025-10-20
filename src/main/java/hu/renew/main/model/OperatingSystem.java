package hu.renew.main.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "oprendszer")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OperatingSystem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 100)
    private String nev;
}

