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

    @NotBlank(message = "error.author.blank")
    @Length(max = 100, message = "error.author.length")
    private String author;

}
