package com.diamondxe.Beans;

import java.io.Serializable;


public class MakeTypeModel implements Serializable {

    String AttribId="", AttribTypeId = "",AttribType="",AttribCode="", SortOrder="", DisplayAttr="";

    boolean isSelected=false;

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

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
