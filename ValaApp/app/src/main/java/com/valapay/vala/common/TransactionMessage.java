//package com.valapay.vala.common;
//
///**
// * Created by noam on 08/05/2016.
// */
//public class TransactionMessage extends Message {
//
//    private String eventId;
//
//    private String transactionId;
//
//    private String senderId;
//
//    private String receiverId;
//
//    private String amount; //amount is in sender currency
//
//    private Currency sendCurrency;
//
//    private Currency receiveCurrency;
//
//    private FundingSource fundingSource;
//
//    private long transactionCreationTimeMilliseconds;
//
//    private TransactionStatus transactionStatus;
//
//    private PriceResponseMessage priceResponse;
//
//    public String getTransactionId() {
//        return transactionId;
//    }
//
//    public void setTransactionId(String transactionId) {
//        this.transactionId = transactionId;
//    }
//
//    public String getSenderId() {
//        return senderId;
//    }
//
//    public void setSenderId(String senderId) {
//        this.senderId = senderId;
//    }
//
//    public String getReceiverId() {
//        return receiverId;
//    }
//
//    public void setReceiverId(String receiverId) {
//        this.receiverId = receiverId;
//    }
//
//    public String getAmount() {
//        return amount;
//    }
//
//    public void setAmount(String amount) {
//        this.amount = amount;
//    }
//
//    public Currency getSendCurrency() {
//        return sendCurrency;
//    }
//
//    public void setSendCurrency(Currency sendCurrency) {
//        this.sendCurrency = sendCurrency;
//    }
//
//    public Currency getReceiveCurrency() {
//        return receiveCurrency;
//    }
//
//    public void setReceiveCurrency(Currency receiveCurrency) {
//        this.receiveCurrency = receiveCurrency;
//    }
//
//    public FundingSource getFundingSource() {
//        return fundingSource;
//    }
//
//    public void setFundingSource(FundingSource fundingSource) {
//        this.fundingSource = fundingSource;
//    }
//
//    public long getTransactionCreationTimeMilliseconds() {
//        return transactionCreationTimeMilliseconds;
//    }
//
//    public void setTransactionCreationTimeMilliseconds(long transactionCreationTimeMilliseconds) {
//        this.transactionCreationTimeMilliseconds = transactionCreationTimeMilliseconds;
//    }
//
//    public TransactionStatus getTransactionStatus() {
//        return transactionStatus;
//    }
//
//    public void setTransactionStatus(TransactionStatus transactionStatus) {
//        this.transactionStatus = transactionStatus;
//    }
//
//    public PriceResponseMessage getPriceResponse() {
//        return priceResponse;
//    }
//
//    public void setPriceResponse(PriceResponseMessage priceResponse) {
//        this.priceResponse = priceResponse;
//    }
//
//    public String getEventId() {
//        return eventId;
//    }
//
//    public void setEventId(String eventId) {
//        this.eventId = eventId;
//    }
//
//    @Override
//    public String toString() {
//        return "TransactionMessage{" +
//                "transactionId='" + transactionId + '\'' +
//                ", senderId='" + senderId + '\'' +
//                ", receiverId='" + receiverId + '\'' +
//                ", amount='" + amount + '\'' +
//                ", sendCurrency=" + sendCurrency +
//                ", receiveCurrency=" + receiveCurrency +
//                ", fundingSource=" + fundingSource +
//                ", transactionCreationTimeMilliseconds=" + transactionCreationTimeMilliseconds +
//                ", transactionStatus=" + transactionStatus +
//                ", priceResponse=" + priceResponse +
//                '}';
//    }
//}
