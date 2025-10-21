package hu.renew.main.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "gep")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Laptop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 100)
    private String gyarto;

    @Column(nullable = false, length = 100)
    private String tipus;

    private Integer kijelzo;
    
    private Integer memoria;
    
    private Integer merevlemez;

    @Column(length = 100, name = "videoezelo", nullable = false )
    private String videoVezerlo;

    private Integer ar;
    
    private Integer db;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "processzorid")
    private Processor processor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "oprendszerid")
    private OperatingSystem operatingSystem;
}

