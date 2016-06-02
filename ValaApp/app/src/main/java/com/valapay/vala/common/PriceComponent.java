package com.valapay.vala.common;

public class PriceComponent {
    private RiskFee riskFee;

    private String totalFeeAmount;

    private String type;

    public RiskFee getRiskFee() {
        return riskFee;
    }

    public void setRiskFee(RiskFee riskFee) {
        this.riskFee = riskFee;
    }

    public String getTotalFeeAmount() {
        return totalFeeAmount;
    }

    public void setTotalFeeAmount(String totalFeeAmount) {
        this.totalFeeAmount = totalFeeAmount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "PriceComponent [riskFee = " + riskFee + ", totalFeeAmount = " + totalFeeAmount + ", type = " + type + "]";
    }
}

			