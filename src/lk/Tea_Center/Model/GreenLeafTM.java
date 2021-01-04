package lk.Tea_Center.Model;

public class GreenLeafTM {
    private int s_ID;
    private String name;
    private int teaAmount;
    private String givenTime;

    public GreenLeafTM() {
    }

    public GreenLeafTM(int s_ID, String name, int teaAmount, String givenTime) {
        this.s_ID = s_ID;
        this.name = name;
        this.teaAmount = teaAmount;
        this.givenTime = givenTime;
    }

    public int getS_ID() {
        return s_ID;
    }

    public void setS_ID(int s_ID) {
        this.s_ID = s_ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTeaAmount() {
        return teaAmount;
    }

    public void setTeaAmount(int teaAmount) {
        this.teaAmount = teaAmount;
    }

    public String getGivenTime() {
        return givenTime;
    }

    public void setGivenTime(String givenTime) {
        this.givenTime = givenTime;
    }
}
