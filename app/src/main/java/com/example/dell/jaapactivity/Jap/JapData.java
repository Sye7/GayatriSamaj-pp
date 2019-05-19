package com.example.dell.jaapactivity.Jap;

public class JapData {
    private int id;
    private String type;
    private Long time;
    private int hasVideo;
    private String videoURl;

    public JapData(){

    }

    public JapData(Long time,String type){
        this.time = time;
        this.type = type;

    }
    public JapData(int hasVideo,int id,Long time,String type,String videoURl){

        this.hasVideo = hasVideo;
        this.id = id;
        this.time = time;
        this.type= type;
        this.videoURl = videoURl;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }



    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }



    public int isHasVideo() {
        return hasVideo;
    }

    public void setHasVideo(int hasVideo) {
        this.hasVideo = hasVideo;
    }



    public String getVideoURl() {
        return videoURl;
    }

    public void setVideoURl(String videoURl) {
        this.videoURl = videoURl;
    }



}
