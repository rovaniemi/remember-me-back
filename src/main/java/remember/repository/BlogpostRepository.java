package remember.repository;

import remember.domain.inertances.Blogpost;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface BlogpostRepository extends TipBaseRepository<Blogpost> {

}
