package in.macrocodes.creatives.Models;

public class CategoriesModel {
    public CategoriesModel(){

    }
    public  String image,name;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CategoriesModel(String image, String name) {
        this.image = image;
        this.name = name;
    }
}
