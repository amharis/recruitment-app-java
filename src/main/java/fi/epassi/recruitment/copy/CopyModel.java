package fi.epassi.recruitment.copy;

import fi.epassi.recruitment.book.BookModel;
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
@Table(name = "Copies")
public class CopyModel {

    @Id
    @Column(name = "id")
    private UUID isbn;

    @NotBlank
    private String title;

    @NotBlank
    private String author;

    @NotNull
    @DecimalMin(value = "1", message = "Quantity must be more than 1")
    private Long copies;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @MapsId
    @JoinColumn(name = "id")
    private BookModel book;

    public void setBook(BookModel book) {
        this.book = book;
        this.isbn = book.getIsbn();
    }
}