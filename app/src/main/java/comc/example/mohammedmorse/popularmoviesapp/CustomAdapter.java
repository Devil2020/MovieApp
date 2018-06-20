package comc.example.mohammedmorse.popularmoviesapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Mohammed Morse on 16/06/2018.
 */

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.Holder> {
    Context context;
    ArrayList<MovieModelData> myData;
    public CustomAdapter(Context context, ArrayList<MovieModelData> myData){
        this.context=context;
        this.myData=myData;
    }
    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.main_activity_recycler_view_item,parent,false);
        Holder holder=new Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(Holder holder, final int position) {
       Picasso.with(context).load(myData.get(position).getPosterMovie()).into(holder.MoviePoster);
       // holder.MoviePoster.setText(myData.get(position).getName());
        holder.myLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,DetailActivity.class);
               intent.putExtra("Name",myData.get(position).getName());
                intent.putExtra("Date",myData.get(position).getReleaseDate());
                intent.putExtra("Rate",myData.get(position).getRate());
                intent.putExtra("Overview",myData.get(position).getOverview());
                intent.putExtra("Background",myData.get(position).getBackgroundMovie());
                intent.putExtra("Poster",myData.get(position).getPosterMovie());
                // Toast.makeText(context, "The poster Url is "+myData.get(position).getPosterMovie(), Toast.LENGTH_SHORT).show();
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return myData.size();
    }

    class Holder extends RecyclerView.ViewHolder{
        public ImageView MoviePoster;
        public LinearLayout myLayout;
        public Holder(View itemView) {
            super(itemView);
        MoviePoster=itemView.findViewById(R.id.MoviePosterImage);
        myLayout=itemView.findViewById(R.id.constrain);
        }
    }
}
