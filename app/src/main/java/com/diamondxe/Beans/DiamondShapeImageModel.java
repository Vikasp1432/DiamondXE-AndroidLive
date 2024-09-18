package com.diamondxe.Beans;

import java.io.Serializable;
import java.util.List;


public class DiamondShapeImageModel implements Serializable {

    String  image = "", carat="", shapeType="",attribCode="",shape="";
    boolean isSelected = false;
    boolean isFirstPosition = false;
    private int drawable;

    private List<DiamondShapeImageNameModel> details;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCarat() {
        return carat;
    }

    public void setCarat(String carat) {
        this.carat = carat;
    }

    public String getShapeType() {
        return shapeType;
    }

    public void setShapeType(String shapeType) {
        this.shapeType = shapeType;
    }

    public String getAttribCode() {
        return attribCode;
    }

    public void setAttribCode(String attribCode) {
        this.attribCode = attribCode;
    }

    public String getShape() {
        return shape;
    }

    public void setShape(String shape) {
        this.shape = shape;
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

    public int getDrawable() {
        return drawable;
    }

    public void setDrawable(int drawable) {
        this.drawable = drawable;
    }

    public List<DiamondShapeImageNameModel> getDetails() {
        return details;
    }

    public void setDetails(List<DiamondShapeImageNameModel> details) {
        this.details = details;
    }
}
