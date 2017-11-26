package remember.domain.instances;

import remember.domain.Tip;

import javax.persistence.Entity;

@Entity
public class Blogpost extends Tip {

    private String author;
    private String url;

    public Blogpost(String author, String url, String title, String comment) {
        this.author = author;
        this.url = url;
        this.setTitle(title);
        this.setComment(comment);
        this.setType("blogpost");
    }

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
