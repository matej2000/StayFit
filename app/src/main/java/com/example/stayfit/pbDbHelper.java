package com.example.stayfit;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class pbDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "StayFit.db";


    public pbDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(pbContract.SQL_CREATE_ENTRIES);
        sqLiteDatabase.execSQL(pbContract.SQL_CREATE_ENTRIES_FOOD);
        sqLiteDatabase.execSQL(pbContract.SQL_CREATE_ENTRIES_FOOD_AVRAGE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(pbContract.SQL_DELETE_ENTRIES);
        onCreate(sqLiteDatabase);
    }
}
