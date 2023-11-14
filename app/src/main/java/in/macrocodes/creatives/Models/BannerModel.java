package in.macrocodes.creatives.Models;

public class BannerModel {
    public BannerModel(){

    }
    public String image,link,navigate;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getNavigate() {
        return navigate;
    }

    public void setNavigate(String navigate) {
        this.navigate = navigate;
    }

    public BannerModel(String image, String link, String navigate) {
        this.image = image;
        this.link = link;
        this.navigate = navigate;
    }
}
