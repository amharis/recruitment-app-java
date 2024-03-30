package fi.epassi.recruitment.copy;

import fi.epassi.recruitment.BaseIntegrationTest;
import fi.epassi.recruitment.book.BookDto;
import fi.epassi.recruitment.book.BookModel;
import fi.epassi.recruitment.book.BookRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static java.math.BigDecimal.TEN;
import static org.hamcrest.Matchers.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class CopyControllerTest extends BaseIntegrationTest {
    private static final String BASE_PATH_V1_COPIES = "/api/v1/copies";
    private static final String AUTHOR = "author";
    private static final String TITLE = "title";
    private static final String BASE_PATH_V1_BOOK_BY_ISBN = BASE_PATH_V1_COPIES + "/{isbn}";

    private static final BookModel BOOK_HOBBIT = BookModel.builder()
            .isbn(UUID.fromString("66737096-39ef-4a7c-aa4a-9fd018c14178"))
            .title("The Hobbit")
            .author("J.R.R Tolkien")
            .price(TEN)
            .build();

    private static final BookModel BOOK_FELLOWSHIP = BookModel.builder()
            .isbn(UUID.fromString("556aa37d-ef9c-45d3-ba4a-a792c123208a"))
            .title("The Fellowship of the Rings")
            .author("J.R.R Tolkien")
            .price(TEN)
            .build();

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CopyRepository copyRepository;

    @Test
    @SneakyThrows
    void testCreateCopiesRecordForExistingBook() {
        // Setup
        var bookSaved = bookRepository.save(BOOK_HOBBIT);
        var copyRecordDto = CopyDto.builder().isbn(BOOK_HOBBIT.getIsbn()).title(BOOK_HOBBIT.getTitle())
                .author(BOOK_HOBBIT.getAuthor()).copies(10l).build();
        var copyRecordDtoJson = mapper.writeValueAsString(copyRecordDto);

        // When
        var requestUrl = getEndpointUrl(BASE_PATH_V1_COPIES);
        var request = post(requestUrl).contentType(APPLICATION_JSON).content(copyRecordDtoJson);
        var response = mvc.perform(request);

        // Then
        System.out.println("**** Response: " + response);
        var expectedIsbn = BOOK_HOBBIT.getIsbn();
        response.andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.response", is(expectedIsbn.toString())));
    }

    @Test
    @SneakyThrows
    void testCreateCopiesRecordForNonExistingBook() {
        // Setup
        var nonExistentIsbn = UUID.fromString("12345678-ef9c-45d3-ba4a-a792c123208a");
        var copyRecordDto = CopyDto.builder().isbn(nonExistentIsbn).title(BOOK_HOBBIT.getTitle())
                .author(BOOK_HOBBIT.getAuthor()).copies(10l).build();
        var copyRecordDtoJson = mapper.writeValueAsString(copyRecordDto);

        // When
        var requestUrl = getEndpointUrl(BASE_PATH_V1_COPIES);
        var request = post(requestUrl).contentType(APPLICATION_JSON).content(copyRecordDtoJson);
        var response = mvc.perform(request);

        // Then
        System.out.println("**** Response: " + response);
        response.andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.detail", containsStringIgnoringCase("No book found with ISBN")));
    }

}
