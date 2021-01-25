package com.example.hikers.models;

import android.util.Log;

import com.example.hikers.helpers.Const;

public class User {

    private String id;
    private String username;
    private String fullname;
    private String imageurl;
    private String bio;
    private String status = "";
    private String search;


    public User(String id, String username, String fullname, String imageurl, String bio, String status, String search) {
        this.id = id;
        this.username = username;
        this.fullname = fullname;
        this.imageurl = imageurl;
        this.bio = bio;
        this.status = status;
        this.search = search;
    }

    public User() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getId() {
        if (this.id == null) {
            Log.e(Const.LOG, "id is null");
            return "";
        }
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        if (this.username == null) {
            return "";
        }
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        if (this.fullname == null) {
            return "";
        }
        return fullname;
    }

    public void setFullName(String fullName) {
        this.fullname = fullName;
    }

    public String getImageUrl() {
        if (this.imageurl == null) {
            return "";
        }
        return imageurl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageurl = imageUrl;
    }

    public String getBio() {
        if (this.bio == null) {
            return "";
        }
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    @Override
    public String toString() {
        return "User { " +
                "id = '" + id + '\'' +
                ", username = '" + username + '\'' +
                ", fullname = '" + fullname + '\'' +
                ", imageurl = '" + imageurl + '\'' +
                ", bio = '" + bio + '\'' +
                ", status = '" + status + '\'' +
                ", search = '" + search + '\'' +
                '}';
    }
}
