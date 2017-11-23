package remember.repository;

import javax.transaction.Transactional;
import remember.domain.Book;

@Transactional
public interface BookRepository extends TipBaseRepository<Book> {
    Book findBookByName(String name);
}
