package fi.epassi.recruitment.copy;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CopyDto {

    @NotNull
    private UUID isbn;

    @NotBlank
    private String title;

    @NotBlank
    private String author;

    @NotBlank
    private String storeCode;

    @NotNull
    @DecimalMin(value = "1", message = "Quantity must be more than 0")
    private Long copies;

}
