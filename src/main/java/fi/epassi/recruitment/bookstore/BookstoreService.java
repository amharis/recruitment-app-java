package fi.epassi.recruitment.bookstore;

import fi.epassi.recruitment.book.BookDto;
import fi.epassi.recruitment.book.BookModel;
import fi.epassi.recruitment.book.BookRepository;
import fi.epassi.recruitment.book.BookService;
import fi.epassi.recruitment.exception.BookNotFoundException;
import fi.epassi.recruitment.exception.BookstoreNotFoundException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookstoreService {

    private final BookstoreRepository bookstoreRepository;

    public String createBookstore(BookstoreDto bookstoreDto) {
        BookstoreModel bookstoreModel = toBookstoreModel(bookstoreDto);
        var savedBookstore = bookstoreRepository.save(bookstoreModel);
        return savedBookstore.getStoreCode();
    }

    public BookstoreDto getBookstore(String storeCode) {
        var bookStoreOpt = bookstoreRepository.findByStoreCode(storeCode);
        if (bookStoreOpt.isPresent()) {
            return toBookstoreDto(bookStoreOpt.get());
        }

        throw new BookstoreNotFoundException(storeCode);
    }

    public List<BookstoreDto> getBookstores() {
        return bookstoreRepository.findAll().stream().map(BookstoreService::toBookstoreDto).toList();
    }

    public String updateBookstore(BookstoreDto bookstoreDto) {
        var bookstoreOpt = bookstoreRepository.findByStoreCode(bookstoreDto.getStoreCode());
        if (bookstoreOpt.isPresent()) {
            var savedBookstore = bookstoreRepository.save(bookstoreOpt.get());
            return savedBookstore.getStoreCode();
        }

        throw new BookstoreNotFoundException(bookstoreDto.getStoreCode());
    }

    public void deleteBookstore(String storeCode) {
        bookstoreRepository.deleteById(storeCode);
    }

    private static BookstoreModel toBookstoreModel(BookstoreDto bookstoreDto) {
        return BookstoreModel.builder()
                .address(bookstoreDto.getAddress())
                .storeCode(bookstoreDto.getStoreCode())
                .city(bookstoreDto.getCity())
                .countryCode(bookstoreDto.getCountryCode())
                .build();
    }

    private static BookstoreDto toBookstoreDto(BookstoreModel bookstoreModel) {
        return BookstoreDto.builder()
                .address(bookstoreModel.getAddress())
                .storeCode(bookstoreModel.getStoreCode())
                .city(bookstoreModel.getCity())
                .countryCode(bookstoreModel.getCountryCode())
                .build();
    }
}
