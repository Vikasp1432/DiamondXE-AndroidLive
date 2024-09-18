package com.diamondxe.Beans;

import java.io.Serializable;


public class MediaSpotLightModel implements Serializable {

    String id="", image = "", description="", link="";

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
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
