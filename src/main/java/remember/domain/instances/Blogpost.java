package remember.domain.instances;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;
import remember.domain.Tip;

import javax.persistence.Entity;

@Entity
@NoArgsConstructor
@Data
public class Blogpost extends Tip {

    @NotBlank
    @Length(max = 100)
    private String author;

    @URL
    private String url;

    public Blogpost(String author, String url, String title, String comment) {
        this.author = author;
        this.url = url;
        this.setTitle(title);
        this.setComment(comment);
        this.setType("blogpost");
    }
}
