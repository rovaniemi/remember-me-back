package remember.domain.instances;

import remember.domain.Tip;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

@Entity
public class Book extends Tip {

    @NotNull
    private String author;

    public Book(String author, String title, String comment) {
        this.author = author;
        this.setTitle(title);
        this.setComment(comment);
    }

    public Book() {
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

}
