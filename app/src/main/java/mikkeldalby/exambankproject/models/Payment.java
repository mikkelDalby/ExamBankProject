package mikkeldalby.exambankproject.models;

import java.util.Date;

public class Payment {
    private double amount;
    private String billType;
    private String creditor;
    private String fromaccount;
    private Date paydate;
    private String payid;
    private String paymentDocumentId;

    public Payment() {
    }

    public Payment(double amount, String billType, String creditor, String fromaccount, Date paydate, String payid, String paymentDocumentId) {
        this.amount = amount;
        this.billType = billType;
        this.creditor = creditor;
        this.fromaccount = fromaccount;
        this.paydate = paydate;
        this.payid = payid;
        this.paymentDocumentId = paymentDocumentId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getBillType() {
        return billType;
    }

    public void setBillType(String billType) {
        this.billType = billType;
    }

    public String getCreditor() {
        return creditor;
    }

    public void setCreditor(String creditor) {
        this.creditor = creditor;
    }

    public String getFromaccount() {
        return fromaccount;
    }

    public void setFromaccount(String fromaccount) {
        this.fromaccount = fromaccount;
    }

    public Date getPaydate() {
        return paydate;
    }

    public void setPaydate(Date paydate) {
        this.paydate = paydate;
    }

    public String getPayid() {
        return payid;
    }

    public void setPayid(String payid) {
        this.payid = payid;
    }

    public String getPaymentDocumentId() {
        return paymentDocumentId;
    }

    public void setPaymentDocumentId(String paymentDocumentId) {
        this.paymentDocumentId = paymentDocumentId;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "amount=" + amount +
                ", billType='" + billType + '\'' +
                ", creditor='" + creditor + '\'' +
                ", fromaccount='" + fromaccount + '\'' +
                ", paydate=" + paydate +
                ", payid='" + payid + '\'' +
                ", paymentDocumentId='" + paymentDocumentId + '\'' +
                '}';
    }
}