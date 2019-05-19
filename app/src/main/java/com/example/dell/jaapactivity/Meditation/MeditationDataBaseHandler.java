package com.example.dell.jaapactivity.Meditation;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import static com.example.dell.jaapactivity.Meditation.MeditationContractClass.WaitListEntryMed.KEY_AUDIO_DURATION;
import static com.example.dell.jaapactivity.Meditation.MeditationContractClass.WaitListEntryMed.KEY_AUDIO_NAME;
import static com.example.dell.jaapactivity.Meditation.MeditationContractClass.WaitListEntryMed.KEY_ID;
import static com.example.dell.jaapactivity.Meditation.MeditationContractClass.WaitListEntryMed.TABLE_MEDITATION_DATA;

public class MeditationDataBaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "meditationReportManager.db";
   public  MeditationDataBaseHandler(Context context){
       super(context,DATABASE_NAME,null,DATABASE_VERSION);
   }

    @Override
    public void onCreate(SQLiteDatabase db) {
       final String SQL_CREATE_WAITLIST_TABLE_MED = "CREATE TABLE " +
               TABLE_MEDITATION_DATA + " ("
               + KEY_ID +" INTEGER PRIMARY KEY AUTOINCREMENT, "
               + KEY_AUDIO_NAME +" TEXT NOT NULL, "
               + KEY_AUDIO_DURATION +" INTEGER); ";

       db.execSQL(SQL_CREATE_WAITLIST_TABLE_MED);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
      db.execSQL("DROP TABLE IF EXISTS "+ TABLE_MEDITATION_DATA);
      onCreate(db);
    }

    public long addMeditationData(MeditationData meditationData){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values =new ContentValues();
        values.put(KEY_AUDIO_NAME,meditationData.getAudioName());
        values.put(KEY_AUDIO_DURATION,meditationData.getDuration());

        return db.insert(TABLE_MEDITATION_DATA,null,values);
    }
    public List<MeditationData> getAllMeditationData() {
       List<MeditationData> meditationDataList = new ArrayList<MeditationData>();
       //select all query
        String selectQuery = "SELECT * FROM "+TABLE_MEDITATION_DATA;

        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor= db.rawQuery(selectQuery,null);

        if(cursor.moveToFirst()){
                do {
                    MeditationData meditationData = new MeditationData();
                    meditationData.setId(Integer.parseInt(cursor.getString(0)));
                    meditationData.setAudioName(cursor.getString(1));
                    meditationData.setDuration(Integer.parseInt(cursor.getString(2)));
                    meditationDataList.add(meditationData);
                }
                while (cursor.moveToNext());
        }
        //return
        return meditationDataList;
    }
    public int totalSong1(){
        int totalByTime= 0;
        String byTimeQuery = "SELECT "+ KEY_AUDIO_NAME + " FROM "  + TABLE_MEDITATION_DATA +
                " WHERE "+ KEY_AUDIO_NAME +"= 'No 1 Atam Bodh Dhyaan'";
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(byTimeQuery,null);
        totalByTime = cursor.getCount();
        cursor.close();
        return  totalByTime;
    }
    public int totalSong2(){
        int totalByTime= 0;
        String byTimeQuery = "SELECT "+ KEY_AUDIO_NAME + " FROM "  + TABLE_MEDITATION_DATA +
                " WHERE "+ KEY_AUDIO_NAME +"= 'No 2 Panchkosh Dhyaan'";
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(byTimeQuery,null);
        totalByTime = cursor.getCount();
        cursor.close();
        return  totalByTime;
    }
    public int totalSong3(){
        int totalByTime= 0;
        String byTimeQuery = "SELECT "+ KEY_AUDIO_NAME + " FROM "  + TABLE_MEDITATION_DATA +
                " WHERE "+ KEY_AUDIO_NAME +"= 'No 3 Sharir Dhyaan'";
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(byTimeQuery,null);
        totalByTime = cursor.getCount();
        cursor.close();
        return  totalByTime;
    }
    public int totalSong4(){
        int totalByTime= 0;
        String byTimeQuery = "SELECT "+ KEY_AUDIO_NAME + " FROM "  + TABLE_MEDITATION_DATA +
                " WHERE "+ KEY_AUDIO_NAME +"= 'No 4 Amrit Varsha Dhyaan'";
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(byTimeQuery,null);
        totalByTime = cursor.getCount();
        cursor.close();
        return  totalByTime;
    }
    public int totalSong5(){
        int totalByTime= 0;
        String byTimeQuery = "SELECT "+ KEY_AUDIO_NAME + " FROM "  + TABLE_MEDITATION_DATA +
                " WHERE "+ KEY_AUDIO_NAME +"= 'No 5 Jyoti Avdhrnam Dhyaan'";
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(byTimeQuery,null);
        totalByTime = cursor.getCount();
        cursor.close();
        return  totalByTime;
    }
    public int totalSong6(){
        int totalByTime= 0;
        String byTimeQuery = "SELECT "+ KEY_AUDIO_NAME + " FROM "  + TABLE_MEDITATION_DATA +
                " WHERE "+ KEY_AUDIO_NAME +"= 'No 6 Naad yog Dhyaan'";
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(byTimeQuery,null);
        totalByTime = cursor.getCount();
        cursor.close();
        return  totalByTime;
    }
    public int totalSong7(){
        int totalByTime= 0;
        String byTimeQuery = "SELECT "+ KEY_AUDIO_NAME + " FROM "  + TABLE_MEDITATION_DATA +
                " WHERE "+ KEY_AUDIO_NAME +"= 'No 7 Tatv Bodh Dhyaan'";
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(byTimeQuery,null);
        totalByTime = cursor.getCount();
        cursor.close();
        return  totalByTime;
    }



}
