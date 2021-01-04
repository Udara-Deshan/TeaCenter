package lk.Tea_Center.Model;

public class Loan {
    private int loan_ID;
    private int s_ID;
    private double loan;
    private String openDate;
    private int monthPeriod;
    private double paymentForMonth;

    public Loan() {
    }

    public Loan(int loan_ID, int s_ID, double loan, String openDate, int monthPeriod, double paymentForMonth) {
        this.loan_ID = loan_ID;
        this.s_ID = s_ID;
        this.loan = loan;
        this.openDate = openDate;
        this.monthPeriod = monthPeriod;
        this.paymentForMonth = paymentForMonth;
    }

    public int getLoan_ID() {
        return loan_ID;
    }

    public void setLoan_ID(int loan_ID) {
        this.loan_ID = loan_ID;
    }

    public int getS_ID() {
        return s_ID;
    }

    public void setS_ID(int s_ID) {
        this.s_ID = s_ID;
    }

    public double getLoan() {
        return loan;
    }

    public void setLoan(double loan) {
        this.loan = loan;
    }

    public String getOpenDate() {
        return openDate;
    }

    public void setOpenDate(String openDate) {
        this.openDate = openDate;
    }

    public int getMonthPeriod() {
        return monthPeriod;
    }

    public void setMonthPeriod(int monthPeriod) {
        this.monthPeriod = monthPeriod;
    }

    public double getPaymentForMonth() {
        return paymentForMonth;
    }

    public void setPaymentForMonth(double paymentForMonth) {
        this.paymentForMonth = paymentForMonth;
    }
}
