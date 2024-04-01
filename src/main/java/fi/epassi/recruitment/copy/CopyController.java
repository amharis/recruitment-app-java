package fi.epassi.recruitment.copy;

import fi.epassi.recruitment.api.ApiResponse;
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
@RequestMapping(path = "/api/v1/copies", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
public class CopyController {

    private final CopyService copyService;
    @PostMapping
    ApiResponse<UUID> addBookCopies(@RequestBody @Validated CopyDto copyDto) {
        return ApiResponse.ok(copyService.createCopy(copyDto));
    }

    @GetMapping("/{isbn}")
    ApiResponse<CopyDto> getCopyRecordByIsbn(@PathVariable("isbn") @Validated UUID isbn) {
        return ApiResponse.ok(copyService.getCopiesByIsbn(isbn));
    }

    @GetMapping
    ApiResponse<List<CopyDto>> getCopyRecords(
            @RequestParam(value = "author", required = false) String author,
            @RequestParam(value = "title", required = false) String title) {
        return ApiResponse.ok(copyService.getCopyRecords(author, title));
    }

}
