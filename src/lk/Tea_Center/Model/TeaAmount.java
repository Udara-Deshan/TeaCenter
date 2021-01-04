package lk.Tea_Center.Model;

public class TeaAmount {
    private int t_ID;
    private int s_ID;
    private int amountKg;
    private String givenTime;

    public TeaAmount(int t_ID, int s_ID, int amountKg, String givenTime) {
        this.t_ID = t_ID;
        this.s_ID = s_ID;
        this.amountKg = amountKg;
        this.givenTime = givenTime;
    }

    public TeaAmount() {
    }

    public int getT_ID() {
        return t_ID;
    }

    public void setT_ID(int t_ID) {
        this.t_ID = t_ID;
    }

    public int getS_ID() {
        return s_ID;
    }

    public void setS_ID(int s_ID) {
        this.s_ID = s_ID;
    }

    public int getAmountKg() {
        return amountKg;
    }

    public void setAmountKg(int amountKg) {
        this.amountKg = amountKg;
    }

    public String getGivenTime() {
        return givenTime;
    }

    public void setGivenTime(String givenTime) {
        this.givenTime = givenTime;
    }
}
