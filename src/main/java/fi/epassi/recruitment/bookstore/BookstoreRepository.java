package fi.epassi.recruitment.bookstore;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookstoreRepository extends JpaRepository<BookstoreModel, String> {
    Optional<BookstoreModel> findByStoreCode(String code);

    //String deleteByStoreCode(String code);
}
