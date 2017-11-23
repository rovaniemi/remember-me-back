package remember.repository;

import org.springframework.transaction.annotation.Transactional;
import remember.domain.Video;

@Transactional
public interface VideoRepository extends TipBaseRepository<Video> {
    
}
