package com.example.dell.jaapactivity;

public class Users {

    String id;
    String imageURL;
    String username;
    String country;
    String mob;
    String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Users(String id, String imageURL, String username, String country, String mob, String email) {
        this.id = id;
        this.imageURL = imageURL;
        this.username = username;
        this.country = country;
        this.mob = mob;
        this.email = email;
    }

    public String getMob() {
        return mob;
    }

    public void setMob(String mob) {
        this.mob = mob;
    }

    public Users(String id, String imageURL, String username, String country, String mob) {
        this.id = id;
        this.imageURL = imageURL;
        this.username = username;
        this.country = country;
        this.mob = mob;
    }

    public Users(){}


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Users(String id, String imageURL, String username, String country) {
        this.id = id;
        this.imageURL = imageURL;
        this.username = username;
        this.country = country;
    }
}
