package comc.example.mohammedmorse.popularmoviesapp.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import comc.example.mohammedmorse.popularmoviesapp.Model.MovieModelData;
import comc.example.mohammedmorse.popularmoviesapp.Model.ReviewData;
import comc.example.mohammedmorse.popularmoviesapp.Model.TrailerData;

/**
 * Created by Mohammed Morse on 29/06/2018.
 */

public class MovieDataBase extends SQLiteOpenHelper {
    public final static String DataBaseName="TodoList";
    public final static int DataBaseVersion=9;
    public String Review,Trailer;
    JSONArray array1=new JSONArray();
    JSONArray array2=new JSONArray();
    public MovieDataBase(Context context) {
        super(context, DataBaseName, null, DataBaseVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    db.execSQL("CREATE TABLE "+MovieContract.TableName+"( "+MovieContract.id +" Integer Primary Key , "+MovieContract.name+" Text , "+
    MovieContract.postermovie +" Text , "+MovieContract.backgroundmovie +" Text , "+MovieContract.releasedate +" Text , "+MovieContract.rate+" Integer , "+
            MovieContract.overview +" Text , "+MovieContract.reviewdata+" Text , "+MovieContract.trailerdata+" Text );");
    }


  /*  public void Insert(ContentValues data) throws JSONException {
        SQLiteDatabase database=getWritableDatabase();
       /* ContentValues values=new ContentValues();
        values.put(MovieContract.id,data.getId());
        values.put(MovieContract.name,data.getName());
        values.put(MovieContract.postermovie,data.getPosterMovie());
        values.put(MovieContract.backgroundmovie,data.getBackgroundMovie());
        values.put(MovieContract.releasedate,data.getReleaseDate());
        values.put(MovieContract.rate,data.getRate());
        values.put(MovieContract.overview,data.getOverview());
        array1=ReturnReviewsAsJson(data);
        String MyReview=array1.toString();
       array2=ReturnTrailersAsJson(data);
        String MyTrailer=array2.toString();
        values.put(MovieContract.reviewdata,MyReview);
        values.put(MovieContract.trailerdata,MyTrailer);
        */
     /*   database.insert(MovieContract.TableName,null,data);
        database.close();
    }
    public void Delet(String Arg ){
        SQLiteDatabase database=getWritableDatabase();
        database.delete(MovieContract.TableName,MovieContract.id+" =? ", new String[]{Arg});
       //   database.delete(MovieContract.TableName,MovieContract.id,new String[]{String.valueOf(data.getId())});
          database.close();
   }*/
  /*  public ArrayList<MovieModelData> Select() throws JSONException {
       ArrayList<MovieModelData> list=new ArrayList<>();
       SQLiteDatabase database=getReadableDatabase();
       Cursor cursor=database.query(MovieContract.TableName,new String[]{MovieContract.id,MovieContract.name,MovieContract.postermovie,MovieContract.backgroundmovie,
               MovieContract.releasedate,MovieContract.rate,MovieContract.overview,MovieContract.reviewdata,MovieContract.trailerdata},null,null,null,null,null);
        cursor.moveToFirst();
       MovieModelData data;
        for(int i=0;i<cursor.getCount();i++){
            data=new MovieModelData();
            data.setId(cursor.getInt(cursor.getColumnIndex(MovieContract.id)));
            data.setName(cursor.getString(cursor.getColumnIndex(MovieContract.name)));
            data.PosterMovie=cursor.getString(cursor.getColumnIndex(MovieContract.postermovie));
            data.BackgroundMovie=cursor.getString(cursor.getColumnIndex(MovieContract.backgroundmovie));
            data.setRate(cursor.getInt(cursor.getColumnIndex(MovieContract.rate)));
            data.setOverview(cursor.getString(cursor.getColumnIndex(MovieContract.overview)));
            data.setReleaseDate(cursor.getString(cursor.getColumnIndex(MovieContract.releasedate)));
            String Review=cursor.getString(cursor.getColumnIndex(MovieContract.reviewdata));
            String Trailer=cursor.getString(cursor.getColumnIndex(MovieContract.trailerdata));
           // ArrayList<ReviewData> ReviewArray = new Gson().fromJson(Review, new TypeToken<ArrayList<ReviewData>>(){}.getType());
            ArrayList<ReviewData> ReviewArray=ReturnReviewsAsArrayList(Review);
            data.setReviewData(ReviewArray);
            //ArrayList<TrailerData> TrailerArray = new Gson().fromJson(Trailer, new TypeToken<ArrayList<TrailerData>>(){}.getType());
            ArrayList<TrailerData> TrailerArray=ReturnTrailerAsArrayList(Trailer);
            data.setTrailerData(TrailerArray);
           list.add(data);
           cursor.moveToNext();
        }
        database.close();
        cursor.close();
       return list;
   }*/
  /*public Cursor Select() throws JSONException {

      SQLiteDatabase database=getReadableDatabase();
      Cursor cursor=database.query(MovieContract.TableName,new String[]{MovieContract.id,MovieContract.name,MovieContract.postermovie,MovieContract.backgroundmovie,
              MovieContract.releasedate,MovieContract.rate,MovieContract.overview,MovieContract.reviewdata,MovieContract.trailerdata},null,null,null,null,null);
      return cursor;
  }*/
    /*public boolean IfExistInDataBaseOrNot(String MovieName){
       boolean ifExists=false;
       SQLiteDatabase database=getReadableDatabase();
       Cursor cursor=database.query(MovieContract.TableName,new String[]{MovieContract.id,MovieContract.name,MovieContract.postermovie,MovieContract.backgroundmovie,
               MovieContract.releasedate,MovieContract.rate,MovieContract.overview,Review,Trailer},MovieContract.name+"=?",new String[]{MovieName},null,null,null,null);
       if(cursor.getCount()!=0){
           ifExists=true;
       }
       return ifExists;
   }*/
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
       db.execSQL("DROP TABLE IF EXISTS "+MovieContract.TableName);
       onCreate(db);
    }
}
