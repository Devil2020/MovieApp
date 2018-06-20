package comc.example.mohammedmorse.popularmoviesapp;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
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

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import butterknife.BindView;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
     String PopularUrl="http://api.themoviedb.org/3/movie/popular?api_key=12ae0210d107863fd1d89b1e2ee1f26a";
     String TopRated="http://api.themoviedb.org/3/movie/top_rated?api_key=12ae0210d107863fd1d89b1e2ee1f26a";
     String FinalUrl=null;
     ArrayList<MovieModelData> myData;
     CustomAdapter adapter;
    RecyclerView.LayoutManager manager;
    RequestQueue requestQueue;
     ProgressBar progressBar;
    RecyclerView MyrecyclerView;
    boolean networkAvailability;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Movies");
        myData=new ArrayList<MovieModelData>();
        MyrecyclerView=findViewById(R.id.MyrecyclerList);
        manager=new GridLayoutManager(this,2);

        adapter=new CustomAdapter(this,myData);
        requestQueue=Volley.newRequestQueue(this);
        MyrecyclerView.setAdapter(adapter);
        MyrecyclerView.setLayoutManager(manager);
        networkAvailability=CheckTheNetwork();
        if(networkAvailability==false){
           // progressBar.setVisibility(View.INVISIBLE);
            ShowAlerDialog();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        FinalUrl=GetFinalUrl();
        GetMovies(FinalUrl);
    }

    @Override
    protected void onStart() {
        super.onStart();
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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
    public void GetMovies(String Url){
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
        String movieChoise=sharedPreferences.getString("movies","Populer");
        if(movieChoise.equals("Populer")){
          FinalUrl=PopularUrl;
        }
        else {
            FinalUrl=TopRated;
        }
       // GetMovies(FinalUrl);
    }
public String GetFinalUrl(){
    SharedPreferences preferences= android.support.v7.preference.PreferenceManager.getDefaultSharedPreferences(this);
    String movieChoise=preferences.getString("movies","Populer");
    if(movieChoise.equals("Populer")){
        FinalUrl=PopularUrl;
    }
    else if(movieChoise.equals("Top Rated")){
        FinalUrl=TopRated;
    }
    return FinalUrl;
}
    @Override
    protected void onStop() {
        super.onStop();
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }
}
