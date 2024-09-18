package com.diamondxe.Beans;

import java.io.Serializable;


public class SortingPriceTypeModel implements Serializable {

    String id ="", type = "";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
