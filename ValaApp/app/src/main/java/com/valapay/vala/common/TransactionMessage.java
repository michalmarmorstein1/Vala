package com.valapay.vala.common;

/**
 * Created by noam on 08/05/2016.
 */
public class TransactionMessage extends Message {

    private String amount;

    private PriceResponse priceResponse;

    private long transactionCreationTimeMilliseconds;

    private String transactionId;

    private String eventId;

    private String depositCode;

    private String receiverId;

    private String sendCurrency;

    private String senderId;

    private String transactionStatus;

    private String fundingSource;

    private String receiveCurrency;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public PriceResponse getPriceResponse() {
        return priceResponse;
    }

    public void setPriceResponse(PriceResponse priceResponse) {
        this.priceResponse = priceResponse;
    }

    public long getTransactionCreationTimeMilliseconds() {
        return transactionCreationTimeMilliseconds;
    }

    public void setTransactionCreationTimeMilliseconds(long transactionCreationTimeMilliseconds) {
        this.transactionCreationTimeMilliseconds = transactionCreationTimeMilliseconds;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getDepositCode() {
        return depositCode;
    }

    public void setDepositCode(String depositCode) {
        this.depositCode = depositCode;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getSendCurrency() {
        return sendCurrency;
    }

    public void setSendCurrency(String sendCurrency) {
        this.sendCurrency = sendCurrency;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(String transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public String getFundingSource() {
        return fundingSource;
    }

    public void setFundingSource(String fundingSource) {
        this.fundingSource = fundingSource;
    }

    public String getReceiveCurrency() {
        return receiveCurrency;
    }

    public void setReceiveCurrency(String receiveCurrency) {
        this.receiveCurrency = receiveCurrency;
    }

    @Override
    public String toString() {
        return "TransactionMessage [amount = " + amount + ", priceResponse = " + priceResponse + ", transactionCreationTimeMilliseconds = " + transactionCreationTimeMilliseconds + ", transactionId = " + transactionId + ", eventId = " + eventId + ", depositCode = " + depositCode + ", receiverId = " + receiverId + ", sendCurrency = " + sendCurrency + ", senderId = " + senderId + ", transactionStatus = " + transactionStatus + ", fundingSource = " + fundingSource + ", receiveCurrency = " + receiveCurrency + "]";
    }
}