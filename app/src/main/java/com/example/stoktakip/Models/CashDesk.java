package com.example.stoktakip.Models;

public class CashDesk {

    String totalSellingProductPrice, totalPurchasedProductPrice, totalCollectedProductPrice, totalPaidProductPrice, totalExpense;

    public CashDesk() {
    }

    public CashDesk(String totalSellingProductPrice, String totalPurchasedProductPrice, String totalCollectedProductPrice, String totalPaidProductPrice, String totalExpense) {
        this.totalSellingProductPrice = totalSellingProductPrice;
        this.totalPurchasedProductPrice = totalPurchasedProductPrice;
        this.totalCollectedProductPrice = totalCollectedProductPrice;
        this.totalPaidProductPrice = totalPaidProductPrice;
        this.totalExpense = totalExpense;
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

    public String getTotalExpense() {
        return totalExpense;
    }

    public void setTotalExpense(String totalExpense) {
        this.totalExpense = totalExpense;
    }
}
