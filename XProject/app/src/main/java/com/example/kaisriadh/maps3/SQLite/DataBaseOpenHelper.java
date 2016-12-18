package com.example.kaisriadh.maps3.SQLite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by riadh on 12/13/2016.
 */

public class DataBaseOpenHelper extends SQLiteOpenHelper {
    public static  final int dbversion = 1;
    public static  final String dbname="NearbyChat.db";

    private static final String SQLCreateTableLogin =
     "CREATE TABLE IF NOT EXISTS "+DatabaseContract.Login.tablename+
     " ("+DatabaseContract.Login._ID + " INTEGER PRIMARY KEY,"+
     DatabaseContract.Login.colmail+" "+DatabaseContract.Login.colmailType+", "+
     DatabaseContract.Login.colpass+" "+DatabaseContract.Login.colpassType+" "+" )";


    private static final String SQLDelteTableLogin =
  "DROP TABLE IF EXISTS " +  DatabaseContract.Login.tablename;

    public DataBaseOpenHelper(Context context){
        super(context , dbname, null, dbversion) ;
    }

/*
    public DataBaseOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
*/
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQLCreateTableLogin);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(SQLDelteTableLogin);
        onCreate(sqLiteDatabase);
    }

    @Override
    public void onDowngrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        onUpgrade(sqLiteDatabase,i1,i);
    }

}
