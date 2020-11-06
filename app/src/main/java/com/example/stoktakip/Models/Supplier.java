package com.example.stoktakip.Models;

public class Supplier {

    private String supplierKey, supplierName, supplierSurname, companyName, supplierNum, supplierAddress, supplierPhoto;

    public Supplier() {
    }

    public Supplier(String supplierKey, String supplierName, String supplierSurname, String companyName, String supplierNum, String supplierAddress, String supplierPhoto) {
        this.supplierKey = supplierKey;
        this.supplierName = supplierName;
        this.supplierSurname = supplierSurname;
        this.companyName = companyName;
        this.supplierNum = supplierNum;
        this.supplierAddress = supplierAddress;
        this.supplierPhoto = supplierPhoto;
    }

    public String getSupplierKey() {
        return supplierKey;
    }

    public void setSupplierKey(String supplierKey) {
        this.supplierKey = supplierKey;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getSupplierSurname() {
        return supplierSurname;
    }

    public void setSupplierSurname(String supplierSurname) {
        this.supplierSurname = supplierSurname;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getSupplierNum() {
        return supplierNum;
    }

    public void setSupplierNum(String supplierNum) {
        this.supplierNum = supplierNum;
    }

    public String getSupplierAddress() {
        return supplierAddress;
    }

    public void setSupplierAddress(String supplierAddress) {
        this.supplierAddress = supplierAddress;
    }

    public String getSupplierPhoto() {
        return supplierPhoto;
    }

    public void setSupplierPhoto(String supplierPhoto) {
        this.supplierPhoto = supplierPhoto;
    }
}
