package com.example.sony.tabhost;

import java.util.concurrent.atomic.AtomicInteger;

public class Trip {
    private String id;
    private String dép;
    private String arv;
    private int Year;
    private int Month;
    private int Day;
    private int Hour;
    private int Minute;
    private int price;
    private int nbPlace;
    private String IdUser;
    private String usernameUser;
    private String UriPhoto;
    private String Phonenumber;
    private Car car;
    private  String Type;

    public Trip() {
        }

    public Trip(String id,String dép, String arv, int Year, int Month, int Day, int Hour, int Minute, int price, int nbPlace, String IdUser, String usernameUser, String Phonenumber, String UriPhoto, Car car,String Type )
    {
        this.Type=Type;
        this.id = id;
        this.dép = dép;
        this.arv = arv;
        this.Year = Year;
        this.Month = Month;
        this.Day = Day;
        this.Hour = Hour;
        this.Minute = Minute;
        this.price = price;
        this.nbPlace = nbPlace;
        this.IdUser = IdUser;
        this.usernameUser = usernameUser;
        this.Phonenumber = Phonenumber;
        this.UriPhoto = UriPhoto;
        this.car = car;
    }


    public String getDép() {
        return dép;
    }

    public void setDép(String dép) {
        this.dép = dép;
    }

    public String getArv() {
        return arv;
    }

    public void setArv(String arv) {
        this.arv = arv;
    }

    public int getNbPlace() {
        return nbPlace;
    }

    public void setNbPlace(int nbPlace) {
        this.nbPlace = nbPlace;
    }

    public String getIdUser() {
        return IdUser;
    }

    public void setIdUser(String idUser) {
        IdUser = idUser;
    }

    public String getUsernameUser() {
        return usernameUser;
    }

    public void setUsernameUser(String usernameUser) {
        this.usernameUser = usernameUser;
    }

    public String getUriPhoto() {
        return UriPhoto;
    }

    public void setUriPhoto(String uriPhoto) {
        UriPhoto = uriPhoto;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }


    public int getYear() {
        return Year;
    }

    public void setYear(int year) {
        Year = year;
    }

    public int getMonth() {
        return Month;
    }

    public void setMonth(int month) {
        Month = month;
    }

    public int getDay() {
        return Day;
    }

    public void setDay(int day) {
        Day = day;
    }

    public int getHour() {
        return Hour;
    }

    public void setHour(int hour) {
        Hour = hour;
    }

    public int getMinute() {
        return Minute;
    }

    public void setMinute(int minute) {
        Minute = minute;
    }

    public String getPhonenumber() {
        return Phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        Phonenumber = phonenumber;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }
}