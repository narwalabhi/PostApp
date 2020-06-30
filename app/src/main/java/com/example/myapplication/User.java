package com.example.myapplication;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String name;
    private String email;
    private String username;
    private String mobile;
    private String dob;
    private String ProPicName;
    private List<String> postIds;
    private int postCount;

    public User() {
    }

    public User(String name, String email, String username, String mobile, String dob, String ProPicName) {
        this.name = name;
        this.username = username;
        this.mobile = mobile;
        this.dob = dob;
        this.email = email;
        this.ProPicName = ProPicName;
        this.postIds = new ArrayList<>();
        this.postCount = 0;
    }

    public List<String> getPostIds() {
        return postIds;
    }

    public int getPostCount() {
        return postCount;
    }

    public String getEmail() {
        return email;
    }

    public String getProPicName() {
        return ProPicName;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getMobile() {
        return mobile;
    }

    public String getDob() {
        return dob;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", mobile='" + mobile + '\'' +
                ", dob='" + dob + '\'' +
                ", ProPicName='" + ProPicName + '\'' +
                ", postIds=" + postIds +
                ", postCount=" + postCount +
                '}';
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public void setProPicName(String proPicName) {
        ProPicName = proPicName;
    }

    public void setPostIds(List<String> postIds) {
        this.postIds = postIds;
    }

    public void setPostCount(int postCount) {
        this.postCount = postCount;
    }
}
