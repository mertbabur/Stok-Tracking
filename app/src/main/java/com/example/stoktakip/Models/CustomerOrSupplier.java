package com.example.stoktakip.Models;

public class CustomerOrSupplier {

    private String key, name, surname, companyName, num, address, photo;

    public CustomerOrSupplier() {
    }

    public CustomerOrSupplier(String key, String name, String surname, String companyName, String num, String address, String photo) {
        this.key = key;
        this.name = name;
        this.surname = surname;
        this.companyName = companyName;
        this.num = num;
        this.address = address;
        this.photo = photo;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
