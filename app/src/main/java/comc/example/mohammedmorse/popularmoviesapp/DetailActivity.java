package comc.example.mohammedmorse.popularmoviesapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import butterknife.BindView;

public class DetailActivity extends AppCompatActivity {
ImageView FrontImage;
ImageView BackImage;
TextView Name;
TextView Rate;
TextView Overview;
TextView Date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

         //FrontImage=findViewById(R.id.MovieFrontImage);
         BackImage=findViewById(R.id.MovieBackroundImage);
         Name=findViewById(R.id.MovieNameDetail);
         Rate=findViewById(R.id.MovieRateDetail);
         Overview=findViewById(R.id.MovieOverviewDetail);
         Date=findViewById(R.id.MovieDateDetail);
         SetData();
    }
public void SetData(){
        Intent intent=getIntent();
        MovieModelData data=new MovieModelData();
        data.setName(intent.getStringExtra("Name"));
        setTitle(data.getName());
        data.setOverview(intent.getStringExtra("Overview"));
        data.setRate(intent.getIntExtra("Rate",0));
        data.setReleaseDate(intent.getStringExtra("Date"));
        String background=intent.getStringExtra("Background");
       String frontimage=intent.getStringExtra("Poster");
        Picasso.with(this).load(background).into(BackImage);
       // Picasso.with(this).load(frontimage).into(FrontImage);
        Name.setText(data.getName());
        Rate.setText(String .valueOf(data.getRate())+"/10");
        Overview.setText(data.getOverview());
        Date.setText(data.getReleaseDate());
   // Toast.makeText(this, data.getName()+" is a very good Chooise for watching it ", Toast.LENGTH_LONG).show();
}

}
