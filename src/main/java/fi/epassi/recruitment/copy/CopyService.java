package fi.epassi.recruitment.copy;

import fi.epassi.recruitment.book.BookDto;
import fi.epassi.recruitment.book.BookModel;
import fi.epassi.recruitment.book.BookRepository;
import fi.epassi.recruitment.book.BookService;
import fi.epassi.recruitment.bookstore.BookstoreModel;
import fi.epassi.recruitment.bookstore.BookstoreRepository;
import fi.epassi.recruitment.exception.BookNotFoundException;
import fi.epassi.recruitment.exception.BookstoreNotFoundException;
import fi.epassi.recruitment.exception.CopyRecordNotFoundException;
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
    private final BookstoreRepository bookstoreRepository;

    public UUID createCopy(CopyDto copyDto) {
        CopyModel copyModel = toCopyModel(copyDto);
        // TODO : can we automate linking via JPA ?
        linkToBookIfExistsOrThrowException(copyModel);
        checkBookstoreExists(copyModel);
        var savedCopyRecord = copyRepository.save(copyModel);
        return savedCopyRecord.getIsbn();
    }

    public CopyDto getCopiesByIsbn(@NonNull UUID isbn) {
        return copyRepository.findByIsbn(isbn)
                .map(CopyService::toCopyDto)
                .orElseThrow(() -> new CopyRecordNotFoundException(isbn.toString()));
    }

    public List<CopyDto> getCopyRecords(String author, String title) {
        if (StringUtils.isNotBlank(author) && StringUtils.isNotBlank(title)) {
            return copyRepository.findByAuthorAndTitle(author, title).stream().map(CopyService::toCopyDto).toList();
        } else if (StringUtils.isNotBlank(author) && StringUtils.isBlank(title)) {
            var list = copyRepository.findByAuthor(author).stream().map(CopyService::toCopyDto).toList();
            return list;
        } else if (StringUtils.isNotBlank(title) && StringUtils.isBlank(author)) {
            return copyRepository.findByTitle(title).stream().map(CopyService::toCopyDto).toList();
        }

        return copyRepository.findAll().stream().map(CopyService::toCopyDto).toList();
    }


    public UUID updateCopyRecord(CopyDto copyDto) {
        var existingCopyRecordOpt = copyRepository.findByIsbn(copyDto.getIsbn());
        if (existingCopyRecordOpt.isPresent()) {
            var copyModel = toCopyModel(copyDto);
            copyModel.setBook(existingCopyRecordOpt.get().getBook());
            checkBookstoreExists(copyModel);
            var savedRecord = copyRepository.save(copyModel);
            return savedRecord.getIsbn();
        }
        throw new CopyRecordNotFoundException(copyDto.getIsbn().toString());
    }

    private void linkToBookIfExistsOrThrowException(CopyModel copyRecord) {
        var existingBook = bookRepository.findByIsbn(copyRecord.getIsbn());
        if (existingBook.isEmpty()) {
            throw new BookNotFoundException("Invalid isbn for copy record");
        }
        copyRecord.setBook(existingBook.get());
    }

    private void checkBookstoreExists(CopyModel copyRecord) {
        var existingBook = bookstoreRepository.findByStoreCode(copyRecord.getStoreCode());
        if (existingBook.isEmpty()) {
            throw new BookstoreNotFoundException(copyRecord.getStoreCode());
        }
    }

    public static CopyModel toCopyModel(CopyDto copyDto) {
        return CopyModel.builder()
                .isbn(copyDto.getIsbn())
                .author(copyDto.getAuthor())
                .title(copyDto.getTitle())
                .copies(copyDto.getCopies())
                .storeCode(copyDto.getStoreCode())
                .build();
    }

    public static CopyDto toCopyDto(CopyModel copyModel) {
        return CopyDto.builder()
                .isbn(copyModel.getIsbn())
                .author(copyModel.getAuthor())
                .title(copyModel.getTitle())
                .copies(copyModel.getCopies()).storeCode(copyModel.getStoreCode())
                .build();
    }

}
