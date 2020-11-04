package com.example.stoktakip.Models;

public class User {

    private String userUID, email, companyName, name, photo;

    public User() {
    }

    public User(String userUID, String email, String companyName, String name, String photo) {
        this.userUID = userUID;
        this.email = email;
        this.companyName = companyName;
        this.name = name;
        this.photo = photo;
    }

    public String getUserUID() {
        return userUID;
    }

    public void setUserUID(String userUID) {
        this.userUID = userUID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

}
