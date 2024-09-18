package com.diamondxe.Beans;

import java.io.Serializable;


public class ShapeImageModel implements Serializable {

    String  image = "",attribCode="";
    boolean isSelected = false;
    boolean isFirstPosition = false;
    int drawable;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAttribCode() {
        return attribCode;
    }

    public void setAttribCode(String attribCode) {
        this.attribCode = attribCode;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getDrawable() {
        return drawable;
    }

    public void setDrawable(int drawable) {
        this.drawable = drawable;
    }

    public boolean isFirstPosition() {
        return isFirstPosition;
    }

    public void setFirstPosition(boolean firstPosition) {
        isFirstPosition = firstPosition;
    }
}
