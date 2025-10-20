package hu.renew.main.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// DTO osztály új Laptop létrehozásához vagy módosításához

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LaptopRequest {

    @NotBlank(message = "A gyártó megadása kötelező!")
    @Size(max = 100, message = "A gyártó neve legfeljebb 100 karakter lehet.")
    private String gyarto;

    @NotBlank(message = "A típus megadása kötelező!")
    @Size(max = 100, message = "A típus neve legfeljebb 100 karakter lehet.")
    private String tipus;

    @Positive(message = "A kijelző mérete pozitív szám kell legyen.")
    private Integer kijelzo;

    @Positive(message = "A memória mérete pozitív szám kell legyen.")
    private Integer memoria;

    @Positive(message = "A merevlemez mérete pozitív szám kell legyen.")
    private Integer merevlemez;

    @Size(max = 100, message = "A videóvezérlő neve legfeljebb 100 karakter lehet.")
    private String videoVezerlo;

    @PositiveOrZero(message = "Az ár nem lehet negatív.")
    private Integer ar;

    @Min(value = 0, message = "A készlet darabszáma nem lehet negatív.")
    private Integer db;

    @NotNull(message = "A processzor azonosító megadása kötelező.")
    private Integer processorId;

    @NotNull(message = "Az operációs rendszer azonosító megadása kötelező.")
    private Integer operatingSystemId;
}

