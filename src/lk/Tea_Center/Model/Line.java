package lk.Tea_Center.Model;

public class Line {
    private int lID;
    private String name;
    private int profit;

    public Line() {
    }

    public Line(int lID, String name, int profit) {
        this.lID = lID;
        this.name = name;
        this.profit = profit;
    }

    public int getlID() {
        return lID;
    }

    public void setlID(int lID) {
        this.lID = lID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getProfit() {
        return profit;
    }

    public void setProfit(int profit) {
        this.profit = profit;
    }
}
