package mikkeldalby.exambankproject.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Department implements Parcelable {
    // registrationNumber is the document id in firebase
    private String registrationNumber;
    private String city;

    public Department(String registrationNumber, String city) {
        this.registrationNumber = registrationNumber;
        this.city = city;
    }

    public Department() {
    }

    protected Department(Parcel in) {
        registrationNumber = in.readString();
        city = in.readString();
    }

    public static final Creator<Department> CREATOR = new Creator<Department>() {
        @Override
        public Department createFromParcel(Parcel in) {
            return new Department(in);
        }

        @Override
        public Department[] newArray(int size) {
            return new Department[size];
        }
    };

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(registrationNumber);
        dest.writeString(city);
    }

    @Override
    public String toString() {
        return "Department{" +
                "registrationNumber='" + registrationNumber + '\'' +
                ", city='" + city + '\'' +
                '}';
    }
}
