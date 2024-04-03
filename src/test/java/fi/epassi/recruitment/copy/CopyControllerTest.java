package fi.epassi.recruitment.copy;

import fi.epassi.recruitment.BaseIntegrationTest;
import fi.epassi.recruitment.book.BookDto;
import fi.epassi.recruitment.book.BookModel;
import fi.epassi.recruitment.book.BookRepository;
import fi.epassi.recruitment.bookstore.BookstoreDto;
import fi.epassi.recruitment.bookstore.BookstoreModel;
import fi.epassi.recruitment.bookstore.BookstoreRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static java.math.BigDecimal.TEN;
import static org.hamcrest.Matchers.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class CopyControllerTest extends BaseIntegrationTest {
    private static final String BASE_PATH_V1_COPIES = "/api/v1/copies";
    private static final String AUTHOR = "author";
    private static final String TITLE = "title";
    private static final String BASE_PATH_V1_COPIES_BY_ISBN = BASE_PATH_V1_COPIES + "/{isbn}";

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

    private static final String STORE_CODE_ESPOO_FIN = "ESPOO-FIN";
    private static final String STORE_CODE_TURKU_FIN = "TURKU-FIN";
    private static final BookstoreModel BOOKSTORE_ESPOO_FIN = BookstoreModel.builder().storeCode(STORE_CODE_ESPOO_FIN).countryCode("FIN")
            .address("Espoo, Finland").city("ESPOO").build();

    private static final BookstoreModel BOOKSTORE_TURKU_FIN = BookstoreModel.builder().storeCode(STORE_CODE_TURKU_FIN).countryCode("FIN")
            .address("Turku, Finland").city("TURKU").build();
    private static final CopyModel FELLOWSHIP_BOOK_COPIES = CopyModel.builder().isbn(BOOK_FELLOWSHIP.getIsbn()).title(BOOK_FELLOWSHIP.getTitle())
            .author(BOOK_FELLOWSHIP.getAuthor()).copies(10l).storeCode(STORE_CODE_ESPOO_FIN).build();

    private static final CopyModel HOBBIT_BOOK_COPIES = CopyModel.builder().isbn(BOOK_HOBBIT.getIsbn()).title(BOOK_HOBBIT.getTitle())
            .author(BOOK_HOBBIT.getAuthor()).copies(10l).storeCode(STORE_CODE_ESPOO_FIN).build();

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CopyRepository copyRepository;

    @Autowired
    private BookstoreRepository bookstoreRepository;

    @Test
    @SneakyThrows
    void testCreateCopiesRecordForExistingBook() {
        // Setup
        var bookSaved = bookRepository.save(BOOK_HOBBIT);
        var bookStoreSaved = bookstoreRepository.save(BOOKSTORE_ESPOO_FIN);
        var copyRecordDtoJson = mapper.writeValueAsString(HOBBIT_BOOK_COPIES);

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
        var bookStoreSaved = bookstoreRepository.save(BOOKSTORE_ESPOO_FIN);
        var nonExistentIsbn = UUID.fromString("12345678-ef9c-45d3-ba4a-a792c123208a");
        var copyRecordDto = CopyDto.builder().isbn(nonExistentIsbn).title(BOOK_HOBBIT.getTitle())
                .author(BOOK_HOBBIT.getAuthor()).copies(10l).storeCode(bookStoreSaved.getStoreCode()).build();
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

    @Test
    @SneakyThrows
    void testGetCopyRecordByIsbn() {
        // Setup
        var bookSaved1 = bookRepository.save(BOOK_HOBBIT);
        var bookStoreSaved = bookstoreRepository.save(BOOKSTORE_ESPOO_FIN);
        HOBBIT_BOOK_COPIES.setBook(bookSaved1);
        var savedCopiesRecord = copyRepository.save(HOBBIT_BOOK_COPIES);

        // When
        var requestUrl = getEndpointUrl(BASE_PATH_V1_COPIES_BY_ISBN);
        var request = get(requestUrl, BOOK_HOBBIT.getIsbn().toString()).contentType(APPLICATION_JSON);
        var response = mvc.perform(request);

        // Then
        response.andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.response.isbn", is(BOOK_HOBBIT.getIsbn().toString())));
    }

    @Test
    @SneakyThrows
    void testGetCopiesByAuthor() {
        // Setup
        var bookStoreSaved1 = bookstoreRepository.save(BOOKSTORE_ESPOO_FIN);
        var bookStoreSaved2 = bookstoreRepository.save(BOOKSTORE_TURKU_FIN);

        var bookSaved1 = bookRepository.save(BOOK_HOBBIT);
        HOBBIT_BOOK_COPIES.setBook(bookSaved1);
        var savedCopiesRecord1 = copyRepository.save(HOBBIT_BOOK_COPIES);

        var bookSaved2 = bookRepository.save(BOOK_FELLOWSHIP);
        FELLOWSHIP_BOOK_COPIES.setBook(bookSaved2);
        var savedCopiesRecord2 = copyRepository.save(FELLOWSHIP_BOOK_COPIES);

        // When
        var requestUrl = getEndpointUrl(BASE_PATH_V1_COPIES);
        var request = get(requestUrl).queryParam(AUTHOR, BOOK_HOBBIT.getAuthor()).contentType(APPLICATION_JSON);
        var response = mvc.perform(request);

        // Then
        response.andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.response.[*].title", containsInAnyOrder(BOOK_HOBBIT.getTitle(), BOOK_FELLOWSHIP.getTitle())));

    }


}
