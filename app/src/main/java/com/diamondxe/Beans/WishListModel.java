package com.diamondxe.Beans;

import java.io.Serializable;


public class WishListModel implements Serializable {

    String certificateNo = "", stockId = "", itemName = "", supplierId = "", category = "", cutGrade = "", certificateName = "", polish = "", symmetry = "", measurement = "", fluorescenceIntensity = "", carat = "", color = "", clarity = "", shape = "", shade = "", tablePerc = "", depthPerc = "",
            luster = "", eyeClean = "", diamondImage = "", diamondVideo = "", discount = "", rapaportPricePerCt = "", labGrownDiaPrice = "",
            naturalDiaPrice = "", isAvailableForSale = "", isReturnable = "", dxePrefered = "", status = "", totalGstPerc = "", pricePerCt = "", subtotal = "", totalPrice = "", isCart = "", onHold = "", shippingCharge = "", platformFeeAmt = "",
            dxeMarkup = "",stockNo="",showingSubTotal="",currencySymbol="";
    double coupondiscountperc=0,subtotalaftercoupondiscount=0;
    int isDxeLUXE=0;

    public int getIsDxeLUXE() {
        return isDxeLUXE;
    }

    public void setIsDxeLUXE(int isDxeLUXE) {
        this.isDxeLUXE = isDxeLUXE;
    }

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

    public String getCertificateNo() {
        return certificateNo;
    }

    public void setCertificateNo(String certificateNo) {
        this.certificateNo = certificateNo;
    }

    public String getStockId() {
        return stockId;
    }

    public void setStockId(String stockId) {
        this.stockId = stockId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCutGrade() {
        return cutGrade;
    }

    public void setCutGrade(String cutGrade) {
        this.cutGrade = cutGrade;
    }

    public String getCertificateName() {
        return certificateName;
    }

    public void setCertificateName(String certificateName) {
        this.certificateName = certificateName;
    }

    public String getPolish() {
        return polish;
    }

    public void setPolish(String polish) {
        this.polish = polish;
    }

    public String getSymmetry() {
        return symmetry;
    }

    public void setSymmetry(String symmetry) {
        this.symmetry = symmetry;
    }

    public String getMeasurement() {
        return measurement;
    }

    public void setMeasurement(String measurement) {
        this.measurement = measurement;
    }

    public String getFluorescenceIntensity() {
        return fluorescenceIntensity;
    }

    public void setFluorescenceIntensity(String fluorescenceIntensity) {
        this.fluorescenceIntensity = fluorescenceIntensity;
    }

    public String getCarat() {
        return carat;
    }

    public void setCarat(String carat) {
        this.carat = carat;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getClarity() {
        return clarity;
    }

    public void setClarity(String clarity) {
        this.clarity = clarity;
    }

    public String getShape() {
        return shape;
    }

    public void setShape(String shape) {
        this.shape = shape;
    }

    public String getShade() {
        return shade;
    }

    public void setShade(String shade) {
        this.shade = shade;
    }

    public String getTablePerc() {
        return tablePerc;
    }

    public void setTablePerc(String tablePerc) {
        this.tablePerc = tablePerc;
    }

    public String getDepthPerc() {
        return depthPerc;
    }

    public void setDepthPerc(String depthPerc) {
        this.depthPerc = depthPerc;
    }

    public String getLuster() {
        return luster;
    }

    public void setLuster(String luster) {
        this.luster = luster;
    }

    public String getEyeClean() {
        return eyeClean;
    }

    public void setEyeClean(String eyeClean) {
        this.eyeClean = eyeClean;
    }

    public String getDiamondImage() {
        return diamondImage;
    }

    public void setDiamondImage(String diamondImage) {
        this.diamondImage = diamondImage;
    }

    public String getDiamondVideo() {
        return diamondVideo;
    }

    public void setDiamondVideo(String diamondVideo) {
        this.diamondVideo = diamondVideo;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getRapaportPricePerCt() {
        return rapaportPricePerCt;
    }

    public void setRapaportPricePerCt(String rapaportPricePerCt) {
        this.rapaportPricePerCt = rapaportPricePerCt;
    }

    public String getLabGrownDiaPrice() {
        return labGrownDiaPrice;
    }

    public void setLabGrownDiaPrice(String labGrownDiaPrice) {
        this.labGrownDiaPrice = labGrownDiaPrice;
    }

    public String getNaturalDiaPrice() {
        return naturalDiaPrice;
    }

    public void setNaturalDiaPrice(String naturalDiaPrice) {
        this.naturalDiaPrice = naturalDiaPrice;
    }

    public String getIsAvailableForSale() {
        return isAvailableForSale;
    }

    public void setIsAvailableForSale(String isAvailableForSale) {
        this.isAvailableForSale = isAvailableForSale;
    }

    public String getIsReturnable() {
        return isReturnable;
    }

    public void setIsReturnable(String isReturnable) {
        this.isReturnable = isReturnable;
    }

    public String getDxePrefered() {
        return dxePrefered;
    }

    public void setDxePrefered(String dxePrefered) {
        this.dxePrefered = dxePrefered;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTotalGstPerc() {
        return totalGstPerc;
    }

    public void setTotalGstPerc(String totalGstPerc) {
        this.totalGstPerc = totalGstPerc;
    }

    public String getPricePerCt() {
        return pricePerCt;
    }

    public void setPricePerCt(String pricePerCt) {
        this.pricePerCt = pricePerCt;
    }

    public String getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(String subtotal) {
        this.subtotal = subtotal;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getIsCart() {
        return isCart;
    }

    public void setIsCart(String isCart) {
        this.isCart = isCart;
    }

    public String getOnHold() {
        return onHold;
    }

    public void setOnHold(String onHold) {
        this.onHold = onHold;
    }

    public String getShippingCharge() {
        return shippingCharge;
    }

    public void setShippingCharge(String shippingCharge) {
        this.shippingCharge = shippingCharge;
    }

    public String getPlatformFeeAmt() {
        return platformFeeAmt;
    }

    public void setPlatformFeeAmt(String platformFeeAmt) {
        this.platformFeeAmt = platformFeeAmt;
    }

    public String getDxeMarkup() {
        return dxeMarkup;
    }

    public void setDxeMarkup(String dxeMarkup) {
        this.dxeMarkup = dxeMarkup;
    }

    public String getStockNo() {
        return stockNo;
    }

    public void setStockNo(String stockNo) {
        this.stockNo = stockNo;
    }

    public String getShowingSubTotal() {
        return showingSubTotal;
    }

    public void setShowingSubTotal(String showingSubTotal) {
        this.showingSubTotal = showingSubTotal;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }
}
