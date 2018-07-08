package comc.example.mohammedmorse.popularmoviesapp.Model;

/**
 * Created by Mohammed Morse on 30/06/2018.
 */

public class ApiUrlBuilder {
    public static final String AuthRev="https://api.themoviedb.org/3/movie/";
    public static final String PathRev="/reviews?api_key=12ae0210d107863fd1d89b1e2ee1f26a&language=en-US";
    public static final String PathTra="/videos?api_key=12ae0210d107863fd1d89b1e2ee1f26a&language=en-US";

    public  String getPopularUrl() {
        return PopularUrl;
    }

    public  String getTopRated() {
        return TopRated;
    }

    public String getYoutubeTrailer() {
        return YoutubeTrailer;
    }

    public void setYoutubeTrailer(String key) {
        YoutubeTrailer += key;
    }

    public String YoutubeTrailer="https://www.youtube.com/watch?v=";
    public String PopularUrl="http://api.themoviedb.org/3/movie/popular?api_key=12ae0210d107863fd1d89b1e2ee1f26a";
    public  String TopRated="http://api.themoviedb.org/3/movie/top_rated?api_key=12ae0210d107863fd1d89b1e2ee1f26a";
   public int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getReviewUrl() {
        return ReviewUrl;
    }

    public void setReviewUrl(String reviewUrl) {
        this.ReviewUrl=AuthRev+id+PathRev;
    }

    public String getTrailerUrl() {
        return TrailerUrl;
    }

    public void setTrailerUrl(String trailerUrl) {
        this.TrailerUrl = AuthRev+id+PathTra;
    }

    public String ReviewUrl;
   public String TrailerUrl;
}
