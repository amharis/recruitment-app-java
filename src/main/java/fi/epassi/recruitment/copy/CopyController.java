package fi.epassi.recruitment.copy;

import fi.epassi.recruitment.api.ApiResponse;
import fi.epassi.recruitment.book.BookDto;
import fi.epassi.recruitment.book.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/copies", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
public class CopyController {

    private final CopyService copyService;
    @PostMapping
    ApiResponse<UUID> addBookCopies(@RequestBody @Validated CopyDto copyDto) {
        return ApiResponse.ok(copyService.createCopy(copyDto));
    }

}
