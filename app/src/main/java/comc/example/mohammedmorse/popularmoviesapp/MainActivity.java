package comc.example.mohammedmorse.popularmoviesapp;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import comc.example.mohammedmorse.popularmoviesapp.Adapters.CustomAdapter;
import comc.example.mohammedmorse.popularmoviesapp.ContentProvider.ContentProviderContract;
import comc.example.mohammedmorse.popularmoviesapp.DataBase.MovieContract;
import comc.example.mohammedmorse.popularmoviesapp.DataBase.MovieDataBase;
import comc.example.mohammedmorse.popularmoviesapp.Model.ApiUrlBuilder;
import comc.example.mohammedmorse.popularmoviesapp.Model.MovieModelData;
import comc.example.mohammedmorse.popularmoviesapp.Model.ReviewData;
import comc.example.mohammedmorse.popularmoviesapp.Model.TrailerData;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
     public ApiUrlBuilder uri;
     String FinalUrl=null;
     boolean ifNotConnect=false;
     ProgressDialog dialog;
     ArrayList<MovieModelData> myData;
     CustomAdapter adapter;
    RecyclerView.LayoutManager manager;
    ImageView imageView;
    RequestQueue requestQueue;

    Parcelable State;
     MovieDataBase dataBase;
    RecyclerView MyrecyclerView;
    FloatingActionButton button;
    boolean networkAvailability;
    boolean IamVisitFavPage=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Movies");
        dialog=new ProgressDialog(this);
        dataBase=new MovieDataBase(this);
        uri=new ApiUrlBuilder();
        imageView=findViewById(R.id.myImage);
        button=findViewById(R.id.floatingImage);
        myData=new ArrayList<MovieModelData>();
        MyrecyclerView=findViewById(R.id.MyrecyclerList);
        manager=new GridLayoutManager(this,2);
        adapter=new CustomAdapter(this,myData);
        requestQueue=Volley.newRequestQueue(this);
        MyrecyclerView.setAdapter(adapter);
        State=new Bundle();
        MyrecyclerView.setLayoutManager(manager);
        SharedPreferences preferences= android.support.v7.preference.PreferenceManager.getDefaultSharedPreferences(this);
        preferences.registerOnSharedPreferenceChangeListener(this);
        networkAvailability=CheckTheNetwork();
        if(networkAvailability==false){
           // progressBar.setVisibility(View.INVISIBLE);
            ShowAlerDialog();
            ifNotConnect=true;
        }
        else {
            FinalUrl=GetFinalUrl();
            GetMoviesDetails(FinalUrl);
        }
        Log.e("Cycle", "onCreate: .");
    }
    @Override
    protected void onResume() {
        super.onResume();
        if(ifNotConnect==true){
            button.setVisibility(View.VISIBLE);
        }
        /*if(IamVisitFavPage!=true){
            FinalUrl=GetFinalUrl();
            GetMoviesDetails(FinalUrl);
        }*/
        // Check if Iam in Favourite Or Not
        //if((myData.size()<20||myData.size()>20)&&IamVisitFavPage==true)
        if(IamVisitFavPage==true){
            ArrayList<MovieModelData> data =new ArrayList<>();

            try {
                Cursor cursor=getContentResolver().query(ContentProviderContract.FinalUrl,null,
                        null,null,null,null);
                data=OperationInCursor(cursor);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            myData.clear();
            myData.addAll(data);
            adapter.notifyDataSetChanged();
        }
        Log.e("Cycle", "onResume: ");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e("Cycle", "onStart: " );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.e("Cycle", "onCreateOptionsMenu: ");
        getMenuInflater().inflate(R.menu.settingmenu,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.setting){
            Intent intent=new Intent(this , SettingActivity.class);
            startActivity(intent);
        }
        else if(id==R.id.about){
            Toast.makeText(this, "My Name is Mohammed Morse , 20 Years Old", Toast.LENGTH_LONG).show();
        }
        else if(id==R.id.favourite){
            ArrayList<MovieModelData> data =new ArrayList<>();
             IamVisitFavPage=true;
            try {
                Cursor cursor=getContentResolver().query(ContentProviderContract.FinalUrl,null,null,null,null);
                data=OperationInCursor(cursor);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            // dialog.dismiss();
                if(data.size()==0){
                    imageView.setVisibility(View.VISIBLE);
                    MyrecyclerView.setVisibility(View.INVISIBLE);
                    Toast.makeText(this, "It`s Empty ", Toast.LENGTH_SHORT).show();
                }
                else {
                     myData.clear();
                     adapter.notifyDataSetChanged();
                    myData.addAll(data);
                    if(imageView.getVisibility()==View.VISIBLE) {
                        imageView.setVisibility(View.INVISIBLE);
                        MyrecyclerView.setVisibility(View.VISIBLE);
                    }
                  //  Toast.makeText(this, "The data is "+data.get(0).getName()+"Poster Image is "+data.get(0).getPosterMovie() +"Review Data "+data.get(0).getReviewData().toString(), Toast.LENGTH_SHORT).show();
                    adapter.notifyDataSetChanged();
                }

        }
        Log.e("Cycle", "onOptionsItemSelected: " );
        return super.onOptionsItemSelected(item);
    }
    public boolean CheckTheNetwork(){
        boolean ifConnected;
        ConnectivityManager manager= (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo info=manager.getActiveNetworkInfo();
        if(info!=null &&info.isConnected()){
            ifConnected=true;
        }
        else
            ifConnected=false;

        return ifConnected;
    }
    public void ShowAlerDialog(){
        final AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Network");
        builder.setIcon(R.drawable.ic_network_check_black_24dp);
        builder.setMessage("Please Check the Wifi or Data , if They aren`t workink Please Turn Wifi or Mobile Data ON .");
        builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent=new Intent(android.provider.Settings.ACTION_SETTINGS);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
            finish();
            }
        });
        builder.show();
    }
    public void GetMoviesDetails(String Url){
          final JsonObjectRequest request=new JsonObjectRequest(Request.Method.GET, Url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                myData.clear();
                adapter.notifyDataSetChanged();
                try {
                    JSONArray resultarray=response.getJSONArray("results");
                    int size=resultarray.length();
                    for(int i=0;i<size;i++){
                        MovieModelData data=new MovieModelData();
                          JSONObject object=resultarray.getJSONObject(i);
                          data.setId(object.getInt("id"));
                          data.setName(object.getString("title"));
                          data.setOverview(object.getString("overview"));
                          data.setRate(object.getInt("vote_average"));
                          data.setReleaseDate(object.getString("release_date"));
                          data.setPosterMovie(object.getString("poster_path"));
                          data.setBackgroundMovie(object.getString("backdrop_path"));
                       //   Toast.makeText(MainActivity.this, "Name"+data.getName(), Toast.LENGTH_LONG).show();
                        myData.add(data);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                adapter.notifyDataSetChanged();
                if(State!=null){
                    manager.onRestoreInstanceState(State);
                }
                Log.e("morse",String .valueOf(myData.size()));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Can`t get Data From Server", Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(request);
    }
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
     if(imageView.getVisibility()==View.VISIBLE) {
         imageView.setVisibility(View.INVISIBLE);
         MyrecyclerView.setVisibility(View.VISIBLE);
     }
        //Toast.makeText(MainActivity.this, "This Shared Preference Switched ", Toast.LENGTH_LONG).show();
        String movieChoise=sharedPreferences.getString("movies","Populer");
        if(movieChoise.equals("Populer")){
          FinalUrl=uri.getPopularUrl();
        }
        else {
            FinalUrl=uri.getTopRated();
        }
        IamVisitFavPage=false;
        GetMoviesDetails(FinalUrl);
    }
    public String GetFinalUrl(){
    SharedPreferences preferences= android.support.v7.preference.PreferenceManager.getDefaultSharedPreferences(this);
    String movieChoise=preferences.getString("movies","Populer");
    if(movieChoise.equals("Populer")){
        FinalUrl=uri.getPopularUrl();

    }
    else if(movieChoise.equals("Top Rated")){
        FinalUrl=uri.getTopRated();
    }
    imageView.setVisibility(View.INVISIBLE);
    MyrecyclerView.setVisibility(View.VISIBLE);
    return FinalUrl;
}
    @Override
    protected void onPause() {
        Log.e("Cycle", "onPause: ");
        super.onPause();
}

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        State=manager.onSaveInstanceState();
        outState.putParcelable("Position",State);
        outState.putBoolean("IfFavourite",IamVisitFavPage);
        //Log.e("LifeCycle","OnSavedInstance X "+((LinearLayoutManager)MyrecyclerView.getLayoutManager()).findFirstVisibleItemPosition());
        Log.e("LifeCycle","OnSaveInstance "+IamVisitFavPage);
        super.onSaveInstanceState(outState);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if(savedInstanceState!=null) {
            State = savedInstanceState.getParcelable("Position");
            IamVisitFavPage=savedInstanceState.getBoolean("IfFavourite");
            Log.e("LifeCycle","OnRestoreInstance "+IamVisitFavPage);
        }

    }

    @Override
    protected void onDestroy() {
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
        Log.e("Cycle", "onDestroy: ");
        super.onDestroy();
    }
    public ArrayList<MovieModelData> OperationInCursor(Cursor cursor) throws JSONException {
        ArrayList<MovieModelData> data =new ArrayList<>();
            cursor.moveToFirst();
            MovieModelData modelData;
            for(int i=0;i<cursor.getCount();i++){
                modelData=new MovieModelData();
                modelData.setId(cursor.getInt(cursor.getColumnIndex(MovieContract.id)));
                modelData.setName(cursor.getString(cursor.getColumnIndex(MovieContract.name)));
                modelData.PosterMovie=cursor.getString(cursor.getColumnIndex(MovieContract.postermovie));
                modelData.BackgroundMovie=cursor.getString(cursor.getColumnIndex(MovieContract.backgroundmovie));
                modelData.setRate(cursor.getInt(cursor.getColumnIndex(MovieContract.rate)));
                modelData.setOverview(cursor.getString(cursor.getColumnIndex(MovieContract.overview)));
                modelData.setReleaseDate(cursor.getString(cursor.getColumnIndex(MovieContract.releasedate)));
                String Review=cursor.getString(cursor.getColumnIndex(MovieContract.reviewdata));
                String Trailer=cursor.getString(cursor.getColumnIndex(MovieContract.trailerdata));
                // ArrayList<ReviewData> ReviewArray = new Gson().fromJson(Review, new TypeToken<ArrayList<ReviewData>>(){}.getType());
                ArrayList<ReviewData> ReviewArray=ReturnReviewsAsArrayList(Review);
                modelData.setReviewData(ReviewArray);
                //ArrayList<TrailerData> TrailerArray = new Gson().fromJson(Trailer, new TypeToken<ArrayList<TrailerData>>(){}.getType());
                ArrayList<TrailerData> TrailerArray=ReturnTrailerAsArrayList(Trailer);
                modelData.setTrailerData(TrailerArray);
                data.add(modelData);
                cursor.moveToNext();
            }
            cursor.close();
            return data;
    }
    public ArrayList<ReviewData> ReturnReviewsAsArrayList(String data) throws JSONException {
        ArrayList<ReviewData> dataArrayList=new ArrayList<>();
        JSONArray array=new JSONArray(data);
        ReviewData redata;
        for (int i=0;i<array.length();i++){
            redata=new ReviewData();
            JSONObject object=array.getJSONObject(i);
            redata.setName(object.getString("name"));
            redata.setReview(object.getString("review"));
            dataArrayList.add(redata);
        }
        return dataArrayList;
    }
    public ArrayList<TrailerData> ReturnTrailerAsArrayList(String data) throws JSONException {
        ArrayList<TrailerData> dataArrayList=new ArrayList<>();
        JSONArray array=new JSONArray(data);
        TrailerData redata;
        for (int i=0;i<array.length();i++){
            redata=new TrailerData();
            JSONObject object=array.getJSONObject(i);
            redata.setName(object.getString("name"));
            redata.setKey(object.getString("key"));
            redata.setSize(object.getString("size"));
            dataArrayList.add(redata);
        }
        return dataArrayList;
    }
    public void Refresh(View view) {
        FinalUrl= GetFinalUrl();
        GetMoviesDetails(FinalUrl);
        button.setVisibility(View.INVISIBLE);
        ifNotConnect=false;
    }
}
