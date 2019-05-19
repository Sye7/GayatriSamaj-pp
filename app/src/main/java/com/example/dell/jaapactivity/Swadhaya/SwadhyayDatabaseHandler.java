package com.example.dell.jaapactivity.Swadhaya;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import static com.example.dell.jaapactivity.Swadhaya.SwadhyayContractClass.WaitListEntrySwadhyay.KEY_ID;
import static com.example.dell.jaapactivity.Swadhaya.SwadhyayContractClass.WaitListEntrySwadhyay.KEY_TIME;
import static com.example.dell.jaapactivity.Swadhaya.SwadhyayContractClass.WaitListEntrySwadhyay.TABLE_SWADHYAY_DATA;

public class SwadhyayDatabaseHandler extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "swadhyayReportManager.db";

    public SwadhyayDatabaseHandler(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_WAITLIST_TABLE_SWADHYAY = "CREATE TABLE "+
                TABLE_SWADHYAY_DATA + "( "
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_TIME + " INTEGER); ";
        db.execSQL(SQL_CREATE_WAITLIST_TABLE_SWADHYAY);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
       db.execSQL("DROP TABLE IF EXISTS "+TABLE_SWADHYAY_DATA);
       onCreate(db);
    }

    public long addSwadhyay(SwadhyayData swadhyayData){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TIME,swadhyayData.getTime());
        return db.insert(TABLE_SWADHYAY_DATA,null,values);
    }
    public List<SwadhyayData> getAllSwadhyayData() {
        List<SwadhyayData> swadhyayDataList = new ArrayList<SwadhyayData>();
        //select all query
        String selectQuery = "SELECT * FROM "+TABLE_SWADHYAY_DATA;

        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor= db.rawQuery(selectQuery,null);

        if(cursor.moveToFirst()){
            do{
                SwadhyayData swadhyayData = new SwadhyayData();
                swadhyayData.setId(Integer.parseInt(cursor.getString(0)));
                swadhyayData.setTime(Long.parseLong(cursor.getString(1)));
                swadhyayDataList.add(swadhyayData);
            }
            while (cursor.moveToNext());
        }
        return swadhyayDataList;
    }
}
