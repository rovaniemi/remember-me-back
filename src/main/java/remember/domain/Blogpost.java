package remember.domain;

import javax.persistence.Entity;

@Entity
public class Blogpost extends Tip {

    private String author;
    private String url;

    public Blogpost() {
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
