package remember.repository;

import org.springframework.transaction.annotation.Transactional;
import remember.domain.Book;

@Transactional
public interface BookRepository extends TipBaseRepository<Book> {
}
