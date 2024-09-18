package com.diamondxe.Beans;

import android.graphics.drawable.Drawable;

import java.io.Serializable;


public class GiftImageModel implements Serializable {

    String link="", image = "";
    int drawable;
    boolean isLocal = false;

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
}
