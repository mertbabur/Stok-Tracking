package com.example.stoktakip.Models;

public class SoldProduct {

    private String sellKey, customerKey, productKey, soldQuantity, totalSoldPrice, isPaid, soldDate;

    public SoldProduct() {
    }

    public SoldProduct(String sellKey, String customerKey, String productKey, String soldQuantity, String totalSoldPrice, String isPaid, String soldDate) {
        this.sellKey = sellKey;
        this.customerKey = customerKey;
        this.productKey = productKey;
        this.soldQuantity = soldQuantity;
        this.totalSoldPrice = totalSoldPrice;
        this.isPaid = isPaid;
        this.soldDate = soldDate;
    }

    public String getSellKey() {
        return sellKey;
    }

    public void setSellKey(String sellKey) {
        this.sellKey = sellKey;
    }

    public String getCustomerKey() {
        return customerKey;
    }

    public void setCustomerKey(String customerKey) {
        this.customerKey = customerKey;
    }

    public String getProductKey() {
        return productKey;
    }

    public void setProductKey(String productKey) {
        this.productKey = productKey;
    }

    public String getSoldQuantity() {
        return soldQuantity;
    }

    public void setSoldQuantity(String soldQuantity) {
        this.soldQuantity = soldQuantity;
    }

    public String getTotalSoldPrice() {
        return totalSoldPrice;
    }

    public void setTotalSoldPrice(String totalSoldPrice) {
        this.totalSoldPrice = totalSoldPrice;
    }

    public String getIsPaid() {
        return isPaid;
    }

    public void setIsPaid(String isPaid) {
        this.isPaid = isPaid;
    }

    public String getSoldDate() {
        return soldDate;
    }

    public void setSoldDate(String soldDate) {
        this.soldDate = soldDate;
    }
}
