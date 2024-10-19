package com.diamondxe.Beans;

import java.io.Serializable;


public class TopDiamondImageModel implements Serializable {

    String id="", image = "", name="", price="",category="", carat="", certificateNo="", currencySymbol="",showingSubTotal="";
    double coupondiscountperc=0,subtotalaftercoupondiscount=0;

    boolean isFirstPosition = false;

    public double getCoupondiscountperc() {
        return coupondiscountperc;
    }

    public void setCoupondiscountperc(double coupondiscountperc) {
        this.coupondiscountperc = coupondiscountperc;
    }

    public double getSubtotalaftercoupondiscount() {
        return subtotalaftercoupondiscount;
    }

    public void setSubtotalaftercoupondiscount(double subtotalaftercoupondiscount) {
        this.subtotalaftercoupondiscount = subtotalaftercoupondiscount;
    }

    public String getId() {
        return id;
    }

    public void setId(String key) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCarat() {
        return carat;
    }

    public void setCarat(String carat) {
        this.carat = carat;
    }

    public boolean isFirstPosition() {
        return isFirstPosition;
    }

    public void setFirstPosition(boolean firstPosition) {
        isFirstPosition = firstPosition;
    }

    public String getCertificateNo() {
        return certificateNo;
    }

    public void setCertificateNo(String certificateNo) {
        this.certificateNo = certificateNo;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }

    public String getShowingSubTotal() {
        return showingSubTotal;
    }

    public void setShowingSubTotal(String showingSubTotal) {
        this.showingSubTotal = showingSubTotal;
    }
}
