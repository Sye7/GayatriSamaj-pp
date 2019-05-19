package com.example.dell.jaapactivity.ReportManager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import static com.example.dell.jaapactivity.ReportManager.ReportDataContractClass.WaitListEntryReport.KEY_ACTUAL_TIME;
import static com.example.dell.jaapactivity.ReportManager.ReportDataContractClass.WaitListEntryReport.KEY_AUDIO_NAME;
import static com.example.dell.jaapactivity.ReportManager.ReportDataContractClass.WaitListEntryReport.KEY_DATE;
import static com.example.dell.jaapactivity.ReportManager.ReportDataContractClass.WaitListEntryReport.KEY_DAY;
import static com.example.dell.jaapactivity.ReportManager.ReportDataContractClass.WaitListEntryReport.KEY_ID;
import static com.example.dell.jaapactivity.ReportManager.ReportDataContractClass.WaitListEntryReport.KEY_MODE;
import static com.example.dell.jaapactivity.ReportManager.ReportDataContractClass.WaitListEntryReport.KEY_TIME;
import static com.example.dell.jaapactivity.ReportManager.ReportDataContractClass.WaitListEntryReport.KEY_TYPE;
import static com.example.dell.jaapactivity.ReportManager.ReportDataContractClass.WaitListEntryReport.KEY_USER_TIME;
import static com.example.dell.jaapactivity.ReportManager.ReportDataContractClass.WaitListEntryReport.KEY_YEAR;
import static com.example.dell.jaapactivity.ReportManager.ReportDataContractClass.WaitListEntryReport.TABLE_USER_REPORT;

