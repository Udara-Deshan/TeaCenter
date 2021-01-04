package lk.Tea_Center.Model;

public class Supplier {
    private int s_ID;
    private String name;
    private String address;
    private int telephone;
    private String dOB;
    private double balance;
    private int l_ID;

    public Supplier() {

    }

    public Supplier(int s_ID, String name, String address, int telephone, String dOB, double balance,  int l_ID) {
        this.s_ID = s_ID;
        this.name = name;
        this.address = address;
        this.telephone = telephone;
        this.dOB = dOB;
        this.balance = balance;
        this.l_ID = l_ID;
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
        this.name= name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getTelephone() {
        return telephone;
    }

    public void setTelephone(int telephone) {
        this.telephone = telephone;
    }

    public String getdOB() {
        return dOB;
    }

    public void setdOB(String dOB) {
        this.dOB = dOB;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public int getL_ID() {
        return l_ID;
    }

    public void setL_ID(int l_ID) {
        this.l_ID = l_ID;
    }


}
