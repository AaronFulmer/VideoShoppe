package thomas.sullivan.videoshoppe.resources;


import java.util.List;

public class InventoryItem {

    public String upcCode;
    public String name;
    public String iD;
    public String releaseDate;
    public String director;
    public String actors;
    public String genre;
    public String condition;

    public InventoryItem(String aUPC, String aName, String aID, String aReleaseDate, String aDirector, String aActors, String aGenre, String aCondition) {
        this.upcCode = aUPC;
        this.name = aName;
        this.iD = aID;
        this.releaseDate = aReleaseDate;
        this.director = aDirector;
        this.actors = aActors;
        this.genre = aGenre;
        this.condition = aCondition;
    }

    public String getUPCCode(){return this.upcCode;}
    public String getTitle(){return this.name;}
    public String getiD(){return this.iD;}
    public String getReleaseDate(){return this.releaseDate;}
    public String getDirector(){return this.director;}
    public String getActors(){return this.actors;}
    public String getGenre(){return this.genre;}
    public String getCondition(){return this.condition;}

    public void setUPCCode(String a){
        this.upcCode = a;
    }
    public void setTitle(String a){
        this.name = a;
    }
    public void setiD(String a){
        this.iD = a;
    }
    public void setReleaseDate(String a){
        this.releaseDate = a;
    }
    public void setDirector(String a){
        this.director = a;
    }
    public void setActors(String a){
        this.actors = a;
    }
    public void setGenre(String a){
        this.genre = a;
    }
    public void setCondition(String a){
        this.condition = a;
    }

}
