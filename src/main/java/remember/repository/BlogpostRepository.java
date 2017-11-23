package remember.repository;

import remember.domain.Blogpost;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface BlogpostRepository extends TipBaseRepository<Blogpost> {

}
