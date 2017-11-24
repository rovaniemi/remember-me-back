package remember.repository.inertances;

import remember.domain.inertances.Blogpost;
import org.springframework.transaction.annotation.Transactional;
import remember.repository.TipBaseRepository;

@Transactional
public interface BlogpostRepository extends TipBaseRepository<Blogpost> {

}
