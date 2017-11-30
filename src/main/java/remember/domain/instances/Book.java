package remember.domain.instances;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import remember.domain.InstanceType;
import remember.domain.Tip;
import javax.persistence.Entity;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Book extends Tip {

    @NotBlank(message = "error.author.blank")
    @Length(max = 100, message = "error.author.length")
    private String author;

    public Book(String title, String comment, String author) {
        this.setTitle(title);
        this.setComment(comment);
        this.setAuthor(author);
        this.setType(InstanceType.BOOK);
    }
}
