package com.example.stoktakip.Models;

public class CashDesk {

    String totalSellingProductPrice, totalPurchasedProductPrice, totalCollectedProductPrice, totalPaidProductPrice, totalExpense
            , totalAdditionalExpense, totalTaxExpense, totalRentExpense, totalFuelExpense, totalEmployeeCost, totalOtherExpense;

    public CashDesk() {
    }

    public CashDesk(String totalSellingProductPrice, String totalPurchasedProductPrice, String totalCollectedProductPrice, String totalPaidProductPrice, String totalExpense, String totalAdditionalExpense, String totalTaxExpense, String totalRentExpense, String totalFuelExpense, String totalEmployeeCost, String totalOtherExpense) {
        this.totalSellingProductPrice = totalSellingProductPrice;
        this.totalPurchasedProductPrice = totalPurchasedProductPrice;
        this.totalCollectedProductPrice = totalCollectedProductPrice;
        this.totalPaidProductPrice = totalPaidProductPrice;
        this.totalExpense = totalExpense;
        this.totalAdditionalExpense = totalAdditionalExpense;
        this.totalTaxExpense = totalTaxExpense;
        this.totalRentExpense = totalRentExpense;
        this.totalFuelExpense = totalFuelExpense;
        this.totalEmployeeCost = totalEmployeeCost;
        this.totalOtherExpense = totalOtherExpense;
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

    public String getTotalAdditionalExpense() {
        return totalAdditionalExpense;
    }

    public void setTotalAdditionalExpense(String totalAdditionalExpense) {
        this.totalAdditionalExpense = totalAdditionalExpense;
    }

    public String getTotalTaxExpense() {
        return totalTaxExpense;
    }

    public void setTotalTaxExpense(String totalTaxExpense) {
        this.totalTaxExpense = totalTaxExpense;
    }

    public String getTotalRentExpense() {
        return totalRentExpense;
    }

    public void setTotalRentExpense(String totalRentExpense) {
        this.totalRentExpense = totalRentExpense;
    }

    public String getTotalFuelExpense() {
        return totalFuelExpense;
    }

    public void setTotalFuelExpense(String totalFuelExpense) {
        this.totalFuelExpense = totalFuelExpense;
    }

    public String getTotalEmployeeCost() {
        return totalEmployeeCost;
    }

    public void setTotalEmployeeCost(String totalEmployeeCost) {
        this.totalEmployeeCost = totalEmployeeCost;
    }

    public String getTotalOtherExpense() {
        return totalOtherExpense;
    }

    public void setTotalOtherExpense(String totalOtherExpense) {
        this.totalOtherExpense = totalOtherExpense;
    }

}
