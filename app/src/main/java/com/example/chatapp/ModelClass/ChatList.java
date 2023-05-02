package com.example.chatapp.ModelClass;

public class ChatList {

    String UserID;
    String UserName;
    String description;
    String date;
    String UriProfile;

    public ChatList(String userID, String userName, String description, String date, String uriProfile) {
        UserID = userID;
        UserName = userName;
        this.description = description;
        this.date = date;
        UriProfile = uriProfile;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUriProfile() {
        return UriProfile;
    }

    public void setUriProfile(String uriProfile) {
        UriProfile = uriProfile;
    }
}
