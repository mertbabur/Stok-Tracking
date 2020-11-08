package com.example.stoktakip.Models;

public class Product {

    String productName, productCode, purchasePrice, sellingPrice, howManyUnit, typeProduct, from, fromKey;

    public Product() {
    }

    public Product(String productName, String productCode, String purchasePrice, String sellingPrice, String howManyUnit, String typeProduct, String from, String fromKey) {
        this.productName = productName;
        this.productCode = productCode;
        this.purchasePrice = purchasePrice;
        this.sellingPrice = sellingPrice;
        this.howManyUnit = howManyUnit;
        this.typeProduct = typeProduct;
        this.from = from;
        this.fromKey = fromKey;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(String purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public String getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(String sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public String getHowManyUnit() {
        return howManyUnit;
    }

    public void setHowManyUnit(String howManyUnit) {
        this.howManyUnit = howManyUnit;
    }

    public String getTypeProduct() {
        return typeProduct;
    }

    public void setTypeProduct(String typeProduct) {
        this.typeProduct = typeProduct;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getFromKey() {
        return fromKey;
    }

    public void setFromKey(String fromKey) {
        this.fromKey = fromKey;
    }
}
