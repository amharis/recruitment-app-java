package fi.epassi.recruitment.book;

import fi.epassi.recruitment.exception.BookNotFoundException;
import java.util.List;
import java.util.UUID;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.cache.support.SimpleCacheManager;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    public static final int PAGE_SIZE = 10;
    @CachePut("books")
    public UUID createBook(BookDto bookDto) {
        BookModel bookModel = toBookModel(bookDto);
        var savedBook = bookRepository.save(bookModel);
        return savedBook.getIsbn();
    }

    @CacheEvict("books")
    public void deleteBookWithIsbn(@NonNull UUID isbn) {
        bookRepository.deleteById(isbn);
    }

    public BookDto getBookByIsbn(@NonNull UUID isbn) throws BookNotFoundException {
        return bookRepository.findByIsbn(isbn)
            .map(BookService::toBookDto)
            .orElseThrow(() -> new BookNotFoundException(isbn.toString()));
    }


    @Cacheable("books")
    public List<BookDto> getBooks(String author, String title) {
        if (StringUtils.isNotBlank(author) && StringUtils.isNotBlank(title)) {
            return bookRepository.findByAuthorAndTitle(author, title).stream().map(BookService::toBookDto).toList();
        } else if (StringUtils.isNotBlank(author) && StringUtils.isBlank(title)) {
            return bookRepository.findByAuthor(author).stream().map(BookService::toBookDto).toList();
        } else if (StringUtils.isNotBlank(title) && StringUtils.isBlank(author)) {
            return bookRepository.findByTitle(title).stream().map(BookService::toBookDto).toList();
        }

        return bookRepository.findAll().stream().map(BookService::toBookDto).toList();
    }

    public BookApiPaginatedResponse getBooksWithPaging(String author, String title, int pageNumber, int pageSize) {
        var pageRequest = PageRequest.of(pageNumber, pageSize);

        if (StringUtils.isNotBlank(author) && StringUtils.isNotBlank(title)) {
            final Page<BookModel> pageQueryResponse = bookRepository.findByAuthorAndTitle(author, title, pageRequest);
            return toBookApiPaginatedResponse(pageQueryResponse);

        } else if (StringUtils.isNotBlank(author) && StringUtils.isBlank(title)) {
            final Page<BookModel> pageQueryResponse = bookRepository.findByAuthor(author, pageRequest);
            return toBookApiPaginatedResponse(pageQueryResponse);

        } else if (StringUtils.isNotBlank(title) && StringUtils.isBlank(author)) {
            final Page<BookModel> pageQueryResponse = bookRepository.findByTitle(title, pageRequest);
            return toBookApiPaginatedResponse(pageQueryResponse);
        }

        final Page<BookModel> pageQueryResponse = bookRepository.findAll(pageRequest);
        return toBookApiPaginatedResponse(pageQueryResponse);
    }

    private static BookApiPaginatedResponse toBookApiPaginatedResponse(Page<BookModel> page) {
        return BookApiPaginatedResponse.builder()
                .books(page.getContent().stream().map(BookService::toBookDto).toList())
                .currentPage(page.getNumber())
                .totalItems(page.getTotalElements())
                .totalPages(page.getTotalPages()).build();
    }
    @CachePut("books")
    public UUID updateBook(BookDto bookDto) {

        if (bookRepository.findByIsbn(bookDto.getIsbn()).isPresent()) {
            var bookModel = toBookModel(bookDto);
            var savedBook = bookRepository.save(bookModel);
            return savedBook.getIsbn();
        }

        throw new BookNotFoundException(bookDto.getIsbn().toString());
    }

    private static BookModel toBookModel(BookDto bookDto) {
        return BookModel.builder()
            .isbn(bookDto.getIsbn())
            .author(bookDto.getAuthor())
            .title(bookDto.getTitle())
            .price(bookDto.getPrice())
            .build();
    }

    private static BookDto toBookDto(BookModel bookModel) {
        return BookDto.builder()
            .isbn(bookModel.getIsbn())
            .author(bookModel.getAuthor())
            .title(bookModel.getTitle())
            .price(bookModel.getPrice())
            .build();
    }
}
