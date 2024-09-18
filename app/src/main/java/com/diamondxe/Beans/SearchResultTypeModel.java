package com.diamondxe.Beans;

import java.io.Serializable;


public class SearchResultTypeModel implements Serializable {

    String stock_id = "", item_name= "", category= "", supplier_id= "", cut_grade= "", certificate_name= "", certificate_no= "", polish= "", symmetry= "", measurement= "",
            fluorescence_intensity= "", carat= "", color= "", clarity= "", shape= "", shade= "", table_perc= "", depth_perc= "", luster= "", eye_clean= "", diamond_image= "",
            diamond_video= "", total_gst_perc= "", status= "", subtotal= "", total_price= "", is_returnable= "", dxe_prefered= "", is_cart= "", is_wishlist= "", on_hold= "", r_discount= "",
            r_discount_type= "",currencySymbol = "", showingSubTotal="",stock_no="";

    boolean isVisible = false;

    public String getStock_id() {
        return stock_id;
    }

    public void setStock_id(String stock_id) {
        this.stock_id = stock_id;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSupplier_id() {
        return supplier_id;
    }

    public void setSupplier_id(String supplier_id) {
        this.supplier_id = supplier_id;
    }

    public String getCut_grade() {
        return cut_grade;
    }

    public void setCut_grade(String cut_grade) {
        this.cut_grade = cut_grade;
    }

    public String getCertificate_name() {
        return certificate_name;
    }

    public void setCertificate_name(String certificate_name) {
        this.certificate_name = certificate_name;
    }

    public String getCertificate_no() {
        return certificate_no;
    }

    public void setCertificate_no(String certificate_no) {
        this.certificate_no = certificate_no;
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

    public String getFluorescence_intensity() {
        return fluorescence_intensity;
    }

    public void setFluorescence_intensity(String fluorescence_intensity) {
        this.fluorescence_intensity = fluorescence_intensity;
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

    public String getTable_perc() {
        return table_perc;
    }

    public void setTable_perc(String table_perc) {
        this.table_perc = table_perc;
    }

    public String getDepth_perc() {
        return depth_perc;
    }

    public void setDepth_perc(String depth_perc) {
        this.depth_perc = depth_perc;
    }

    public String getLuster() {
        return luster;
    }

    public void setLuster(String luster) {
        this.luster = luster;
    }

    public String getEye_clean() {
        return eye_clean;
    }

    public void setEye_clean(String eye_clean) {
        this.eye_clean = eye_clean;
    }

    public String getDiamond_image() {
        return diamond_image;
    }

    public void setDiamond_image(String diamond_image) {
        this.diamond_image = diamond_image;
    }

    public String getDiamond_video() {
        return diamond_video;
    }

    public void setDiamond_video(String diamond_video) {
        this.diamond_video = diamond_video;
    }

    public String getTotal_gst_perc() {
        return total_gst_perc;
    }

    public void setTotal_gst_perc(String total_gst_perc) {
        this.total_gst_perc = total_gst_perc;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(String subtotal) {
        this.subtotal = subtotal;
    }

    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }

    public String getIs_returnable() {
        return is_returnable;
    }

    public void setIs_returnable(String is_returnable) {
        this.is_returnable = is_returnable;
    }

    public String getDxe_prefered() {
        return dxe_prefered;
    }

    public void setDxe_prefered(String dxe_prefered) {
        this.dxe_prefered = dxe_prefered;
    }

    public String getIs_cart() {
        return is_cart;
    }

    public void setIs_cart(String is_cart) {
        this.is_cart = is_cart;
    }

    public String getIs_wishlist() {
        return is_wishlist;
    }

    public void setIs_wishlist(String is_wishlist) {
        this.is_wishlist = is_wishlist;
    }

    public String getOn_hold() {
        return on_hold;
    }

    public void setOn_hold(String on_hold) {
        this.on_hold = on_hold;
    }

    public String getR_discount() {
        return r_discount;
    }

    public void setR_discount(String r_discount) {
        this.r_discount = r_discount;
    }

    public String getR_discount_type() {
        return r_discount_type;
    }

    public void setR_discount_type(String r_discount_type) {
        this.r_discount_type = r_discount_type;
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

    public String getStock_no() {
        return stock_no;
    }

    public void setStock_no(String stock_no) {
        this.stock_no = stock_no;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }
}
