package com.example.dell.jaapactivity.Jap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import static com.example.dell.jaapactivity.Jap.JapContractClass.WaitListEntry;
import static com.example.dell.jaapactivity.Jap.JapContractClass.WaitListEntry.HAS_VIDEO;
import static com.example.dell.jaapactivity.Jap.JapContractClass.WaitListEntry.KEY_TIME;
import static com.example.dell.jaapactivity.Jap.JapContractClass.WaitListEntry.KEY_TYPE;
import static com.example.dell.jaapactivity.Jap.JapContractClass.WaitListEntry.TABLE_JAP_DATA;
import static com.example.dell.jaapactivity.Jap.JapContractClass.WaitListEntry.VIDEO_URL;

public class JapDatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static  final String DATABASE_NAME = "japReportManager.db";



    public JapDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_WAITLIST_TABLE = "CREATE TABLE " +
                WaitListEntry.TABLE_JAP_DATA + " ("
                +WaitListEntry.KEY_ID +" INTEGER PRIMARY KEY AUTOINCREMENT, "
                +WaitListEntry.KEY_TYPE +" TEXT NOT NULL, "
                +WaitListEntry.KEY_TIME +" INTEGER, "
                +WaitListEntry.HAS_VIDEO +" INTEGER, "
                +WaitListEntry.VIDEO_URL + " TEXT); ";

        db.execSQL(SQL_CREATE_WAITLIST_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ WaitListEntry.TABLE_JAP_DATA);
        onCreate(db);

    }
    //code to add new Jap Data
    public long addJapData(JapData japData){
        SQLiteDatabase db = getWritableDatabase();ContentValues values = new ContentValues();
        values.put(KEY_TYPE,japData.getType());
        values.put(KEY_TIME,japData.getTime());
        values.put(HAS_VIDEO,japData.isHasVideo());
        values.put(VIDEO_URL,japData.getVideoURl());

        //inserting row

         return db.insert(TABLE_JAP_DATA,null,values);


    }

    //code to get all japData in a list view
    public List<JapData> getAllJapData() {
        List<JapData> japDataList = new ArrayList<JapData>();
        //select all query
        String selectQuery = "SELECT * FROM "+TABLE_JAP_DATA;

        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);

        //looping through all rows and adding to the list
        if(cursor.moveToFirst()){
            do {
                JapData japData = new JapData();
                japData.setId(Integer.parseInt(cursor.getString(0)));
                japData.setType(cursor.getString(1));
                japData.setTime(cursor.getLong(2));
                japData.setHasVideo(cursor.getInt(3));
                japData.setVideoURl(cursor.getString(4));
                japDataList.add(japData);
            }
            while (cursor.moveToNext());
        }
        //return jap data list
        return japDataList;
    }
    //getting japData counts
   public int getJapDataCounts() {
        String countQuery = "SELECT * FROM " + TABLE_JAP_DATA;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery,null);
        cursor.close();

        //return count
       return cursor.getCount();
   }

   public int totalbyTime(){
        int totalByTime= 0;
        String byTimeQuery = "SELECT "+ KEY_TYPE + " FROM "  + TABLE_JAP_DATA +
                " WHERE "+ KEY_TYPE +"= 'by Time'";
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(byTimeQuery,null);
        totalByTime = cursor.getCount();
        cursor.close();
        return  totalByTime;
   }
    public int totalbyMala(){
        int totalByMala= 0;
        String byTimeQuery = "SELECT "+ KEY_TYPE + " FROM "  + TABLE_JAP_DATA +
                " WHERE "+ KEY_TYPE +"= 'by Mala'";
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(byTimeQuery,null);
        totalByMala = cursor.getCount();
        cursor.close();
        return  totalByMala;
    }
    public int totalwithGurudev(){
        int totalwithGurudev= 0;
        String byTimeQuery = "SELECT "+ KEY_TYPE + " FROM "  + TABLE_JAP_DATA +
                " WHERE "+ KEY_TYPE +"= 'with Pujya Gurudev'";
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(byTimeQuery,null);
        totalwithGurudev = cursor.getCount();
        cursor.close();
        return  totalwithGurudev;
    }
    public int totalwithMataji(){
        int totalWithMataji= 0;
        String byTimeQuery = "SELECT "+ KEY_TYPE + " FROM "  + TABLE_JAP_DATA +
                " WHERE "+ KEY_TYPE +"= 'with Pujya Mataji'";
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(byTimeQuery,null);
        totalWithMataji = cursor.getCount();
        cursor.close();
        return  totalWithMataji;
    }
}
