package remember.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import remember.domain.Tip;

@NoRepositoryBean
public interface TipBaseRepository<T extends Tip> extends JpaRepository<T, Long>{

}
