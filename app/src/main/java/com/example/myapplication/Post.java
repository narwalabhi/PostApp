package com.example.myapplication;



import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Post implements Serializable {
    private String postId;
    private String UserId;
    private String content;
    private String Username;
    private String UserImageUrl;
    private String postImageUrl;
    private List<String> likedIds;
    private ArrayList<Comment> comments;
    private int commentCount;
    private String title;
    private int likeCount;

    public Post() {
        //empty constructor
    }

    public Post(String postId, String userId, String content, String username, String userImageUrl, String title, String postImageUrl) {
        UserId = userId;
        this.postId = postId;
        this.content = content;
        Username = username;
        UserImageUrl = userImageUrl;
        this.title = title;
        this.likeCount = 0;
        this.commentCount = 0;
        this.postImageUrl = postImageUrl;
        likedIds = new ArrayList<>();
        comments = new ArrayList<>();
    }

    public String getPostImageUrl() {
        return postImageUrl;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public void setUserImageUrl(String userImageUrl) {
        UserImageUrl = userImageUrl;
    }

    public void setPostImageUrl(String postImageUrl) {
        this.postImageUrl = postImageUrl;
    }

    public void setLikedIds(ArrayList<String> likedIds) {
        this.likedIds = likedIds;
    }


    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }



    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    @Override
    public String toString() {
        return "Post{" +
                "UserId='" + UserId + '\'' +
                ", content='" + content + '\'' +
                ", Username='" + Username + '\'' +
                ", UserImageName='" + UserImageUrl + '\'' +
                ", likedIds=" + likedIds +
                ", comments=" + comments +
                ", commentCount=" + commentCount +
                ", topic='" + title + '\'' +
                ", likeCount=" + likeCount +
                '}';
    }

    public String getUserImageUrl() {
        return UserImageUrl;
    }

    public String getUserId() {
        return UserId;
    }

    public String getContent() {
        return content;
    }

    public String getUsername() {
        return Username;
    }


    public List<String> getLikedIds() {
        return likedIds;
    }


    public ArrayList<Comment> getComments() {
        return comments;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public String getTitle() {
        return title;
    }

    public int getLikeCount() {
        return likeCount;
    }

}
