package mikkeldalby.exambankproject.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Account implements Parcelable {
    // accountType is the document id in firebase
    private String accountType;
    private String accountnumber;
    private boolean active;
    private double balance;
    private String customname;

    public Account(String accountType, String accountNumber, boolean active, double balance, String customName) {
        this.accountType = accountType;
        this.accountnumber = accountNumber;
        this.active = active;
        this.balance = balance;
        this.customname = customName;
    }

    public Account(String accountnumber, boolean active, double balance, String customname) {
        this.accountnumber = accountnumber;
        this.active = active;
        this.balance = balance;
        this.customname = customname;
    }

    public Account() {
    }

    protected Account(Parcel in) {
        accountType = in.readString();
        accountnumber = in.readString();
        active = in.readByte() != 0;
        balance = in.readDouble();
        customname = in.readString();
    }

    public static final Creator<Account> CREATOR = new Creator<Account>() {
        @Override
        public Account createFromParcel(Parcel in) {
            return new Account(in);
        }

        @Override
        public Account[] newArray(int size) {
            return new Account[size];
        }
    };

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getAccountnumber() {
        return accountnumber;
    }

    public void setAccountnumber(String accountnumber) {
        this.accountnumber = accountnumber;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getCustomname() {
        return customname;
    }

    public void setCustomname(String customname) {
        this.customname = customname;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(accountType);
        dest.writeString(accountnumber);
        dest.writeByte((byte) (active ? 1 : 0));
        dest.writeDouble(balance);
        dest.writeString(customname);
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountType='" + accountType + '\'' +
                ", accountnumber='" + accountnumber + '\'' +
                ", active=" + active +
                ", balance=" + balance +
                ", customname='" + customname + '\'' +
                '}';
    }
}
