package remember.domain.instances;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;
import remember.domain.Tip;

import javax.persistence.Entity;

@Entity
@NoArgsConstructor
@Data
public class Video extends Tip {


    @NotBlank
    @URL
    private String url;

    public Video(String url, String title, String comment) {
        this.url = url;
        this.setTitle(title);
        this.setComment(comment);
        this.setType("video");
    }
}