public class ReportDataBaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "userReportManager.db";
    private static final String TAG = "ReportDataBaseHandler";


    public ReportDataBaseHandler(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);

    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_WAITLIST_TABLE_USER_REPORT = "CREATE TABLE "+
                TABLE_USER_REPORT + "( "
                +KEY_ID +" INTEGER PRIMARY KEY AUTOINCREMENT, "
                +KEY_MODE +" TEXT NOT NULL, "
                +KEY_USER_TIME +" INTEGER, "
                +KEY_ACTUAL_TIME +" INTEGER, "
                +KEY_DATE +" TEXT NOT NULL, "
                +KEY_TIME +" TEXT NOT NULL, "
                +KEY_DAY +" TEXT NOT NULL, "
                +KEY_TYPE +" TEXT, "
                +KEY_AUDIO_NAME +" TEXT, "
                +KEY_YEAR +" TEXT); ";

        db.execSQL(SQL_CREATE_WAITLIST_TABLE_USER_REPORT);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_USER_REPORT);
        onCreate(db);


    }
    public long addUserReportData(ReportData reportData){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_MODE,reportData.getMode());
        values.put(KEY_USER_TIME,reportData.getUserTime());
        values.put(KEY_ACTUAL_TIME,reportData.getActualTime());
        values.put(KEY_DATE,reportData.getDate());
        values.put(KEY_DAY,reportData.getDay());
        values.put(KEY_TIME,reportData.getTime());
        values.put(KEY_TYPE,reportData.getType());
        values.put(KEY_AUDIO_NAME,reportData.getAudioName());
        values.put(KEY_YEAR,reportData.getYear());

        return db.insert(TABLE_USER_REPORT,null,values);
    }
    public List<ReportData> getAllUserReportData() {
        List<ReportData> reportDataList = new ArrayList<ReportData>();

        String selectQuery = "SELECT * FROM "+TABLE_USER_REPORT;

        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);

        if(cursor.moveToFirst()){
            do{
                ReportData reportData = new ReportData();
                reportData.setId(Integer.parseInt(cursor.getString(0)));
                reportData.setMode(cursor.getString(1));
                reportData.setUserTime(Integer.parseInt(cursor.getString(2)));
                reportData.setActualTime(Float.parseFloat(cursor.getString(3)));
                reportData.setDate(cursor.getString(4));
                reportData.setTime(cursor.getString(5));
                reportData.setDay(cursor.getString(6));
                reportData.setType(cursor.getString(7));
                reportData.setAudioName(cursor.getString(8));
                reportData.setYear(cursor.getString(9));
                reportDataList.add(reportData);

            }
            while (cursor.moveToNext());

        }
        return reportDataList;
    }

    public int getLastId(){
        String query = "SELECT "+KEY_ID +" from "+TABLE_USER_REPORT+ " order by "+KEY_ID+" DESC limit 1";
        SQLiteDatabase db = getWritableDatabase();
        int lastId=1;
        Cursor c = db.rawQuery(query,null);
        if(c!=null && c.moveToFirst()){
           lastId = c.getInt(0);

        }
        return lastId;
    }

    public boolean updateData(String id,float actualTime){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cn = new ContentValues();
        cn.put(KEY_ACTUAL_TIME,actualTime);
        db.update(TABLE_USER_REPORT,cn," id = ?",new String[] { id });
       return true;
    }

    public float sumUserTime(){
         float total = 0f;
        SQLiteDatabase db = this.getWritableDatabase();
        String sumQuery = " SELECT SUM(" + KEY_USER_TIME +") as Total FROM "+ TABLE_USER_REPORT;
        Cursor c = db.rawQuery(sumQuery,null);
        if(c.moveToFirst()){
             total = c.getInt(c.getColumnIndex("Total"));

        }
        c.close();
        return total;

    }

    public float sumActualTime(){
        float total = 0f;
        SQLiteDatabase db = getWritableDatabase();
        String sumQuery = " SELECT SUM(" + KEY_ACTUAL_TIME +") as Total FROM "+ TABLE_USER_REPORT;
        Cursor c = db.rawQuery(sumQuery,null);
        if(c.moveToFirst()){
            total = c.getInt(c.getColumnIndex("Total"));

        }
       c.close();
        return total;
    }

    public int totalJaps() {
        int totaljaps = 0;
        SQLiteDatabase db= getWritableDatabase();
        String japQuery = " SELECT "+ KEY_MODE + " FROM " + TABLE_USER_REPORT +
                " WHERE " + KEY_MODE + "= 'Jap'";
        Cursor cursor = db.rawQuery(japQuery,null);
        totaljaps = cursor.getCount();
        cursor.close();
        return  totaljaps;

    }
    public int totalMeditations() {
        int totaljaps = 0;
        SQLiteDatabase db= getWritableDatabase();
        String japQuery = " SELECT "+ KEY_MODE + " FROM " + TABLE_USER_REPORT +
                " WHERE " + KEY_MODE + "= 'Meditation'";
        Cursor cursor = db.rawQuery(japQuery,null);
        totaljaps = cursor.getCount();
        cursor.close();
        return  totaljaps;

    }
    public int totalSwadhyay() {
        int totaljaps = 0;
        SQLiteDatabase db= getWritableDatabase();
        String japQuery = " SELECT "+ KEY_MODE + " FROM " + TABLE_USER_REPORT +
                " WHERE " + KEY_MODE + "= 'Swadhyay'";
        Cursor cursor = db.rawQuery(japQuery,null);
        totaljaps = cursor.getCount();
        cursor.close();
        return  totaljaps;

    }
    public int totalYagya() {
        int totaljaps = 0;
        SQLiteDatabase db= getWritableDatabase();
        String japQuery = " SELECT "+ KEY_MODE + " FROM " + TABLE_USER_REPORT +
                " WHERE " + KEY_MODE + "= 'Yagya'";
        Cursor cursor = db.rawQuery(japQuery,null);
        totaljaps = cursor.getCount();
        cursor.close();
        return  totaljaps;

    }

    //Displays total time spent  on different week days

    public int totalDaysTime(int d){

        String[] days  = new String[7];
        days[0] = "Mon";
        days[1] = "Tue";
        days[2] = "Wed";
        days[3] = "Thu";
        days[4] = "Fri";
        days[5] = "Sat";
        days[6] = "Sun";

        int totalTime = 0;
        SQLiteDatabase db = getWritableDatabase();
        String sumQuery = " SELECT SUM(" + KEY_ACTUAL_TIME +") as TotalDaysTime FROM "+ TABLE_USER_REPORT + " WHERE "
                +KEY_DAY +"= '"+days[d]+"'";
        Cursor cursor = db.rawQuery(sumQuery,null);

        if(cursor.moveToFirst()){
            totalTime = cursor.getInt(cursor.getColumnIndex("TotalDaysTime"));

        }
        cursor.close();
        return totalTime;


    }

   //calculates total time spent in current month
    public int totalMonthTime(int m) {
        int totalTime = 0;
        String[] months = new String[12];
        months[0]= "Jan";
        months[1] = "Feb";
        months[2]= "Mar";
        months[3] = "Apr";
        months[4]= "May";
        months[5] = "Jun";
        months[6]= "July";
        months[7] = "Aug";
        months[8]= "Sep";
        months[9] = "Oct";
        months[10] = "Nov";
        months[11] = "Dec";

        SQLiteDatabase db = getWritableDatabase();
        String janQuery = " SELECT SUM(" + KEY_ACTUAL_TIME +") as TotalMon FROM "+ TABLE_USER_REPORT + " WHERE "
                +KEY_DATE + "= '"+months[m]+"'";
        Cursor cursor = db.rawQuery(janQuery,null);


        if(cursor.moveToFirst()){
            totalTime = cursor.getInt(cursor.getColumnIndex("TotalMon"));

        }
        cursor.close();
        return totalTime;

    }


   //calculates total Times mode done ( not time )
    public int totalMonthModes(int t,int m){


        int titito = 0;
        String[] months = new String[12];
        months[0]= "Jan";
        months[1] = "Feb";
        months[2]= "Mar";
        months[3] = "Apr";
        months[4]= "May";
        months[5] = "Jun";
        months[6]= "July";
        months[7] = "Aug";
        months[8]= "Sep";
        months[9] = "Oct";
        months[10] = "Nov";
        months[11] = "Dec";

        String[] modes  = new String[4];
        modes[0]= "Jap";
        modes[1] = "Meditation";
        modes[2] = "Swadhyay";
        modes[3] = "yagya";
        SQLiteDatabase db = getWritableDatabase();
        String query = " SELECT "+ KEY_MODE + " FROM " + TABLE_USER_REPORT +
                " WHERE " + KEY_MODE + " = '"+modes[t]+"' AND " + KEY_DATE +" ='"+months[m]+"'";

        Cursor cursor = db.rawQuery(query,null);
        titito = cursor.getCount();
        cursor.close();
        return  titito;

    }


    public int totalTimeMonthModes(int t,int m){

        int titito = 0;
        String[] months = new String[12];
        months[0]= "Jan";
        months[1] = "Feb";
        months[2]= "Mar";
        months[3] = "Apr";
        months[4]= "May";
        months[5] = "Jun";
        months[6]= "July";
        months[7] = "Aug";
        months[8]= "Sep";
        months[9] = "Oct";
        months[10] = "Nov";
        months[11] = "Dec";

        String[] modes  = new String[4];
        modes[0]= "Jap";
        modes[1] = "Meditation";
        modes[2] = "Swadhyay";
        modes[3] = "yagya";



            SQLiteDatabase db = getWritableDatabase();
            String sumQuery = " SELECT SUM(" + KEY_ACTUAL_TIME +") as TotalExpJan FROM "
                    + TABLE_USER_REPORT + " WHERE "+ KEY_MODE + "= '"+modes[t]+"' AND " + KEY_DATE +" ='"+months[m]+"'";
            Cursor cr = db.rawQuery(sumQuery,null);
            if(cr.moveToFirst()){
                titito = cr.getInt(cr.getColumnIndex("TotalExpJan"));

            }
            cr.close();


        return titito;
    }

    //TODO: total time past one week
    //total time past week forward code if month doesn't change

    public int pastOneWeekF(int date,int month,int mode){
        int totalTimeOneWeek = 0;
        String[] months = new String[12];
        months[0]= "Jan";
        months[1] = "Feb";
        months[2]= "Mar";
        months[3] = "Apr";
        months[4]= "May";
        months[5] = "Jun";
        months[6]= "July";
        months[7] = "Aug";
        months[8]= "Sep";
        months[9] = "Oct";
        months[10] = "Nov";
        months[11] = "Dec";

        String[] modes  = new String[4];
        modes[0]= "Jap";
        modes[1] = "Meditation";
        modes[2] = "Swadhyay";
        modes[3] = "yagya";

        Integer[] days = new Integer[32];
        days[0]=1;
        days[1]=2;
        days[2]=3;
        days[3]=4;
        days[4]=5;
        days[5]=6;
        days[6]=7;
        days[7]=8;
        days[8]=9;
        days[9]=10;
        days[10]=11;
        days[11]=12;
        days[12]=13;
        days[13]=14;
        days[14]=15;
        days[15]=16;
        days[16]=17;
        days[17]=18;
        days[18]=19;
        days[19]=20;
        days[20]=21;
        days[21]=22;
        days[22]=23;
        days[23]=24;
        days[24]=25;
        days[25]=26;
        days[26]=27;
        days[27]=28;
        days[28]=29;
        days[29]=30;
        days[30]=31;
        days[31]=31;


        SQLiteDatabase db= getWritableDatabase();
        if(date>=7){
            Log.d(TAG, "pastOneWeekF: yes date is greater that 7  ");

        for(int i=date;i>=date-7;i--){

            Log.d(TAG, "pastOneWeekF: inside for loop ");
            String query = " SELECT SUM(" + KEY_ACTUAL_TIME +") as TotalTimePastWeekF FROM "
                    + TABLE_USER_REPORT + " WHERE "+ KEY_MODE + "= '"+modes[mode]+"' AND " + KEY_DATE +" ='"+months[month]+"' AND "
                    + KEY_TIME+ "= '"+days[i]+"'";

            Cursor cr = db.rawQuery(query,null);
            if(cr.moveToFirst()){
                totalTimeOneWeek = cr.getInt(cr.getColumnIndex("TotalTimePastWeekF"))+ totalTimeOneWeek;
                Log.d(TAG, "pastOneWeekF: total time "+  totalTimeOneWeek);
            }
            cr.close();

        }}
        else {

            for (int i = date;i>=1;i--){
                String query = " SELECT SUM(" + KEY_ACTUAL_TIME +") as TotalTimePastWeekF FROM "
                        + TABLE_USER_REPORT + " WHERE "+ KEY_MODE + "= '"+modes[mode]+"' AND " + KEY_DATE +" ='"+months[month]+"' AND "
                        + KEY_TIME+ "= '"+days[i]+"'";

                Cursor cr = db.rawQuery(query,null);
                if(cr.moveToFirst()){
                    totalTimeOneWeek = cr.getInt(cr.getColumnIndex("TotalTimePastWeekF"))+ totalTimeOneWeek;
                    Log.d(TAG, "pastOneWeekF: this week forward "+ totalTimeOneWeek);
                }
                cr.close();

            }
        }

        return totalTimeOneWeek;
    }

    //total time one week backward if month changes
    public int pastOneWeekB(int date,int month,int mode){
        int totalTimeOneWeek = 0;
        String[] months = new String[12];
        months[0]= "Jan";
        months[1] = "Feb";
        months[2]= "Mar";
        months[3] = "Apr";
        months[4]= "May";
        months[5] = "Jun";
        months[6]= "July";
        months[7] = "Aug";
        months[8]= "Sep";
        months[9] = "Oct";
        months[10] = "Nov";
        months[11] = "Dec";

        String[] modes  = new String[4];
        modes[0]= "Jap";
        modes[1] = "Meditation";
        modes[2] = "Swadhyay";
        modes[3] = "yagya";

        Integer[] days = new Integer[32];
        days[0]=1;
        days[1]=2;
        days[2]=3;
        days[3]=4;
        days[4]=5;
        days[5]=6;
        days[6]=7;
        days[7]=8;
        days[8]=9;
        days[9]=10;
        days[10]=11;
        days[11]=12;
        days[12]=13;
        days[13]=14;
        days[14]=15;
        days[15]=16;
        days[16]=17;
        days[17]=18;
        days[18]=19;
        days[19]=20;
        days[20]=21;
        days[21]=22;
        days[22]=23;
        days[23]=24;
        days[24]=25;
        days[25]=26;
        days[26]=27;
        days[27]=28;
        days[28]=29;
        days[29]=30;
        days[30]=31;
        days[31]=31;


        SQLiteDatabase db= getWritableDatabase();

            for(int i=date;i<=31;i++){

                Log.d(TAG, "pastOneWeekB: inside for loop past one week backward ");
                String query = " SELECT SUM(" + KEY_ACTUAL_TIME +") as TotalTimePastWeekB FROM "
                        + TABLE_USER_REPORT + " WHERE "+ KEY_MODE + "= '"+modes[mode]+"' AND " + KEY_DATE +" ='"+months[month]+"' AND "
                        + KEY_TIME+ "= '"+days[i]+"'";

                Cursor cr = db.rawQuery(query,null);
                if(cr.moveToFirst()){
                    totalTimeOneWeek = cr.getInt(cr.getColumnIndex("TotalTimePastWeekB"))+ totalTimeOneWeek;
                    Log.d(TAG, "pastOneWeekB: time past week backward "+ totalTimeOneWeek);
                 }
                cr.close();

            }
            return totalTimeOneWeek;
    }

    //total Mode Time past 30 days
  public  int pastOneMonthDataF(int date,int month, int mode){
        int totalTimePastOneMonth = 0;
        String[] months = new String[12];
        months[0]= "Jan";
        months[1] = "Feb";
        months[2]= "Mar";
        months[3] = "Apr";
        months[4]= "May";
        months[5] = "Jun";
        months[6]= "July";
        months[7] = "Aug";
        months[8]= "Sep";
        months[9] = "Oct";
        months[10] = "Nov";
        months[11] = "Dec";

        Integer[] days = new Integer[32];
        days[0]=1;
        days[1]=2;
        days[2]=3;
        days[3]=4;
        days[4]=5;
        days[5]=6;
        days[6]=7;
        days[7]=8;
        days[8]=9;
        days[9]=10;
        days[10]=11;
        days[11]=12;
        days[12]=13;
        days[13]=14;
        days[14]=15;
        days[15]=16;
        days[16]=17;
        days[17]=18;
        days[18]=19;
        days[19]=20;
        days[20]=21;
        days[21]=22;
        days[22]=23;
        days[23]=24;
        days[24]=25;
        days[25]=26;
        days[26]=27;
        days[27]=28;
        days[28]=29;
        days[29]=30;
        days[30]=31;
        days[31]=31;

        String[] modes  = new String[4];
        modes[0]= "Jap";
        modes[1] = "Meditation";
        modes[2] = "Swadhyay";
        modes[3] = "yagya";

        SQLiteDatabase db = getWritableDatabase();
        for(int i = date;i>0;i--){
            String query = " SELECT SUM(" + KEY_ACTUAL_TIME +") as TotalPastOneMonthDataF FROM "
                    + TABLE_USER_REPORT + " WHERE "+ KEY_MODE + "= '"+modes[mode]+"' AND " + KEY_DATE +" ='"+months[month]+"' AND "
                    + KEY_TIME+ "= '"+days[i]+"'";

            Cursor cr = db.rawQuery(query,null);
            if(cr.moveToFirst()){
                totalTimePastOneMonth = cr.getInt(cr.getColumnIndex("TotalPastOneMonthDataF"))+ totalTimePastOneMonth;

            }
            cr.close();

        }




        return totalTimePastOneMonth;
    }



    public  int pastOneMonthDataB(int date,int month, int mode){
        int totalTimePastOneMonth = 0;
        String[] months = new String[12];
        months[0]= "Jan";
        months[1] = "Feb";
        months[2]= "Mar";
        months[3] = "Apr";
        months[4]= "May";
        months[5] = "Jun";
        months[6]= "July";
        months[7] = "Aug";
        months[8]= "Sep";
        months[9] = "Oct";
        months[10] = "Nov";
        months[11] = "Dec";

        Integer[] days = new Integer[32];
        days[0]=1;
        days[1]=2;
        days[2]=3;
        days[3]=4;
        days[4]=5;
        days[5]=6;
        days[6]=7;
        days[7]=8;
        days[8]=9;
        days[9]=10;
        days[10]=11;
        days[11]=12;
        days[12]=13;
        days[13]=14;
        days[14]=15;
        days[15]=16;
        days[16]=17;
        days[17]=18;
        days[18]=19;
        days[19]=20;
        days[20]=21;
        days[21]=22;
        days[22]=23;
        days[23]=24;
        days[24]=25;
        days[25]=26;
        days[26]=27;
        days[27]=28;
        days[28]=29;
        days[29]=30;
        days[30]=31;
        days[31]=31;

        String[] modes  = new String[4];
        modes[0]= "Jap";
        modes[1] = "Meditation";
        modes[2] = "Swadhyay";
        modes[3] = "yagya";

        SQLiteDatabase db = getWritableDatabase();


        for(int i= date;i<32;i++){
            String query = " SELECT SUM(" + KEY_ACTUAL_TIME +") as TotalPastOneMonthDataB FROM "
                    + TABLE_USER_REPORT + " WHERE "+ KEY_MODE + "= '"+modes[mode]+"' AND " + KEY_DATE +" ='"+months[month]+"' AND "
                    + KEY_TIME+ "= '"+days[i]+"'";

            Cursor cr = db.rawQuery(query,null);
            if(cr.moveToFirst()){
                totalTimePastOneMonth = cr.getInt(cr.getColumnIndex("TotalPastOneMonthDataB"))+ totalTimePastOneMonth;

            }
            cr.close();

        }


        return totalTimePastOneMonth;
    }


    public int timeBetweenRange(int startDate,int endDate,int startMonth, int endMonth,int mode){

        int totalTimeInGivenRange = 0;
        String[] months = new String[12];
        months[0]= "Jan";
        months[1] = "Feb";
        months[2]= "Mar";
        months[3] = "Apr";
        months[4]= "May";
        months[5] = "Jun";
        months[6]= "July";
        months[7] = "Aug";
        months[8]= "Sep";
        months[9] = "Oct";
        months[10] = "Nov";
        months[11] = "Dec";

        Integer[] days = new Integer[32];
        days[0]=1;days[1]=2;days[2]=3;
        days[3]=4;days[4]=5;
        days[5]=6;
        days[6]=7;
        days[7]=8;
        days[8]=9;
        days[9]=10;
        days[10]=11;
        days[11]=12;
        days[12]=13;
        days[13]=14;
        days[14]=15;
        days[15]=16;
        days[16]=17;
        days[17]=18;
        days[18]=19;
        days[19]=20;
        days[20]=21;
        days[21]=22;
        days[22]=23;
        days[23]=24;
        days[24]=25;
        days[25]=26;
        days[26]=27;
        days[27]=28;
        days[28]=29;
        days[29]=30;
        days[30]=31;
        days[31]=31;

        String[] modes  = new String[4];
        modes[0]= "Jap";
        modes[1] = "Meditation";
        modes[2] = "Swadhyay";
        modes[3] = "yagya";

        SQLiteDatabase db = getWritableDatabase();

        if(startMonth==endMonth){

            for(int i = startDate-1;i<=endDate;i++) {

                String query = " SELECT SUM(" + KEY_ACTUAL_TIME + ") as TotalTimeInGivenRange FROM "
                        + TABLE_USER_REPORT + " WHERE " + KEY_MODE + "= '" + modes[mode] + "' AND " + KEY_DATE + " ='" + months[startMonth] + "' AND "
                        + KEY_TIME + "= '" + days[i] + "'";
                Cursor cr = db.rawQuery(query,null);
                if(cr.moveToFirst()){
                    totalTimeInGivenRange = cr.getInt(cr.getColumnIndex("TotalTimeInGivenRange"))+ totalTimeInGivenRange;

                }
                cr.close();
            }
        }
        return totalTimeInGivenRange;
    }

    public int yearlyData(int year,int mode){
        int totalTimeInYear = 0;

        String[] modes  = new String[4];
        modes[0]= "Jap";
        modes[1] = "Meditation";
        modes[2] = "Swadhyay";
        modes[3] = "yagya";
        SQLiteDatabase db = getWritableDatabase();

        String query = " SELECT SUM(" + KEY_ACTUAL_TIME + ") as TotalTimeInYear FROM "
                + TABLE_USER_REPORT + " WHERE " + KEY_MODE + "= '" + modes[mode] + "' AND " + KEY_YEAR + " ='" + year + "'";

        Cursor cr = db.rawQuery(query,null);
        if(cr.moveToFirst()){
            totalTimeInYear = cr.getInt(cr.getColumnIndex("TotalTimeInYear"))+ totalTimeInYear;

        }
        cr.close();


        return totalTimeInYear;
    }




}
