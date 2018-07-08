package comc.example.mohammedmorse.popularmoviesapp;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonArray;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import comc.example.mohammedmorse.popularmoviesapp.Adapters.MyCustomCallBackForTrailer;
import comc.example.mohammedmorse.popularmoviesapp.Adapters.ReviewAdapter;
import comc.example.mohammedmorse.popularmoviesapp.Adapters.TrailerAdapter;
import comc.example.mohammedmorse.popularmoviesapp.ContentProvider.ContentProviderContract;
import comc.example.mohammedmorse.popularmoviesapp.DataBase.MovieContract;
import comc.example.mohammedmorse.popularmoviesapp.DataBase.MovieDataBase;
import comc.example.mohammedmorse.popularmoviesapp.Model.ApiUrlBuilder;
import comc.example.mohammedmorse.popularmoviesapp.Model.MovieModelData;
import comc.example.mohammedmorse.popularmoviesapp.Model.ReviewData;
import comc.example.mohammedmorse.popularmoviesapp.Model.TrailerData;
import comc.example.mohammedmorse.popularmoviesapp.databinding.ActivityDetailBinding;

public class DetailActivity extends AppCompatActivity implements MyCustomCallBackForTrailer{
   public ApiUrlBuilder urls;
    public ProgressDialog dialog;
    public ArrayList<ReviewData> myReviews;
    public ArrayList<TrailerData> myTrailer;
    public TrailerAdapter trailerAdapter;
    public String MovieName;
    public boolean ifExist;
    public JSONArray array1;
    public JSONArray array2;
    ArrayList<ReviewData>reviewData;
    public ReviewAdapter reviewAdapter;
    public RecyclerView.LayoutManager layoutManager;
    public RecyclerView.LayoutManager layoutManagerc;
    public RequestQueue queue ;
    MovieDataBase dataBase;
    MovieModelData data;
    public ActivityDetailBinding detailBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        detailBinding= DataBindingUtil.setContentView(this,R.layout.activity_detail);
        dialog=new ProgressDialog(this);
          urls=new ApiUrlBuilder();
          data=new MovieModelData();
          myReviews=new ArrayList<>();
          myTrailer=new ArrayList<>();
          array1=new JSONArray();
        array2=new JSONArray();
        reviewData=new ArrayList<>();
          dataBase=new MovieDataBase(this);
        trailerAdapter=new TrailerAdapter(this,this,myTrailer);
        reviewAdapter = new ReviewAdapter(this,myReviews);
        layoutManager=new GridLayoutManager(this,1);
        layoutManagerc=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        detailBinding.Review.setLayoutManager(layoutManager);
        detailBinding.Review.setAdapter(reviewAdapter);
        detailBinding.Trailer.setLayoutManager(layoutManagerc);
        detailBinding.Trailer.setAdapter(trailerAdapter);
        queue= Volley.newRequestQueue(this);
        SetLoadingDialog();
         SetData();
    }
    public void SetData(){
        Intent intent=getIntent();
        data=new MovieModelData();
        int Position=intent.getIntExtra("Position",0);
        data.setId(intent.getIntExtra("id",0));
        data.setName(intent.getStringExtra("Name"));
        setTitle(data.getName());
        MovieName=data.getName();
        data.setOverview(intent.getStringExtra("Overview"));
        data.setRate(intent.getIntExtra("Rate",0));
        data.setReleaseDate(intent.getStringExtra("Date"));
        String background=intent.getStringExtra("Background");
        String poster=intent.getStringExtra("Poster");
        data.PosterMovie=poster;
        data.BackgroundMovie=background;
        ifExist=dataBase.IfExistInDataBaseOrNot(MovieName);
        if(ifExist==true){
            detailBinding.unorFavourite.setImageResource(R.drawable.ic_favorite);
        }
        else{
            detailBinding.unorFavourite.setImageResource(R.drawable.unfavourite);
        }
       //LoadReviewFirst
       GetMovieReview(data.getId());
       //Load TrailerFirst
        GetMovieTrailer(data.getId());

        Picasso.with(this).load(background).into(detailBinding.MovieBackroundImage);
        detailBinding.MovieNameDetail.setText(data.getName());
        detailBinding.MovieRateDetail.setText(String .valueOf(data.getRate())+"/10");
        detailBinding.MovieOverviewDetail.setText(data.getOverview());
        detailBinding.MovieDateDetail.setText(data.getReleaseDate());

        dialog.dismiss();
   // Toast.makeText(this, data.getName()+" is a very good Chooise for watching it ", Toast.LENGTH_LONG).show();
}

    public void GetMovieReview(int Id){
        urls.setId(Id);
        urls.setReviewUrl(String.valueOf(Id));
        String ReviewUrl=urls.getReviewUrl();

        JsonObjectRequest reviewrequest=new JsonObjectRequest(Request.Method.GET, ReviewUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray=response.getJSONArray("results");
                    ReviewData Objectdata;
                    for(int i=0;i<jsonArray.length();i++) {
                        Objectdata=new ReviewData();
                            JSONObject object=jsonArray.getJSONObject(i);
                        Objectdata.setName(object.getString("author"));
                        Objectdata.setReview(object.getString("content"));
                            myReviews.add(Objectdata);
                        Log.e("morse", "Name is "+myReviews.get(i).getName());
                    }
                    Log.e("morse", "Size is "+myReviews.size());
                    if(myReviews.size()==0){
                        detailBinding.ReviewP.setVisibility(View.INVISIBLE);
                        detailBinding.Review.setVisibility(View.INVISIBLE);
                    }
                    else {
                        detailBinding.ReviewP.setVisibility(View.VISIBLE);
                        detailBinding.Review.setVisibility(View.VISIBLE);
                        data.setReviewData(myReviews);
                    }
                    reviewAdapter.notifyDataSetChanged();
                    data.setReviewData(myReviews);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DetailActivity.this, "The error Message is "+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(reviewrequest);
    }
    public void GetMovieTrailer(int Id){
        urls.setId(Id);
        urls.setTrailerUrl(String.valueOf(Id));
        String TrailerUrl=urls.getTrailerUrl();
        JsonObjectRequest reviewrequest=new JsonObjectRequest(Request.Method.GET, TrailerUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray=response.getJSONArray("results");
                    for(int i=0;i<jsonArray.length();i++) {
                        TrailerData Objectdata=new TrailerData();
                        JSONObject object=jsonArray.getJSONObject(i);
                        Objectdata.setName(object.getString("name"));
                        Objectdata.setKey(object.getString("key"));
                        Objectdata.setSize(String.valueOf(object.getInt("size")));
                    myTrailer.add(Objectdata);
                    }
                    if(myTrailer.size()==0){
                        detailBinding.TrailerP.setVisibility(View.INVISIBLE);
                        detailBinding.Trailer.setVisibility(View.INVISIBLE);
                    }
                    else {
                        detailBinding.TrailerP.setVisibility(View.VISIBLE);
                        detailBinding.Trailer.setVisibility(View.VISIBLE);
                    data.setTrailerData(myTrailer);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                trailerAdapter.notifyDataSetChanged();
                data.setTrailerData(myTrailer);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DetailActivity.this, "The error Message is "+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(reviewrequest);

    }
   public void SetLoadingDialog(){
        dialog.setMessage("Loading");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMax(100);
        dialog.show();
   }
    public void MakeitFavourite(View view) throws JSONException {
       // If It`s in DataBase So >>>>>>>>>>>>>> Un Favourite
     if(ifExist==true){
         //Delet
        getContentResolver().delete(ContentProviderContract.URITOTABLE,null, new String[]{String.valueOf(data.getId())});
         detailBinding.unorFavourite.setImageResource(R.drawable.unfavourite);
         Toast.makeText(this, "Delet Operation has Done .", Toast.LENGTH_SHORT).show();
     }
     else{
         reviewData=data.getReviewData();
         int Size1=reviewData.size();
         if(Size1==0){
             ArrayList<ReviewData> NullObject=new ArrayList<>();
             NullObject.add(new ReviewData("Nothing","Nothing"));
             data.setReviewData(NullObject);
         }
         if(data.getTrailerData().size()==0){
             ArrayList<TrailerData> NullObject=new ArrayList<>();
             NullObject.add(new TrailerData("Nothing","Nothing","Nothing"));
             data.setTrailerData(NullObject);
         }
         ContentValues values=new ContentValues();
         values.put(MovieContract.id,data.getId());
         values.put(MovieContract.name,data.getName());
         values.put(MovieContract.postermovie,data.getPosterMovie());
         values.put(MovieContract.backgroundmovie,data.getBackgroundMovie());
         values.put(MovieContract.releasedate,data.getReleaseDate());
         values.put(MovieContract.rate,data.getRate());
         values.put(MovieContract.overview,data.getOverview());
         array1=dataBase.ReturnReviewsAsJson(data);
         String MyReview=array1.toString();
         array2=dataBase.ReturnTrailersAsJson(data);
         String MyTrailer=array2.toString();
         values.put(MovieContract.reviewdata,MyReview);
         values.put(MovieContract.trailerdata,MyTrailer);
         getContentResolver().insert(ContentProviderContract.URITOTABLE,values);
         detailBinding.unorFavourite.setImageResource(R.drawable.ic_favorite);
         //Insert
         Toast.makeText(this, "Insert Operation has Done .", Toast.LENGTH_SHORT).show();
     }
       //Else Favourite It
    }
    @Override
    public void OpenYoutubeListener(String data) {
       urls.setYoutubeTrailer(data);
       String Uri=urls.getYoutubeTrailer();
       Intent OPenYoutube=new Intent(Intent.ACTION_VIEW);
       OPenYoutube.setData(android.net.Uri.parse(Uri));
       if(OPenYoutube.resolveActivity(getPackageManager())==null){
          Intent Chooser=Intent.createChooser(OPenYoutube,"Choose an App to open the Video ");
          startActivity(Chooser);
       }
       else{
       startActivity(OPenYoutube);
           }
   }
}
