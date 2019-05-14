package mikkeldalby.exambankproject.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Account implements Parcelable {
    // accountType is the document id in firebase
    private String accountType;
    private String accountNumber;
    private boolean active;
    private double balance;
    private String customName;

    public Account(String accountType, String accountNumber, boolean active, double balance, String customName) {
        this.accountType = accountType;
        this.accountNumber = accountNumber;
        this.active = active;
        this.balance = balance;
        this.customName = customName;
    }

    protected Account(Parcel in) {
        accountType = in.readString();
        accountNumber = in.readString();
        active = in.readByte() != 0;
        balance = in.readDouble();
        customName = in.readString();
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

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
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

    public String getCustomName() {
        return customName;
    }

    public void setCustomName(String customName) {
        this.customName = customName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(accountType);
        dest.writeString(accountNumber);
        dest.writeByte((byte) (active ? 1 : 0));
        dest.writeDouble(balance);
        dest.writeString(customName);
    }
}
