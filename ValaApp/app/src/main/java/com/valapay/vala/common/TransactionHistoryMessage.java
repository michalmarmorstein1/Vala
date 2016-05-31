package com.valapay.vala.common;

import com.valapay.vala.activities.MyTransactionsActivity;

public class TransactionHistoryMessage {

    Transaction[] sentTransactions;
    Transaction[] receivedTransactions;

    public TransactionHistoryMessage(Transaction[] sentTransactions, Transaction[] receivedTransactions) {
        this.sentTransactions = sentTransactions;
        this.receivedTransactions = receivedTransactions;
    }

    public Transaction[] getSentTransactions() {
        return sentTransactions;
    }

    public void setSentTransactions(Transaction[] sentTransactions) {
        this.sentTransactions = sentTransactions;
    }

    public Transaction[] getReceivedTransactions() {
        return receivedTransactions;
    }

    public void setReceivedTransactions(Transaction[] receivedTransactions) {
        this.receivedTransactions = receivedTransactions;
    }

    public class Transaction {

        private String name;
        private String amount;
        private String date;
        private String status;
        private String id;

        public Transaction(String name, String amount, String date, String status, String id) {
            this.name = name;
            this.amount = amount;
            this.date = date;
            this.status = status;
            this.id = id;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
