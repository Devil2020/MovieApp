package comc.example.mohammedmorse.popularmoviesapp.ContentProvider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.json.JSONException;

import java.util.ArrayList;

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
           try {
               myCursor=dataBase.Select();

           } catch (JSONException e) {
               e.printStackTrace();
           }
       }
        else if(Num==4){
           //Not Implemented Yet For Select Specific Film
       }
        return myCursor;
    }
public UriMatcher Match(){
        matcher=new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(ContentProviderContract.AUTHONTCATION,ContentProviderContract.TABLEPATH,2);
        matcher.addURI(ContentProviderContract.AUTHONTCATION,ContentProviderContract.TABLEPATH+"/#",4);
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
            try {
                dataBase.Insert(values);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int Num=matcher.match(uri);
        if(Num==4)
        {
            dataBase.Delet(Integer.valueOf(selectionArgs[0]).toString());
        }
        else
            dataBase.Delet(Integer.valueOf(selectionArgs[0]).toString());
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
