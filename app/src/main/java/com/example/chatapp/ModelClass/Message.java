package com.example.chatapp.ModelClass;

public class Message {


    String datetime;
    String text_message;
    String type;
    String senderId;
    String receiver;
    String imageUri;


    public Message() {

    }

    public Message(String datetime, String text_message,  String senderId, String receiver, String imageUri,String type) {
        this.datetime = datetime;
        this.text_message = text_message;
        this.senderId = senderId;
        this.receiver = receiver;
        this.imageUri =imageUri;
        this.type=type;

    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return text_message;
    }

    public void setMessage(String message) {
        this.text_message = message;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public void setImageUri(String imageUri) {

        this.imageUri = imageUri;
    }

    public String getImageUri() {
        return imageUri;
    }
}
