package comc.example.mohammedmorse.popularmoviesapp;

/**
 * Created by Mohammed Morse on 16/06/2018.
 */

public class MovieModelData {
    public String Name;
    public String PosterMovie;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPosterMovie() {

        return PosterMovie;
    }

    public void setPosterMovie(String posterMovie) {
        PosterMovie = "http://image.tmdb.org/t/p/w185/"+posterMovie;
    }

    public String getBackgroundMovie() {
        return BackgroundMovie;
    }

    public void setBackgroundMovie(String backgroundMovie) {
        BackgroundMovie = "http://image.tmdb.org/t/p/w500"+backgroundMovie;
    }

    public int getRate() {
        return Rate;
    }

    public void setRate(int rate) {
        Rate = rate;
    }

    public String getOverview() {
        return Overview;
    }

    public void setOverview(String overview) {
        Overview = overview;
    }

    public String getReleaseDate() {
        return ReleaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        ReleaseDate = releaseDate;
    }

    public String BackgroundMovie;
    public int Rate;
    public String Overview;
    public String ReleaseDate;
}
