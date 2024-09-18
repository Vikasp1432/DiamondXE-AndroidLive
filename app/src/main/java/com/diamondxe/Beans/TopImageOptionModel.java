package com.diamondxe.Beans;

import java.io.Serializable;


public class TopImageOptionModel implements Serializable {

    String link="", image = "", name="";
    int drawable;
    boolean isLocal = false;

    boolean isFirstPosition = false;
    boolean isLastPosition = false;

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
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

    public int getDrawable() {
        return drawable;
    }

    public void setDrawable(int drawable) {
        this.drawable = drawable;
    }

    public boolean isLocal() {
        return isLocal;
    }

    public void setLocal(boolean local) {
        isLocal = local;
    }

    public boolean isFirstPosition() {
        return isFirstPosition;
    }

    public void setFirstPosition(boolean firstPosition) {
        isFirstPosition = firstPosition;
    }

    public boolean isLastPosition() {
        return isLastPosition;
    }

    public void setLastPosition(boolean lastPosition) {
        isLastPosition = lastPosition;
    }
}
