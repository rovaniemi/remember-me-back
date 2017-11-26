package remember.domain.instances;

import remember.domain.Tip;

import javax.persistence.Entity;

@Entity
public class Video extends Tip {

    private String url;

    public Video(String url, String title, String comment) {
        this.url = url;
        this.setTitle(title);
        this.setComment(comment);
        this.setType("video");
    }

    public Video() {

    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
