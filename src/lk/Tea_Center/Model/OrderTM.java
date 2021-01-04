package lk.Tea_Center.Model;

public class OrderTM {
    private String name;
    private int quantity;
    private double price;
    private int itemCode;

    public OrderTM() {
    }

    public OrderTM(String name, int quantity, double price, int itemCode) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.itemCode = itemCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getItemCode() {
        return itemCode;
    }

    public void setItemCode(int itemCode) {
        this.itemCode = itemCode;
    }
}
