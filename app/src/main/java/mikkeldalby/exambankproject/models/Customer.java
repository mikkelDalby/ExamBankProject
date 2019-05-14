package mikkeldalby.exambankproject.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Customer implements Parcelable {
    // customerId is the document id in firebase
    private String customerId;
    private String adress;
    private String city;
    private String cpr;
    private String customerNumber;
    private String firstName;
    private String lastName;
    private String registrationNumber;
    private int zipcode;

    private List<Account> accounts;
    private Department department;

    public Customer(String customerId, String adress, String city, String cpr, String customerNumber, String firstName, String lastName, String registrationNumber, int zipcode, List<Account> accounts, Department department) {
        this.customerId = customerId;
        this.adress = adress;
        this.city = city;
        this.cpr = cpr;
        this.customerNumber = customerNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.registrationNumber = registrationNumber;
        this.zipcode = zipcode;
        this.accounts = accounts;
        this.department = department;
    }

    protected Customer(Parcel in) {
        customerId = in.readString();
        adress = in.readString();
        city = in.readString();
        cpr = in.readString();
        customerNumber = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        registrationNumber = in.readString();
        zipcode = in.readInt();
        department = in.readParcelable(Department.class.getClassLoader());
    }

    public static final Creator<Customer> CREATOR = new Creator<Customer>() {
        @Override
        public Customer createFromParcel(Parcel in) {
            return new Customer(in);
        }

        @Override
        public Customer[] newArray(int size) {
            return new Customer[size];
        }
    };

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCpr() {
        return cpr;
    }

    public void setCpr(String cpr) {
        this.cpr = cpr;
    }

    public String getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(String customerNumber) {
        this.customerNumber = customerNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public int getZipcode() {
        return zipcode;
    }

    public void setZipcode(int zipcode) {
        this.zipcode = zipcode;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(customerId);
        dest.writeString(adress);
        dest.writeString(city);
        dest.writeString(cpr);
        dest.writeString(customerNumber);
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(registrationNumber);
        dest.writeInt(zipcode);
        dest.writeParcelable(department, flags);
    }
}
