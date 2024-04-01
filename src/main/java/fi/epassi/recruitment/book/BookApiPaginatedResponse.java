package fi.epassi.recruitment.book;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookApiPaginatedResponse {

    private long totalItems;
    private int currentPage;
    private int totalPages;
    private List<BookDto> books;
}