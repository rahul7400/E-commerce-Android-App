package in.macrocodes.creatives.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class TrendingProducts implements Parcelable  {
    String name;
    String description;
    String price;
    String image;
    String amazon,flipkart;
    String id,rank,category;



    TrendingProducts(){

    }


    protected TrendingProducts(Parcel in) {
        name = in.readString();
        description = in.readString();
        price = in.readString();
        image = in.readString();
        amazon = in.readString();
        flipkart = in.readString();
        id = in.readString();
        rank = in.readString();
        category = in.readString();
    }

    public static final Creator<TrendingProducts> CREATOR = new Creator<TrendingProducts>() {
        @Override
        public TrendingProducts createFromParcel(Parcel in) {
            return new TrendingProducts(in);
        }

        @Override
        public TrendingProducts[] newArray(int size) {
            return new TrendingProducts[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAmazon() {
        return amazon;
    }

    public void setAmazon(String amazon) {
        this.amazon = amazon;
    }

    public String getFlipkart() {
        return flipkart;
    }

    public void setFlipkart(String flipkart) {
        this.flipkart = flipkart;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public TrendingProducts(String name, String description, String price, String image, String id, String amazon, String flipkart, String category) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.image = image;
        this.amazon = amazon;
        this.flipkart = flipkart;
        this.id = id;
        this.category=category;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(price);
        dest.writeString(image);
        dest.writeString(amazon);
        dest.writeString(flipkart);
        dest.writeString(id);
        dest.writeString(rank);
        dest.writeString(category);
    }
}
