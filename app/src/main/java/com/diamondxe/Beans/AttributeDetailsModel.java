package com.diamondxe.Beans;

import java.io.Serializable;


public class AttributeDetailsModel implements Serializable {

    String ParentAttribTypeId="",ParentAttribType="",AttribId="", AttribTypeId = "",AttribType="",AttribCode="", SortOrder="", DisplayAttr="",
            fancyColorCode="", filterType="", filterTypeTo = "", priceFrm="", priceTo="";

    boolean isSelected=false;

    boolean isFirstPosition = false;

    public String getParentAttribTypeId() {
        return ParentAttribTypeId;
    }

    public void setParentAttribTypeId(String parentAttribTypeId) {
        ParentAttribTypeId = parentAttribTypeId;
    }

    public String getParentAttribType() {
        return ParentAttribType;
    }

    public void setParentAttribType(String parentAttribType) {
        ParentAttribType = parentAttribType;
    }

    public String getAttribId() {
        return AttribId;
    }

    public void setAttribId(String attribId) {
        AttribId = attribId;
    }

    public String getAttribTypeId() {
        return AttribTypeId;
    }

    public void setAttribTypeId(String attribTypeId) {
        AttribTypeId = attribTypeId;
    }

    public String getAttribType() {
        return AttribType;
    }

    public void setAttribType(String attribType) {
        AttribType = attribType;
    }

    public String getAttribCode() {
        return AttribCode;
    }

    public void setAttribCode(String attribCode) {
        AttribCode = attribCode;
    }

    public String getSortOrder() {
        return SortOrder;
    }

    public void setSortOrder(String sortOrder) {
        SortOrder = sortOrder;
    }

    public String getDisplayAttr() {
        return DisplayAttr;
    }

    public void setDisplayAttr(String displayAttr) {
        DisplayAttr = displayAttr;
    }

    public String getFancyColorCode() {
        return fancyColorCode;
    }

    public void setFancyColorCode(String fancyColorCode) {
        this.fancyColorCode = fancyColorCode;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isFirstPosition() {
        return isFirstPosition;
    }

    public void setFirstPosition(boolean firstPosition) {
        isFirstPosition = firstPosition;
    }

    public String getFilterType() {
        return filterType;
    }

    public void setFilterType(String filterType) {
        this.filterType = filterType;
    }

    public String getFilterTypeTo() {
        return filterTypeTo;
    }

    public void setFilterTypeTo(String filterTypeTo) {
        this.filterTypeTo = filterTypeTo;
    }

    public String getPriceFrm() {
        return priceFrm;
    }

    public void setPriceFrm(String priceFrm) {
        this.priceFrm = priceFrm;
    }

    public String getPriceTo() {
        return priceTo;
    }

    public void setPriceTo(String priceTo) {
        this.priceTo = priceTo;
    }
}
