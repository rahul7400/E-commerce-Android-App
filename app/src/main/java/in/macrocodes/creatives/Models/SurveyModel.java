package in.macrocodes.creatives.Models;

public class SurveyModel {
    int a,b;

    SurveyModel(){

    }
    public int getA() {
        return a;
    }

    public void setA(int a) {
        this.a = a;
    }

    public int getB() {
        return b;
    }

    public void setB(int b) {
        this.b = b;
    }

    public SurveyModel(int a, int b) {
        this.a = a;
        this.b = b;
    }
}
