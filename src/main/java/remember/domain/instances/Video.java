package remember.domain.instances;

import remember.domain.Tip;

import javax.persistence.Entity;

@Entity
public class Video extends Tip {

    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
