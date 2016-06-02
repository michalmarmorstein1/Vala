package com.valapay.vala.common;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by noam on 18/04/2016.
 */
public class PriceResponse {

    private PriceRequest priceRequest;

    private String valaRevenue;

    private ValaFee valaFee;

    private PriceComponent[] priceComponents;

    public PriceRequest getPriceRequest() {
        return priceRequest;
    }

    public void setPriceRequest(PriceRequest priceRequest) {
        this.priceRequest = priceRequest;
    }

    public String getValaRevenue() {
        return valaRevenue;
    }

    public void setValaRevenue(String valaRevenue) {
        this.valaRevenue = valaRevenue;
    }

    public ValaFee getValaFee() {
        return valaFee;
    }

    public void setValaFee(ValaFee valaFee) {
        this.valaFee = valaFee;
    }

    public PriceComponent[] getPriceComponents() {
        return priceComponents;
    }

    public void setPriceComponents(PriceComponent[] priceComponents) {
        this.priceComponents = priceComponents;
    }

    @Override
    public String toString() {
        return "PriceResponse [priceRequest = " + priceRequest + ", valaRevenue = " + valaRevenue + ", valaFee = " + valaFee + ", priceComponents = " + priceComponents + "]";
    }
}