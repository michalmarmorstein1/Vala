package com.valapay.vala.common;

public class CashCollectRequestMessage {

    private String amount;

    private double locationLong;

    private String userId;

    private double locationLat;

    private Affiliate[] affiliates;

    private String currency;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public double getLocationLong() {
        return locationLong;
    }

    public void setLocationLong(double locationLong) {
        this.locationLong = locationLong;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public double getLocationLat() {
        return locationLat;
    }

    public void setLocationLat(double locationLat) {
        this.locationLat = locationLat;
    }

    public Affiliate[] getAffiliates() {
        return affiliates;
    }

    public void setAffiliates(Affiliate[] affiliates) {
        this.affiliates = affiliates;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @Override
    public String toString() {
        return "ClassPojo [amount = " + amount + ", locationLong = " + locationLong + ", userId = " + userId + ", locationLat = " + locationLat + ", affiliates = " + affiliates + ", currency = " + currency + "]";
    }
}

