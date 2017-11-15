package remember.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import remember.domain.Book;

public interface BookRepository extends JpaRepository<Book, Long> {

}
