package remember.domain;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor
@AllArgsConstructor
@Data
public abstract class Tip extends AbstractPersistable<Long> {

    @NotBlank(message = "error.title.blank")
    @Length(max = 100, message = "error.title.length")
    private String title;

    @Length(max = 1000, message = "error.comment.length")
    private String comment;

    private String type;
}
