package fi.epassi.recruitment.copy;

import fi.epassi.recruitment.book.BookModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CopyRepository extends JpaRepository<CopyModel, UUID> {

    Optional<CopyModel> findByIsbn(UUID isbn);

    List<CopyModel> findByTitle(String title);

    List<CopyModel> findByAuthor(String author);

    List<CopyModel> findByAuthorAndTitle(String author, String title);
}