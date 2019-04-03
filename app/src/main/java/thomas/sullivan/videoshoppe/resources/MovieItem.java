package thomas.sullivan.videoshoppe.resources;

public class MovieItem {

    private String movieID;
    private String title;
    private String director;
    private String actor;
    private String releaseDate;
    private String ratings;
    private String genre;
    private String rentedBy;

    public MovieItem()
    {
        this.movieID = "";
        this.title = "";
        this.director = "";
        this.actor = "";
        this.releaseDate = "";
        this.ratings = "";
        this.genre = "";
        this.rentedBy = "";
    }

    public String getMovieID() {
        return this.movieID;
    }
    public String getTitle() {
        return this.title;
    }
    public String getDirector() {
        return this.director;
    }
    public String getActor(){
        return this.actor;
    }
    public String getReleaseDate() {
        return this.releaseDate;
    }
    public String getRatings() {
        return this.ratings;
    }
    public String getGenre() {
        return this.genre;
    }
    public String getRentedBy(){
        return this.rentedBy;
    }

    public void setMovieID(String aMovieID) {
        this.movieID = aMovieID;
    }
    public void setTitle(String aTitle) {
        this.title = aTitle;
    }
    public void setDirector(String aDirector) {
        this.director = aDirector;
    }
    public void setActor(String aActor) {
        this.actor = aActor;
    }
    public void setReleaseDate(String aReleaseDate) {
        this.releaseDate = aReleaseDate;
    }
    public void setRatings(String aRatings) {
        this.ratings = aRatings;
    }
    public void setGenre(String aGenre) {
        this.genre = aGenre;
    }
    public void setRentedBy(String aRentedBy) {
        this.rentedBy = aRentedBy;
    }



}
