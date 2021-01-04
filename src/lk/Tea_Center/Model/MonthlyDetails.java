package lk.Tea_Center.Model;

public class MonthlyDetails {
    private int m_ID;
    private int l_ID;
    private String year_Name;
    private String month_Name;
    private double tea_Price;
    private double month_Total;

    public MonthlyDetails() {
    }

    public MonthlyDetails(int m_ID, int l_ID, String year_Name, String month_Name, double tea_Price, double month_Total) {
        this.m_ID = m_ID;
        this.l_ID = l_ID;
        this.year_Name = year_Name;
        this.month_Name = month_Name;
        this.tea_Price = tea_Price;
        this.month_Total = month_Total;
    }

    public int getM_ID() {
        return m_ID;
    }

    public void setM_ID(int m_ID) {
        this.m_ID = m_ID;
    }

    public int getL_ID() {
        return l_ID;
    }

    public void setL_ID(int l_ID) {
        this.l_ID = l_ID;
    }

    public String getYear_Name() {
        return year_Name;
    }

    public void setYear_Name(String year_Name) {
        this.year_Name = year_Name;
    }

    public String getMonth_Name() {
        return month_Name;
    }

    public void setMonth_Name(String month_Name) {
        this.month_Name = month_Name;
    }

    public double getTea_Price() {
        return tea_Price;
    }

    public void setTea_Price(double tea_Price) {
        this.tea_Price = tea_Price;
    }

    public double getMonth_Total() {
        return month_Total;
    }

    public void setMonth_Total(double month_Total) {
        this.month_Total = month_Total;
    }
}
