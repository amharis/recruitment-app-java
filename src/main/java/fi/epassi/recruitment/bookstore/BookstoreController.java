package fi.epassi.recruitment.bookstore;

import fi.epassi.recruitment.api.ApiResponse;
import fi.epassi.recruitment.book.BookDto;
import fi.epassi.recruitment.book.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/bookstores", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
public class BookstoreController {
    private final BookstoreService bookstoreService;

    @GetMapping
    ApiResponse<List<BookstoreDto>> getBookstores(
            @RequestParam(value = "storecode", required = false) String storeCode
    ) {
        return ApiResponse.ok(bookstoreService.getBookstores());
    }

    @PostMapping
    ApiResponse<String> createBookstore(@RequestBody @Validated BookstoreDto bookstoreDto) {
        var storeCode = bookstoreService.createBookstore(bookstoreDto);
        return ApiResponse.ok(storeCode);
    }

    @PutMapping
    ApiResponse<String> updateBookstore(@RequestBody @Validated BookstoreDto bookstoreDto) {
        var storeCode = bookstoreService.updateBookstore(bookstoreDto);
        return ApiResponse.ok(storeCode);
    }

    @GetMapping("/{storecode}")
    ApiResponse<BookstoreDto> getBookstoreBycode(@PathVariable("storecode") @Validated String storeCode) {
        return ApiResponse.ok(bookstoreService.getBookstore(storeCode));
    }

    @DeleteMapping("/{storecode}")
    ApiResponse<Void> deleteBookByIsbn(@PathVariable("storecode") @Validated String storeCode) {
        bookstoreService.deleteBookstore(storeCode);
        return ApiResponse.ok();
    }


}
