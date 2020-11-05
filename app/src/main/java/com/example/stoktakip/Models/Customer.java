package com.example.stoktakip.Models;

public class Customer {

    private String customerKey, customerName, customerSurname, companyName, customerNum, customerAddress, customerPhoto;

    public Customer() {
    }

    public Customer(String customerKey, String customerName, String customerSurname, String companyName, String customerNum, String customerAddress, String customerPhoto) {
        this.customerKey = customerKey;
        this.customerName = customerName;
        this.customerSurname = customerSurname;
        this.companyName = companyName;
        this.customerNum = customerNum;
        this.customerAddress = customerAddress;
        this.customerPhoto = customerPhoto;
    }

    public String getCustomerKey() {
        return customerKey;
    }

    public void setCustomerKey(String customerKey) {
        this.customerKey = customerKey;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerSurname() {
        return customerSurname;
    }

    public void setCustomerSurname(String customerSurname) {
        this.customerSurname = customerSurname;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCustomerNum() {
        return customerNum;
    }

    public void setCustomerNum(String customerNum) {
        this.customerNum = customerNum;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public String getCustomerPhoto() {
        return customerPhoto;
    }

    public void setCustomerPhoto(String customerPhoto) {
        this.customerPhoto = customerPhoto;
    }
}
