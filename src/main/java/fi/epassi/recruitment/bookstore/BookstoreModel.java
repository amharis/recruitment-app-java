package fi.epassi.recruitment.bookstore;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;

import java.math.BigDecimal;
import java.util.UUID;

import static java.sql.Types.VARCHAR;


@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Table( name = "bookstore" )
public class BookstoreModel {
    @NotBlank
    private String address;

    @NotBlank
    private String countryCode;

    @NotBlank
    private String city;

    @Id
    @NotBlank
    private String storeCode;
}