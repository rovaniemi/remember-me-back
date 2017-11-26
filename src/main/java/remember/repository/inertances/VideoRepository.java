package remember.repository.inertances;

import org.springframework.transaction.annotation.Transactional;
import remember.domain.inertances.Video;
import remember.repository.TipBaseRepository;

@Transactional
public interface VideoRepository extends TipBaseRepository<Video> {
    
}
