package in.macrocodes.creatives.Models;

public class Reported {
    public Reported(){

    }
    String id,name,amazon,flipkart,solved,issue;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getSolved() {
        return solved;
    }

    public void setSolved(String solved) {
        this.solved = solved;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public Reported(String id, String name, String amazon, String flipkart, String solved, String issue) {
        this.id = id;
        this.name = name;
        this.amazon = amazon;
        this.flipkart = flipkart;
        this.solved = solved;
        this.issue = issue;
    }
}
