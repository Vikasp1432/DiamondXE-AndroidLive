package com.diamondxe.Beans.MyOrder;

import java.io.Serializable;
import java.util.ArrayList;


public class CancelOrderReasonListModel implements Serializable {

    String id="", reason="";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
