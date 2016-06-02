package com.valapay.vala.common;

public class TransactionConfirmMessage extends Message{
    private String transactionId;

    private String trackingNumber;

    private FundingSourceDetails fundingSourceDetails;

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public FundingSourceDetails getFundingSourceDetails() {
        return fundingSourceDetails;
    }

    public void setFundingSourceDetails(FundingSourceDetails fundingSourceDetails) {
        this.fundingSourceDetails = fundingSourceDetails;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    @Override
    public String toString() {
        return "TransactionConfirmMessage [transactionId = " + transactionId + ", fundingSourceDetails = " + fundingSourceDetails + "]";
    }
}


