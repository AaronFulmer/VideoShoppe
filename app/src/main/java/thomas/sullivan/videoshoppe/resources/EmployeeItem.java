package thomas.sullivan.videoshoppe.resources;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

public class EmployeeItem {

    public String firstName;
    public String lastName;
    public String iD;
    public String phoneNumber;
    public String email;
    public String username;
    public int admin;

    public EmployeeItem(String aFirstName, String aLastName, String aID, String aPhoneNumber, String aEmail, String aUsername, int aAdmin) {
        this.firstName = aFirstName;
        this.lastName = aLastName;
        this.iD = aID;
        this.phoneNumber = aPhoneNumber;
        this.email = aEmail;
        this.username = aUsername;
        this.admin = aAdmin;
    }

    public String getFirstName(){return this.firstName;}
    public String getLastName(){return this.lastName;}
    public String getiD(){return this.iD;}
    public String getPhoneNumber(){return this.phoneNumber;}
    public String getEmail(){return this.email;}
    public String getUsername(){return this.username;}
    public int getAdmin(){return this.admin;}

    public String getName() {return lastName+", "+firstName;}

    public void setFirstName(String a){
        this.firstName = a;
    }
    public void setLastName(String a){
        this.lastName = a;
    }
    public void setiD(String a){
        this.iD = a;
    }
    public void setPhoneNumber(String a){
        this.phoneNumber = a;
    }
    public void setEmail(String a){
        this.email = a;
    }
    public void setUsername(String a){
        this.username = a;
    }
    public void setAdmin(int a){
        this.admin = a;
    }

}
