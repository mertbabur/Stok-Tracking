package com.example.stoktakip.Models;

public class CashDesk {

    String totalSellingProductPrice, totalPurchasedProductPrice, totalCollectedProductPrice, totalPaidProductPrice, totatExpense;

    public CashDesk() {
    }

    public CashDesk(String totalSellingProductPrice, String totalPurchasedProductPrice, String totalCollectedProductPrice, String totalPaidProductPrice, String totatExpense) {
        this.totalSellingProductPrice = totalSellingProductPrice;
        this.totalPurchasedProductPrice = totalPurchasedProductPrice;
        this.totalCollectedProductPrice = totalCollectedProductPrice;
        this.totalPaidProductPrice = totalPaidProductPrice;
        this.totatExpense = totatExpense;
    }

    public String getTotalSellingProductPrice() {
        return totalSellingProductPrice;
    }

    public void setTotalSellingProductPrice(String totalSellingProductPrice) {
        this.totalSellingProductPrice = totalSellingProductPrice;
    }

    public String getTotalPurchasedProductPrice() {
        return totalPurchasedProductPrice;
    }

    public void setTotalPurchasedProductPrice(String totalPurchasedProductPrice) {
        this.totalPurchasedProductPrice = totalPurchasedProductPrice;
    }

    public String getTotalCollectedProductPrice() {
        return totalCollectedProductPrice;
    }

    public void setTotalCollectedProductPrice(String totalCollectedProductPrice) {
        this.totalCollectedProductPrice = totalCollectedProductPrice;
    }

    public String getTotalPaidProductPrice() {
        return totalPaidProductPrice;
    }

    public void setTotalPaidProductPrice(String totalPaidProductPrice) {
        this.totalPaidProductPrice = totalPaidProductPrice;
    }

    public String getTotatExpense() {
        return totatExpense;
    }

    public void setTotatExpense(String totatExpense) {
        this.totatExpense = totatExpense;
    }
}
