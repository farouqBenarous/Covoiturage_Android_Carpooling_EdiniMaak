package com.example.sony.tabhost;

import android.net.Uri;

public class Message {
    private String Uri;
    private String username;
    private  String message;
   public Message() {}

    public  Message ( String Uri , String username ,String message ) {
        this.Uri = Uri;
        this.username = username;
        this.message = message;
        }

    public String getUri() {
        return Uri;
    }

    public void setUri(String uri) {
        Uri = uri;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
