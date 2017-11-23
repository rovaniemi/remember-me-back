package remember.domain;

import javax.persistence.Entity;

@Entity(name = "Video")
public class Video extends Tip {

    private String url;

    public Video() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
