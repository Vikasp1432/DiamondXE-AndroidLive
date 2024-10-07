package com.diamondxe.Beans.MyOrder;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;


public class MyOrderListModel implements Serializable {

    String orderId = "", orderDate = "", currencyCode = "", totalAmount = "", orderStatus = "", paymentStatus = "",
            createdAt = "", deliveryDate = "", isCancelable = "", isReserveOrder = "", paymentReceivedDate = "", timeLeftForCancel = "", id = "",
            orderNumber = "", orderDateTime = "", image = "", name = "", status = "", itemDetails = "", stockNumber = "", category = "",
            subTotal = "", currencySymbol="", carat="", color="", clarity="", showingSubTotal="",isReturnable="", shape="", type="",
    cut="", polish="", symmetry="", fir="", lab="", table="", depth="",compareDateTime="", stockId="", certificateNo="",stockNo="", dxePrefered="",
    diamondImage="",growthType="",certificateName="", trackingStatusCode="", trackingStatus="", trackingNote="", trackingDateTime="",cancelReturnOrderId="",
            returnOrderId="",returnEligibleDate="", selectedReason="", writeMessage="", returnOrderImage="", returnOrderVideo="", returnOrderVideoUrl="",
            returnPolicyUrl="";


    private ArrayList<InnerOrderListModel> allItemsInSection;

    boolean isChecked = false;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getIsCancelable() {
        return isCancelable;
    }

    public void setIsCancelable(String isCancelable) {
        this.isCancelable = isCancelable;
    }

    public String getIsReserveOrder() {
        return isReserveOrder;
    }

    public void setIsReserveOrder(String isReserveOrder) {
        this.isReserveOrder = isReserveOrder;
    }

    public String getPaymentReceivedDate() {
        return paymentReceivedDate;
    }

    public void setPaymentReceivedDate(String paymentReceivedDate) {
        this.paymentReceivedDate = paymentReceivedDate;
    }

    public String getTimeLeftForCancel() {
        return timeLeftForCancel;
    }

    public void setTimeLeftForCancel(String timeLeftForCancel) {
        this.timeLeftForCancel = timeLeftForCancel;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getOrderDateTime() {
        return orderDateTime;
    }

    public void setOrderDateTime(String orderDateTime) {
        this.orderDateTime = orderDateTime;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getItemDetails() {
        return itemDetails;
    }

    public void setItemDetails(String itemDetails) {
        this.itemDetails = itemDetails;
    }

    public String getStockNumber() {
        return stockNumber;
    }

    public void setStockNumber(String stockNumber) {
        this.stockNumber = stockNumber;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(String subTotal) {
        this.subTotal = subTotal;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
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

    public String getShowingSubTotal() {
        return showingSubTotal;
    }

    public void setShowingSubTotal(String showingSubTotal) {
        this.showingSubTotal = showingSubTotal;
    }

    public ArrayList<InnerOrderListModel> getAllItemsInSection() {
        return allItemsInSection;
    }

    public void setAllItemsInSection(ArrayList<InnerOrderListModel> allItemsInSection) {
        this.allItemsInSection = allItemsInSection;
    }

    public String getIsReturnable() {
        return isReturnable;
    }

    public void setIsReturnable(String isReturnable) {
        this.isReturnable = isReturnable;
    }

    public String getShape() {
        return shape;
    }

    public void setShape(String shape) {
        this.shape = shape;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCut() {
        return cut;
    }

    public void setCut(String cut) {
        this.cut = cut;
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

    public String getFir() {
        return fir;
    }

    public void setFir(String fir) {
        this.fir = fir;
    }

    public String getLab() {
        return lab;
    }

    public void setLab(String lab) {
        this.lab = lab;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getDepth() {
        return depth;
    }

    public void setDepth(String depth) {
        this.depth = depth;
    }

    public String getCompareDateTime() {
        return compareDateTime;
    }

    public void setCompareDateTime(String compareDateTime) {
        this.compareDateTime = compareDateTime;
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

    public String getDxePrefered() {
        return dxePrefered;
    }

    public void setDxePrefered(String dxePrefered) {
        this.dxePrefered = dxePrefered;
    }

    public String getDiamondImage() {
        return diamondImage;
    }

    public void setDiamondImage(String diamondImage) {
        this.diamondImage = diamondImage;
    }

    public String getGrowthType() {
        return growthType;
    }

    public void setGrowthType(String growthType) {
        this.growthType = growthType;
    }

    public String getCertificateName() {
        return certificateName;
    }

    public void setCertificateName(String certificateName) {
        this.certificateName = certificateName;
    }

    public String getTrackingStatusCode() {
        return trackingStatusCode;
    }

    public void setTrackingStatusCode(String trackingStatusCode) {
        this.trackingStatusCode = trackingStatusCode;
    }

    public String getTrackingStatus() {
        return trackingStatus;
    }

    public void setTrackingStatus(String trackingStatus) {
        this.trackingStatus = trackingStatus;
    }

    public String getTrackingNote() {
        return trackingNote;
    }

    public void setTrackingNote(String trackingNote) {
        this.trackingNote = trackingNote;
    }

    public String getTrackingDateTime() {
        return trackingDateTime;
    }

    public void setTrackingDateTime(String trackingDateTime) {
        this.trackingDateTime = trackingDateTime;
    }

    public String getCancelReturnOrderId() {
        return cancelReturnOrderId;
    }

    public void setCancelReturnOrderId(String cancelReturnOrderId) {
        this.cancelReturnOrderId = cancelReturnOrderId;
    }

    public String getReturnOrderId() {
        return returnOrderId;
    }

    public void setReturnOrderId(String returnOrderId) {
        this.returnOrderId = returnOrderId;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getReturnEligibleDate() {
        return returnEligibleDate;
    }

    public void setReturnEligibleDate(String returnEligibleDate) {
        this.returnEligibleDate = returnEligibleDate;
    }

    public String getSelectedReason() {
        return selectedReason;
    }

    public void setSelectedReason(String selectedReason) {
        this.selectedReason = selectedReason;
    }

    public String getWriteMessage() {
        return writeMessage;
    }

    public void setWriteMessage(String writeMessage) {
        this.writeMessage = writeMessage;
    }

    public String getReturnOrderImage() {
        return returnOrderImage;
    }

    public void setReturnOrderImage(String returnOrderImage) {
        this.returnOrderImage = returnOrderImage;
    }

    public String getReturnOrderVideo() {
        return returnOrderVideo;
    }

    public void setReturnOrderVideo(String returnOrderVideo) {
        this.returnOrderVideo = returnOrderVideo;
    }

    public String getReturnOrderVideoUrl() {
        return returnOrderVideoUrl;
    }

    public void setReturnOrderVideoUrl(String returnOrderVideoUrl) {
        this.returnOrderVideoUrl = returnOrderVideoUrl;
    }

    public String getReturnPolicyUrl() {
        return returnPolicyUrl;
    }

    public void setReturnPolicyUrl(String returnPolicyUrl) {
        this.returnPolicyUrl = returnPolicyUrl;
    }
}
