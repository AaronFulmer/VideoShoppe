package thomas.sullivan.videoshoppe.resources;

public class InventoryItem {

    String dvdUpc = "";
    String dvdName = "";
    String dvdId = "";
    String dvdReleaseDate = "";
    String dvdDirector = "";
    String dvdGenre = "";
    String dvdCondition = "";
    boolean checkedOut;

    public InventoryItem(){}

    public InventoryItem(String upc, String name, String id, String releaseDate, String director, String genre, String condition){
        dvdUpc = upc;
        dvdName = name;
        dvdId = id;
        dvdReleaseDate = releaseDate;
        dvdDirector = director;
        dvdGenre = genre;
        dvdCondition = condition;
    }
    public String getDvdUpc() {
        return dvdUpc;
    }

    public String getDvdName() {
        return dvdName;
    }

    public String getDvdId() {
        return dvdId;
    }

    public String getDvdReleaseDate() {
        return dvdReleaseDate;
    }

    public String getDvdDirector() {
        return dvdDirector;
    }

    public String getDvdGenre() {
        return dvdGenre;
    }

    public String getDvdCondition() {
        return dvdCondition;
    }

    public boolean getCheckedOut(){ return checkedOut; }
}
