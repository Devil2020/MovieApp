package comc.example.mohammedmorse.popularmoviesapp.ContentProvider;

import android.net.Uri;

import comc.example.mohammedmorse.popularmoviesapp.DataBase.MovieContract;
import comc.example.mohammedmorse.popularmoviesapp.Model.MovieModelData;

/**
 * Created by Mohammed Morse on 06/07/2018.
 */

public class ContentProviderContract {
    static final public String SCHEMA="content://";
    static final public String AUTHONTCATION="comc.example.mohammedmorse.popularmoviesapp";
    static final public String TABLEPATH= MovieContract.TableName;
    static final public Uri URITOTABLE=Uri.parse(SCHEMA+AUTHONTCATION+"/"+TABLEPATH);
    static final public Uri URITOTABLEBYNAME=Uri.parse(SCHEMA+AUTHONTCATION+"/"+TABLEPATH+"/##");
}
