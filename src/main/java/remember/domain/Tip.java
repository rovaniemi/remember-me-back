package remember.domain;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Tip extends AbstractPersistable<Long> {

    private String title;
    private String comment;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

}
