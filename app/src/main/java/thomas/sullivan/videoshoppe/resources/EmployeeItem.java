package thomas.sullivan.videoshoppe.resources;

public class EmployeeItem {

    private String userID;
    private String lastName;
    private String firstName;
    private String username;
    private String password;
    private String admin;

    public EmployeeItem()
    {
        this.userID = "";
        this.lastName = "";
        this.firstName = "";
        this.username = "";
        this.password = "";
        this.admin = "";
    }

    public String getUserID() {
        return this.userID;
    }
    public String getLastName() {
        return this.lastName;
    }
    public String getFirstName() {
        return this.firstName;
    }
    public String getUsername() {
        return this.firstName;
    }
    public String getPassword() {
        return this.password;
    }
    public String getAdmin() {
        return this.admin;
    }

    public void setUserID(String aUserID) {
        this.userID = aUserID;
    }
    public void setLastName(String aLastName) {
        this.lastName = aLastName;
    }
    public void setFirstName(String aFirstName) {
        this.firstName = aFirstName;
    }
    public void setUsername(String aUsername) {
        this.username = aUsername;
    }
    public void setPassword(String aPassword) {
        this.password = aPassword;
    }
    public void setAdmin(String aAdmin) {
        this.admin = aAdmin;
    }

}
