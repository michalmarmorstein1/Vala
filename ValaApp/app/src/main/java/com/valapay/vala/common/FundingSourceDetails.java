package com.valapay.vala.common;

public class FundingSourceDetails {
    private String cardHolderName;

    private String expiresMonth;

    private String type;

    private String ccv;

    private String expiresYear;

    private String cardNumber;

    public String getCardHolderName() {
        return cardHolderName;
    }

    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }

    public String getExpiresMonth() {
        return expiresMonth;
    }

    public void setExpiresMonth(String expiresMonth) {
        this.expiresMonth = expiresMonth;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCcv() {
        return ccv;
    }

    public void setCcv(String ccv) {
        this.ccv = ccv;
    }

    public String getExpiresYear() {
        return expiresYear;
    }

    public void setExpiresYear(String expiresYear) {
        this.expiresYear = expiresYear;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    @Override
    public String toString() {
        return "FundingSourceDetails [cardHolderName = " + cardHolderName + ", expiresMonth = " + expiresMonth + ", type = " + type + ", ccv = " + ccv + ", expiresYear = " + expiresYear + ", cardNumber = " + cardNumber + "]";
    }
}

