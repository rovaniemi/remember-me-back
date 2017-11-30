package remember.domain.instances;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;
import remember.domain.InstanceType;
import remember.domain.Tip;

import javax.persistence.Entity;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Video extends Tip {

    @NotBlank(message = "error.url.blank")
    @URL(message = "error.url.invalid")
    private String url;

    public Video(String title, String comment, String url) {
        this.setTitle(title);
        this.setComment(comment);
        this.setUrl(url);
        this.setType(InstanceType.VIDEO);
    }
}
