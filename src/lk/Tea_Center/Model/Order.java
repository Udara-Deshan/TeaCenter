package lk.Tea_Center.Model;

public class Order {
    private int O_ID;
    private int S_ID;
    private String order_date;

    public Order() {
    }

    public Order(int o_ID, int s_ID, String order_date) {
        O_ID = o_ID;
        S_ID = s_ID;
        this.order_date = order_date;
    }

    public int getO_ID() {
        return O_ID;
    }

    public void setO_ID(int o_ID) {
        O_ID = o_ID;
    }

    public int getS_ID() {
        return S_ID;
    }

    public void setS_ID(int s_ID) {
        S_ID = s_ID;
    }

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }
}

