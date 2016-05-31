package com.valapay.vala.common;

public class Affiliate {
    private String phoneNumber;

    private double locationLong;

    private String address;

    private String userId;

    private String name;

    private double locationLat;

    private String hoursClose;

    private String rating;

    private String hoursOpen;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public double getLocationLong() {
        return locationLong;
    }

    public void setLocationLong(double locationLong) {
        this.locationLong = locationLong;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLocationLat() {
        return locationLat;
    }

    public void setLocationLat(double locationLat) {
        this.locationLat = locationLat;
    }

    public String getHoursClose() {
        return hoursClose;
    }

    public void setHoursClose(String hoursClose) {
        this.hoursClose = hoursClose;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getHoursOpen() {
        return hoursOpen;
    }

    public void setHoursOpen(String hoursOpen) {
        this.hoursOpen = hoursOpen;
    }

    @Override
    public String toString() {
        return "ClassPojo [phoneNumber = " + phoneNumber + ", locationLong = " + locationLong + ", address = " + address + ", userId = " + userId + ", name = " + name + ", locationLat = " + locationLat + ", hoursClose = " + hoursClose + ", rating = " + rating + ", hoursOpen = " + hoursOpen + "]";
    }
}
