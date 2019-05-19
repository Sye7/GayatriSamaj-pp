package com.example.dell.jaapactivity.ReportManager;

public class ReportData {
    private int id;
    private String mode;
    private float userTime;
    private float actualTime;
    private String date;
    private String time;
    private String day;
    private String type;
    private String audioName;
    private String year;


    public ReportData(){

        //constructor that will be used for jap activity
    }
    public ReportData(String mode,Long userTime,Long actualTime,String date, String time
    ,String day,String type,String year){
        this.mode = mode;
        this.userTime = userTime;
        this.actualTime = actualTime;
        this.date = date;
        this.time = time;
        this.day = day;
        this.type = type;
        this.year = year;

    }
    //Constructor to be used for meditation activity
    public ReportData(String mode,String date, String time,String day,
                      String audioName,Long userTime,Long actualTime, String year){

        this.mode = mode;
        this.date = date;
        this.time = time;
        this.day = day;
        this.audioName = audioName;
        this.userTime = userTime;
        this.actualTime = actualTime;
        this.year = year;

    }

    //Constructor to be used for swadhyay activity
    public ReportData(String mode,String date,String time,String day
            ,Long userTime,Long actualTime, String year){
        this.mode = mode;
        this.date = date;
        this.time = time;
        this.day = day;
        this.userTime = userTime;
        this.actualTime = actualTime;
        this.year = year;
    }

    //Constructor to be used for Report Activity
    public ReportData(String mode,String date,String time,String day,int userTime,
                      float actualTime ,String audioName,String type, String year){
        this.mode = mode;
        this.date = date;
        this.time = time;
        this.day = day;
        this.userTime = userTime;
        this.actualTime = actualTime;
        this.audioName = audioName;
        this.type = type;
        this.year = year;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public float getUserTime() {
        return userTime;
    }

    public void setUserTime(int userTime) {
        this.userTime = userTime;
    }

    public float getActualTime() {
        return actualTime;
    }

    public void setActualTime(float actualTime) {
        this.actualTime = actualTime;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAudioName() {
        return audioName;
    }

    public void setAudioName(String audioName) {
        this.audioName = audioName;
    }
    public void setUserTime(float userTime) {
        this.userTime = userTime;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }


}
