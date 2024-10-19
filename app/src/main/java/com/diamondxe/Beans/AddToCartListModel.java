package com.diamondxe.Beans;

import java.io.Serializable;


public class AddToCartListModel implements Serializable {

    String itemName = "", stockId = "", certificateNo = "", stockNo = "", supplierId = "", category = "", diamondImage = "",
            shape = "", color = "", clarity = "", rapaportPricePerCt = "", carat = "", discount = "", totalGstPerc = "", pricePerCt = "", subtotal = "", cgstPer = "", cgstAmt = "",
            sgstPer = "", sgstAmt = "", igstPer = "", igstAmt = "", tax = "", totalPrice = "", labGrownDiaPrice = "", naturalDiaPrice = "", shippingCharge = "",
            platformFeeAmt = "", isAvailableForSale = "", isReturnable = "", dxePrefered = "", status = "", onHold = "", dxeMarkup = "", dealerMarkupCommission = "",
            platformFeeTax = "", shippingChargeTax = "", totalAmount = "", totalTax = "", totalCharges = "",showingSubTotal="",currencySymbol="";

    double coupondiscountperc=0,subtotalaftercoupondiscount=0;

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

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getStockId() {
        return stockId;
    }

    public void setStockId(String stockId) {
        this.stockId = stockId;
    }

    public String getCertificateNo() {
        return certificateNo;
    }

    public void setCertificateNo(String certificateNo) {
        this.certificateNo = certificateNo;
    }

    public String getStockNo() {
        return stockNo;
    }

    public void setStockNo(String stockNo) {
        this.stockNo = stockNo;
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

    public String getDiamondImage() {
        return diamondImage;
    }

    public void setDiamondImage(String diamondImage) {
        this.diamondImage = diamondImage;
    }

    public String getShape() {
        return shape;
    }

    public void setShape(String shape) {
        this.shape = shape;
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

    public String getRapaportPricePerCt() {
        return rapaportPricePerCt;
    }

    public void setRapaportPricePerCt(String rapaportPricePerCt) {
        this.rapaportPricePerCt = rapaportPricePerCt;
    }

    public String getCarat() {
        return carat;
    }

    public void setCarat(String carat) {
        this.carat = carat;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
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

    public String getCgstPer() {
        return cgstPer;
    }

    public void setCgstPer(String cgstPer) {
        this.cgstPer = cgstPer;
    }

    public String getCgstAmt() {
        return cgstAmt;
    }

    public void setCgstAmt(String cgstAmt) {
        this.cgstAmt = cgstAmt;
    }

    public String getSgstPer() {
        return sgstPer;
    }

    public void setSgstPer(String sgstPer) {
        this.sgstPer = sgstPer;
    }

    public String getSgstAmt() {
        return sgstAmt;
    }

    public void setSgstAmt(String sgstAmt) {
        this.sgstAmt = sgstAmt;
    }

    public String getIgstPer() {
        return igstPer;
    }

    public void setIgstPer(String igstPer) {
        this.igstPer = igstPer;
    }

    public String getIgstAmt() {
        return igstAmt;
    }

    public void setIgstAmt(String igstAmt) {
        this.igstAmt = igstAmt;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
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

    public String getOnHold() {
        return onHold;
    }

    public void setOnHold(String onHold) {
        this.onHold = onHold;
    }

    public String getDxeMarkup() {
        return dxeMarkup;
    }

    public void setDxeMarkup(String dxeMarkup) {
        this.dxeMarkup = dxeMarkup;
    }

    public String getDealerMarkupCommission() {
        return dealerMarkupCommission;
    }

    public void setDealerMarkupCommission(String dealerMarkupCommission) {
        this.dealerMarkupCommission = dealerMarkupCommission;
    }

    public String getPlatformFeeTax() {
        return platformFeeTax;
    }

    public void setPlatformFeeTax(String platformFeeTax) {
        this.platformFeeTax = platformFeeTax;
    }

    public String getShippingChargeTax() {
        return shippingChargeTax;
    }

    public void setShippingChargeTax(String shippingChargeTax) {
        this.shippingChargeTax = shippingChargeTax;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getTotalTax() {
        return totalTax;
    }

    public void setTotalTax(String totalTax) {
        this.totalTax = totalTax;
    }

    public String getTotalCharges() {
        return totalCharges;
    }

    public void setTotalCharges(String totalCharges) {
        this.totalCharges = totalCharges;
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
