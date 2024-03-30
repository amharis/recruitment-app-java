package fi.epassi.recruitment.copy;

import fi.epassi.recruitment.book.BookDto;
import fi.epassi.recruitment.book.BookModel;
import fi.epassi.recruitment.book.BookRepository;
import fi.epassi.recruitment.book.BookService;
import fi.epassi.recruitment.exception.BookNotFoundException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.awt.print.Book;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor

public class CopyService {
    private final CopyRepository copyRepository;
    private final BookRepository bookRepository;

    public UUID createCopy(CopyDto copyDto) {
        CopyModel copyModel = toCopyModel(copyDto);
        // TODO : can we automate linking via JPA ?
        var existingBook = bookRepository.findByIsbn(copyDto.getIsbn());
        if (existingBook.isEmpty()) {
            throw new BookNotFoundException("Invalid isbn for copy record");
        }

        copyModel.setBook(existingBook.get());
        var savedCopyRecord = copyRepository.save(copyModel);
        return savedCopyRecord.getIsbn();
    }

    private static CopyModel toCopyModel(CopyDto copyDto) {
        return CopyModel.builder()
                .isbn(copyDto.getIsbn())
                .author(copyDto.getAuthor())
                .title(copyDto.getTitle())
                .copies(copyDto.getCopies())
                .build();
    }

    private static CopyDto toCopyDto(CopyModel copyModel) {
        return CopyDto.builder()
                .isbn(copyModel.getIsbn())
                .author(copyModel.getAuthor())
                .title(copyModel.getTitle())
                .copies(copyModel.getCopies())
                .build();
    }

}
