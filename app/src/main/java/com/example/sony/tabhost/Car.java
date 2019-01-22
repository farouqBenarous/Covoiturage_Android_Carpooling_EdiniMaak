package com.example.sony.tabhost;


import android.net.Uri;

import java.util.Locale;

public class Car {
    private String id;
    private String Photo;
    private String brand;
    private  String model;
    private  String matricule;
    private String numcartegrise;


    public Car () {}

    public Car (String brand) {
    this.brand = brand;
}

    public Car (String id,String Photo , String brand, String model, String matricule, String numcartegrise) {
        this.id=id;
        this.Photo = Photo;
        this.brand = brand;
        this.model = model;
        this.matricule = matricule;
        this.numcartegrise = numcartegrise;


    }

    public Car(String id,String brand, String model, String matricule, String numcartegrise) {
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.matricule = matricule;
        this.numcartegrise = numcartegrise;
    }

    public String getNumcartegrise() {
        return numcartegrise;
    }

    public void setNumcartegrise(String numcartegrise) {
        this.numcartegrise = numcartegrise;
    }

    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getPhoto() {
        return Photo;
    }

    public void setPhoto(String photo) {
        Photo = photo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
