package fi.epassi.recruitment.exception;

import static org.springframework.http.HttpStatus.NOT_FOUND;

public class CopyRecordNotFoundException extends ApplicationException {

    public CopyRecordNotFoundException(final String isbn) {
        super(NOT_FOUND, "No Copy record found with ISBN {%s}".formatted(isbn));
    }
}
