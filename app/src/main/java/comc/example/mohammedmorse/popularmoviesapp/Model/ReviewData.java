package comc.example.mohammedmorse.popularmoviesapp.Model;

/**
 * Created by Mohammed Morse on 29/06/2018.
 */

public class ReviewData {
    public String Name;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getReview() {
        return Review;
    }

    public void setReview(String review) {
        Review = review;
    }

    public String Review;
    public ReviewData(){}
    public ReviewData(String n,String r){
        this.Name=n;
        this.Review=r;
    }
}
