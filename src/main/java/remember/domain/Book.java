package remember.domain;

import javax.persistence.Entity;

@Entity
public class Book extends Tip {

    private String author;

    public Book() {
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

}
