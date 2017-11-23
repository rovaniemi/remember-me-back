package remember.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import remember.domain.Tip;

public interface TipBaseRepository<T extends Tip> extends JpaRepository<T, Long>{

}
