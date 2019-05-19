package com.example.dell.jaapactivity.Meditation;

public class MeditationData {
    private int id;
    private String audioName;
    private int duration;

    public MeditationData(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAudioName() {
        return audioName;
    }

    public void setAudioName(String audioName) {
        this.audioName = audioName;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public MeditationData(String audioName, int duration){
        this.audioName = audioName;
        this.duration = duration;

    }
}
