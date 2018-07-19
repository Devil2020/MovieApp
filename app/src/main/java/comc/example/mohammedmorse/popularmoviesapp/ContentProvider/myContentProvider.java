package comc.example.mohammedmorse.popularmoviesapp.ContentProvider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;

import org.json.JSONException;

import java.util.ArrayList;

import comc.example.mohammedmorse.popularmoviesapp.DataBase.MovieContract;
import comc.example.mohammedmorse.popularmoviesapp.DataBase.MovieDataBase;
import comc.example.mohammedmorse.popularmoviesapp.Model.MovieModelData;

/**
 * Created by Mohammed Morse on 06/07/2018.
 */

public class myContentProvider extends ContentProvider {
    public MovieDataBase dataBase;
   public UriMatcher matcher;
    @Override
    public boolean onCreate() {
        dataBase=new MovieDataBase(getContext());
        matcher=Match();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
       int Num= matcher.match(uri);
       Cursor myCursor=null;
       if(Num==2){
           myCursor=dataBase.getReadableDatabase().query(MovieContract.TableName,
                   new String[]{MovieContract.id,MovieContract.name,MovieContract.postermovie,MovieContract.backgroundmovie,
                   MovieContract.releasedate,MovieContract.rate,MovieContract.overview,MovieContract.reviewdata,MovieContract.trailerdata}
                   ,null,null,null,null,null);

//myCursor.close();
       }
        else if(Num==4){
           myCursor=dataBase.getReadableDatabase().query(MovieContract.TableName,new String[]{MovieContract.id,MovieContract.name,
                   MovieContract.postermovie,MovieContract.backgroundmovie,
                   MovieContract.releasedate,MovieContract.rate,MovieContract.overview,
                   MovieContract.reviewdata,MovieContract.trailerdata},
                   MovieContract.name +"=?", new String[]{selectionArgs[0]},
                   null,null,null,null);
           //Not Implemented Yet For Select Specific Film
     //  myCursor.close();

       }
       // dataBase.close();
        return myCursor;
    }
public UriMatcher Match(){
        matcher=new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(ContentProviderContract.AUTHONTCATION,ContentProviderContract.TABLEPATH,2);
        matcher.addURI(ContentProviderContract.AUTHONTCATION,ContentProviderContract.TABLEPATH+"/*",4);
        return matcher;
}
    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
      int num=matcher.match(uri);
        if(num==2) {
            dataBase.getWritableDatabase().insert(MovieContract.TableName,null,values);
            dataBase.close();
        }
        return null;
    }
    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int Num=matcher.match(uri);
        if(Num==2)
        {
            dataBase.getWritableDatabase().delete(MovieContract.TableName,selection, new String[]{String.valueOf(selectionArgs)});
            dataBase.close();
        }
        else
            dataBase.getWritableDatabase().delete(MovieContract.TableName,MovieContract.name+"=?", new String[]{String.valueOf(selectionArgs[0])});
        dataBase.close();
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
