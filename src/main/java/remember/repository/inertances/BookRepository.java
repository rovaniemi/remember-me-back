package remember.repository.inertances;

import org.springframework.transaction.annotation.Transactional;
import remember.domain.instances.Book;
import remember.repository.TipBaseRepository;

@Transactional
public interface BookRepository extends TipBaseRepository<Book> {
}
