package com.diamondxe.Beans;

import java.io.Serializable;


public class DiamondShapeImageNameModel implements Serializable {

     String carat="", image="";
    boolean isSelected = false;

    public String getCarat() {
        return carat;
    }

    public void setCarat(String carat) {
        this.carat = carat;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
