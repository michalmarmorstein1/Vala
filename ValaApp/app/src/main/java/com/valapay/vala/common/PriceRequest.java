package com.valapay.vala.common;

public class PriceRequest
{
    private String sourceCurrency;

    private String amount;

    private String destinationCurrency;

    private String amountInUSD;

    private String fundingSource;

    public String getSourceCurrency ()
    {
        return sourceCurrency;
    }

    public void setSourceCurrency (String sourceCurrency)
    {
        this.sourceCurrency = sourceCurrency;
    }

    public String getAmount ()
    {
        return amount;
    }

    public void setAmount (String amount)
    {
        this.amount = amount;
    }

    public String getDestinationCurrency ()
    {
        return destinationCurrency;
    }

    public void setDestinationCurrency (String destinationCurrency)
    {
        this.destinationCurrency = destinationCurrency;
    }

    public String getAmountInUSD ()
    {
        return amountInUSD;
    }

    public void setAmountInUSD (String amountInUSD)
    {
        this.amountInUSD = amountInUSD;
    }

    public String getFundingSource ()
    {
        return fundingSource;
    }

    public void setFundingSource (String fundingSource)
    {
        this.fundingSource = fundingSource;
    }

    public String toString() {
        return "PriceRequest{" +
                "amount=" + amount +
                ", sourceCurrency=" + sourceCurrency +
                ", destinationCurrency=" + destinationCurrency +
                ", fundingSource=" + fundingSource +
                '}';
    }
}