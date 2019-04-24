package thomas.sullivan.videoshoppe.resources;

public class RentalItem {

    String checkInDate = "";
    boolean isLate = false;
    double cost;
    String cust;
    int id;
    String upc;


    RentalItem(){}

    public RentalItem(String in, boolean late, double newCost, int newId, String upcCode, String custId){
        checkInDate = in;
        isLate = late;
        cost = newCost;
        id = newId;
        upc = upcCode;
        cust = custId;
    }

    public String getCheckInDate() {
        return checkInDate;
    }

    public boolean isLate() {
        return isLate;
    }

    public double getCost(){ return cost; }

    public int getId(){ return id; }

    public String getUpc(){ return upc; }

    public String getCust(){ return cust; }

}
