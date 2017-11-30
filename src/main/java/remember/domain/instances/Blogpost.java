package remember.domain.instances;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;
import remember.domain.InstanceType;
import remember.domain.Tip;

import javax.persistence.Entity;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Blogpost extends Tip {

    @NotBlank(message = "error.author.blank")
    @Length(max = 100, message = "error.author.length")
    private String author;

    @NotBlank(message = "error.url.blank")
    @URL(message = "error.url.invalid")
    private String url;

    public Blogpost(String title, String comment, String author, String url) {
        this.setTitle(title);
        this.setComment(comment);
        this.setAuthor(author);
        this.setUrl(url);
        this.setType(InstanceType.BLOGPOST);
    }
}
