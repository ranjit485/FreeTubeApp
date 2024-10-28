package com.rdev.freetube.modal;

public class CommentModal {
    String userName;
    String commentMessage;
    String userProfile;
    String commentId;

    public CommentModal(String userName, String commentMessage, String userProfile, String commentId) {
        this.userName = userName;
        this.commentMessage = commentMessage;
        this.userProfile = userProfile;
        this.commentId = commentId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCommentMessage() {
        return commentMessage;
    }

    public void setCommentMessage(String commentMessage) {
        this.commentMessage = commentMessage;
    }

    public String getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(String userProfile) {
        this.userProfile = userProfile;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

}
