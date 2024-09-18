package com.diamondxe.Beans;

import android.graphics.drawable.Drawable;

import java.io.Serializable;


public class UPIAppInfoListModel implements Serializable {

    private String packageName;
    private String appName;
    private Drawable appIcon;

    boolean isSelected  = false;

    public UPIAppInfoListModel(String packageName, String appName, Drawable appIcon, boolean isSelected) {
        this.packageName = packageName;
        this.appName = appName;
        this.appIcon = appIcon;
        this.isSelected = isSelected;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getAppName() {
        return appName;
    }

    public Drawable getAppIcon() {
        return appIcon;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
