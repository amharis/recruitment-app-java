package fi.epassi.recruitment.bookstore;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookstoreDto {
    @NotBlank
    private String address;

    @NotBlank
    private String countryCode;

    @NotBlank
    private String city;

    @NotBlank
    private String storeCode;
}
