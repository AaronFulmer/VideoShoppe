package thomas.sullivan.videoshoppe.resources;

import android.os.Parcel;
import android.os.Parcelable;

public class EmployeeChild implements Parcelable {

    private String userID;
    private String cellPhone;
    private String email;
    private String lastName;
    private String firstName;

    public EmployeeChild(String aID, String aCellPhone, String aEmail)
    {
        this.userID = aID;
        this.cellPhone = aCellPhone;
        this.email = aEmail;
    }

    protected EmployeeChild(Parcel in) {
        userID = in.readString();
        cellPhone = in.readString();
        email = in.readString();
        lastName = in.readString();
        firstName = in.readString();
    }

    public String getUserID() {
        return this.userID;
    }
    public String getCellPhone() {
        return this.cellPhone;
    }
    public String getEmail() {
        return this.email;
    }
    public String getLastName() {
        return this.lastName;
    }
    public String getFirstName() {
        return this.firstName;
    }
    public String getName() {
        return this.lastName+", "+this.firstName;
    }

    public void setLastName(String aLastName) {
        this.lastName = aLastName;
    }
    public void setFirstName(String aFirstName) {
        this.firstName = aFirstName;
    }
    public void setUserID(String aUserID) {
        this.userID = aUserID;
    }
    public void setCellPhone(String aCellPhone) {
        this.cellPhone = aCellPhone;
    }
    public void setEmail(String aEmail) {
        this.email = aEmail;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getLastName()+", "+getFirstName());
    }

    public static final Creator<EmployeeChild> CREATOR = new Creator<EmployeeChild>() {
        @Override
        public EmployeeChild createFromParcel(Parcel in) {
            return new EmployeeChild(in);
        }

        @Override
        public EmployeeChild[] newArray(int size) {
            return new EmployeeChild[size];
        }
    };
}
