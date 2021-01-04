package lk.Tea_Center.Model;

public class Item {
    private int iID;
    private String name;
    private double price;
    private int paymentUptoMonth;
    private int quntitiyOnStock;


    public Item(int iID, String name, double price, int paymentUptoMonth, int quntitiyOnStock) {
        this.iID = iID;
        this.name = name;
        this.price = price;
        this.paymentUptoMonth = paymentUptoMonth;
        this.quntitiyOnStock = quntitiyOnStock;
    }

    public Item() {

    }

    public Item(double price, int onStock) {
        this.price=price;
        this.quntitiyOnStock=onStock;
    }

    public int getiID() {
        return iID;
    }

    public void setiID(int iID) {
        this.iID = iID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getPaymentUptoMonth() {
        return paymentUptoMonth;
    }

    public void setPaymentUptoMonth(int paymentUptoMonth) {
        this.paymentUptoMonth = paymentUptoMonth;
    }

    public int getQuntitiyOnStock() {
        return quntitiyOnStock;
    }

    public void setQuntitiyOnStock(int quntitiyOnStock) {
        this.quntitiyOnStock = quntitiyOnStock;
    }
}
