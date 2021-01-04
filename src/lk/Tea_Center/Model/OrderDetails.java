package lk.Tea_Center.Model;

public class OrderDetails {
    private int o_ID;
    private int i_ID;
    private int quntitiy;
    private double priceTotal;

    public OrderDetails() {
    }

    public OrderDetails(int o_ID, int i_ID, int quntitiy, double priceTotal) {
        this.o_ID = o_ID;
        this.i_ID = i_ID;
        this.quntitiy = quntitiy;
        this.priceTotal = priceTotal;
    }

    public double getPriceTotal() {
        return priceTotal;
    }

    public void setPriceTotal(double priceTotal) {
        this.priceTotal = priceTotal;
    }

    public int getO_ID() {
        return o_ID;
    }

    public void setO_ID(int o_ID) {
        this.o_ID = o_ID;
    }

    public int getI_ID() {
        return i_ID;
    }

    public void setI_ID(int i_ID) {
        this.i_ID = i_ID;
    }

    public int getQuntitiy() {
        return quntitiy;
    }

    public void setQuntitiy(int quntitiy) {
        this.quntitiy = quntitiy;
    }
}
