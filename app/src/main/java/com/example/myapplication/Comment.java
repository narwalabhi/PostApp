package com.example.myapplication;

import java.io.Serializable;

public class Comment implements Serializable {

    private String comment;
    private String imgUrl;
    private String username;

    public Comment() {
    }

    public Comment(String comment, String imgUrl, String username) {
        this.comment = comment;
        this.imgUrl = imgUrl;
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
