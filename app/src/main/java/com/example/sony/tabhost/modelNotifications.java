package com.example.sony.tabhost;

public class modelNotifications {
    private  String id;
    private String fullname;
    private String photo_profile;
    private String Descreptios;
    private  String type;
public modelNotifications() {}


public modelNotifications(String id,String fullname,String photo_profile,String Descreptios,String type) {
    this.id = id;
    this.fullname =fullname;
    this.photo_profile = photo_profile;
    this.Descreptios = Descreptios;
    this.type = type;
}
    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPhoto_profile() {
        return photo_profile;
    }

    public void setPhoto_profile(String photo_profile) {
        this.photo_profile = photo_profile;
    }

    public String getDescreptios() {
        return Descreptios;
    }

    public void setDescreptios(String descreptios) {
        Descreptios = descreptios;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
