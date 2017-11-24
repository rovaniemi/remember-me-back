package remember.repository;

import org.springframework.transaction.annotation.Transactional;
import remember.domain.inertances.Book;

@Transactional
public interface BookRepository extends TipBaseRepository<Book> {
}
