package com.example.hikers.models;

public class Favourite {

    private boolean isPost;
    private String imageUrl;
    private String id;

    public Favourite() {
    }

    public Favourite(boolean isPost, String imageUrl) {
        this.isPost = isPost;
        this.imageUrl = imageUrl;
    }

    public Favourite(boolean isPost, String imageUrl, String id) {
        this.isPost = isPost;
        this.imageUrl = imageUrl;
        this.id = id;
    }

    public boolean isPost() {
        return isPost;
    }

    public void setPost(boolean post) {
        isPost = post;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
