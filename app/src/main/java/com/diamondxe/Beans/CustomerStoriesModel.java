package com.diamondxe.Beans;

import java.io.Serializable;


public class CustomerStoriesModel implements Serializable {

    String id="", image = "";
    int drawable;
    boolean isLocal = false;

    public String getId() {
        return id;
    }

    public void setId(String key) {
        this.id = id;
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
