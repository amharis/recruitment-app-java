package fi.epassi.recruitment.exception;

import static org.springframework.http.HttpStatus.NOT_FOUND;

public class BookstoreNotFoundException extends ApplicationException {

    public BookstoreNotFoundException(final String storeCode) {
        super(NOT_FOUND, "No bookstore found with code {%s}".formatted(storeCode));
    }
}
