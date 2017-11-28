package remember.domain.instances;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import remember.domain.Tip;
import javax.persistence.Entity;

@Entity
@NoArgsConstructor
@Data
public class Book extends Tip {

    @NotBlank
    @Length(max = 100)
    private String author;

    public Book(String author, String title, String comment) {
        this.author = author;
        this.setTitle(title);
        this.setComment(comment);
        this.setType("book");
    }
}
