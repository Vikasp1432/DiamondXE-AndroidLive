package com.diamondxe.Beans.Dealer;

import java.io.Serializable;


public class DealerMarkupListModel implements Serializable {

    String id = "", fromValue = "", toValue="", naturalValue = "", labGrownValue = "";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFromValue() {
        return fromValue;
    }

    public void setFromValue(String fromValue) {
        this.fromValue = fromValue;
    }

    public String getToValue() {
        return toValue;
    }

    public void setToValue(String toValue) {
        this.toValue = toValue;
    }

    public String getNaturalValue() {
        return naturalValue;
    }

    public void setNaturalValue(String naturalValue) {
        this.naturalValue = naturalValue;
    }

    public String getLabGrownValue() {
        return labGrownValue;
    }

    public void setLabGrownValue(String labGrownValue) {
        this.labGrownValue = labGrownValue;
    }
}
