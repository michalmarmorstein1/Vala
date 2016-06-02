package com.valapay.vala.common;

public class RiskFee {
    private String amount;

    private String percent;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPercent() {
        return percent;
    }

    public void setPercent(String percent) {
        this.percent = percent;
    }

    @Override
    public String toString() {
        return "RiskFee [amount = " + amount + ", percent = " + percent + "]";
    }
}

			