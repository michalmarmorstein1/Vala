package com.valapay.vala.common;

public class CashCollectReserveMessage {

    private String amount;

    private String reservationCode;

    private String userId;

    private String reservationId;

    private String reservationStatus;

    private String affiliateId;

    private String currency;

    public String getAmount ()
    {
        return amount;
    }

    public void setAmount (String amount)
    {
        this.amount = amount;
    }

    public String getReservationCode ()
    {
        return reservationCode;
    }

    public void setReservationCode (String reservationCode)
    {
        this.reservationCode = reservationCode;
    }

    public String getUserId ()
    {
        return userId;
    }

    public void setUserId (String userId)
    {
        this.userId = userId;
    }

    public String getReservationId ()
    {
        return reservationId;
    }

    public void setReservationId (String reservationId)
    {
        this.reservationId = reservationId;
    }

    public String getReservationStatus ()
    {
        return reservationStatus;
    }

    public void setReservationStatus (String reservationStatus)
    {
        this.reservationStatus = reservationStatus;
    }

    public String getAffiliateId ()
    {
        return affiliateId;
    }

    public void setAffiliateId (String affiliateId)
    {
        this.affiliateId = affiliateId;
    }

    public String getCurrency ()
    {
        return currency;
    }

    public void setCurrency (String currency)
    {
        this.currency = currency;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [amount = "+amount+", reservationCode = "+reservationCode+", userId = "+userId+", reservationId = "+reservationId+", reservationStatus = "+reservationStatus+", affiliateId = "+affiliateId+", currency = "+currency+"]";
    }
}
