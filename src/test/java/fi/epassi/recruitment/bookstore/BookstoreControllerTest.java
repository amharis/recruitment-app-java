package fi.epassi.recruitment.bookstore;

import fi.epassi.recruitment.BaseIntegrationTest;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class BookstoreControllerTest extends BaseIntegrationTest {
    private static final String BASE_PATH_V1_BOOKSTORE = "/api/v1/bookstores";

    private static final String BASE_PATH_V1_BOOKSTORE_STORECODE = BASE_PATH_V1_BOOKSTORE + "/{storecode}";

    private static final String STORE_CODE_ESPOO_FIN = "ESPOO-FIN";
    private static final String STORE_CODE_TURKU_FIN = "TURKU-FIN";
    private static final BookstoreModel BOOKSTORE_ESPOO_FIN = BookstoreModel.builder().storeCode(STORE_CODE_ESPOO_FIN).countryCode("FIN")
            .address("Espoo, Finland").city("ESPOO").build();

    private static final BookstoreModel BOOKSTORE_TURKU_FIN = BookstoreModel.builder().storeCode(STORE_CODE_TURKU_FIN).countryCode("FIN")
            .address("Turku, Finland").city("TURKU").build();

    @Autowired
    BookstoreRepository bookstoreRepository;
    @Test
    @SneakyThrows
    void testGetExistingBookStoreUsingCode() {
        // Setup
        var bookStoreSaved = bookstoreRepository.save(BOOKSTORE_ESPOO_FIN);

        // When
        var requestUrl = getEndpointUrl(BASE_PATH_V1_BOOKSTORE_STORECODE);
        var request = get(requestUrl, bookStoreSaved.getStoreCode()).contentType(APPLICATION_JSON);
        var response = mvc.perform(request);

        // Then
        response.andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.response.country_code", is("FIN")))
                .andExpect(jsonPath("$.response.city", is("ESPOO")))
                .andExpect(jsonPath("$.response.store_code", is("ESPOO-FIN")));
    }

    @Test
    @SneakyThrows
    void testGetAllBookstores() {
        // Setup
        var bookStoreSavedEspoo = bookstoreRepository.save(BOOKSTORE_ESPOO_FIN);
        var bookStoreSavedTurku = bookstoreRepository.save(BOOKSTORE_TURKU_FIN);

        // When
        var requestUrl = getEndpointUrl(BASE_PATH_V1_BOOKSTORE);
        var request = get(requestUrl).contentType(APPLICATION_JSON);
        var response = mvc.perform(request);

        // Then
        response.andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.response.[*].city", containsInAnyOrder( "TURKU", "ESPOO")))
                .andExpect(jsonPath("$.response.[*].store_code", containsInAnyOrder( "TURKU-FIN", "ESPOO-FIN")));
    }

}
