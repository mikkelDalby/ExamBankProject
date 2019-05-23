package mikkeldalby.exambankproject.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Customer implements Parcelable {
    // customerId is the document id in firebase
    private String customerId;
    private String adress;
    private String city;
    private String cpr;
    private String customernumber;
    private String firstname;
    private String lastname;
    private String registrationnumber;
    private int zipcode;

    private List<Account> accounts;
    private Department department;

    public Customer(String customerId, String adress, String city, String cpr, String customerNumber, String firstName, String lastName, String registrationNumber, int zipcode, List<Account> accounts, Department department) {
        this.customerId = customerId;
        this.adress = adress;
        this.city = city;
        this.cpr = cpr;
        this.customernumber = customerNumber;
        this.firstname = firstName;
        this.lastname = lastName;
        this.registrationnumber = registrationNumber;
        this.zipcode = zipcode;
        this.accounts = accounts;
        this.department = department;
    }

    public Customer() {
    }

    protected Customer(Parcel in) {
        customerId = in.readString();
        adress = in.readString();
        city = in.readString();
        cpr = in.readString();
        customernumber = in.readString();
        firstname = in.readString();
        lastname = in.readString();
        registrationnumber = in.readString();
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

    public String getCustomernumber() {
        return customernumber;
    }

    public void setCustomernumber(String customernumber) {
        this.customernumber = customernumber;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getRegistrationnumber() {
        return registrationnumber;
    }

    public void setRegistrationnumber(String registrationnumber) {
        this.registrationnumber = registrationnumber;
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
        dest.writeString(customernumber);
        dest.writeString(firstname);
        dest.writeString(lastname);
        dest.writeString(registrationnumber);
        dest.writeInt(zipcode);
        dest.writeParcelable(department, flags);
    }

    public int getAge(){
        int age = -1;
        String a = cpr.replaceAll("..(?!$)", "$0 ");
        List<String> b = Arrays.asList(a.split(" "));
        LocalDate birthDate = LocalDate.of(Integer.parseInt(b.get(2)+b.get(3)),
                Integer.parseInt(b.get(1)), Integer.parseInt(b.get(0)));
        LocalDate currentDate = LocalDate.now();

        if ((birthDate != null) && (currentDate != null)) {
            age = Period.between(birthDate, currentDate).getYears();
        } else {
            age = 0;
        }

        return age;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "customerId='" + customerId + '\'' +
                ", adress='" + adress + '\'' +
                ", city='" + city + '\'' +
                ", cpr='" + cpr + '\'' +
                ", customernumber='" + customernumber + '\'' +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", registrationnumber='" + registrationnumber + '\'' +
                ", zipcode=" + zipcode +
                ", accounts=" + accounts +
                ", department=" + department +
                '}';
    }

    public List<String> getPossibleAccounts() {
        List<String> fromAccounts = new ArrayList<>();
        for (Account a: getAccounts()){
            boolean oldEnough = getAge() > 76;
            if (a.isActive()) {
                if (oldEnough && a.getAccountType().equals("pension") || !a.getAccountType().equals("pension")){
                    fromAccounts.add(a.getCustomname());
                }
            }
        }
        return fromAccounts;
    }
}
