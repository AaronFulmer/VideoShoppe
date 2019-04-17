package thomas.sullivan.videoshoppe.resources;


import java.util.List;

public class CustomerItem {

    public String firstName;
    public String lastName;
    public String iD;
    public String phoneNumber;
    public String email;
    public String cardNumber;
    public String expirationDate;
    public String securityCode;
    public String cardType;

    public CustomerItem(String aFirstName, String aLastName, String aID, String aPhoneNumber, String aEmail, String aCardNumber, String aExpiration, String aSecurity, String aType) {
        this.firstName = aFirstName;
        this.lastName = aLastName;
        this.iD = aID;
        this.phoneNumber = aPhoneNumber;
        this.email = aEmail;
        this.cardNumber = aCardNumber;
        this.expirationDate = aExpiration;
        this.securityCode = aSecurity;
        this.cardType = aType;
    }

    public String getFirstName(){return this.firstName;}
    public String getLastName(){return this.lastName;}
    public String getiD(){return this.iD;}
    public String getPhoneNumber(){return this.phoneNumber;}
    public String getEmail(){return this.email;}
    public String getCardNumber(){return this.cardNumber;}
    public String getExpirationDate(){return this.expirationDate;}
    public String getSecurityCode(){return this.securityCode;}
    public String getCardType(){return this.cardType;}

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
    public void setCardNumber(String a){
        this.cardNumber = a;
    }
    public void setExpirationDate(String a){
        this.expirationDate = a;
    }
    public void setSecurityCode(String a){
        this.securityCode = a;
    }
    public void setCardType(String a){
        this.cardType = a;
    }

}
