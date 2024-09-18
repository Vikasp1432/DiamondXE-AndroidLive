package com.diamondxe.Beans;
import java.util.ArrayList;

public class DrawerMenuModel {

    String id="",parent="",name="";
    int image = 0;
    int imageSelected = 0;
    boolean isSelected = false;
    ArrayList<DrawerMenuModel> subMenu = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public int getImageSelected() {
        return imageSelected;
    }

    public void setImageSelected(int imageSelected) {
        this.imageSelected = imageSelected;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public ArrayList<DrawerMenuModel> getSubMenu() {
        return subMenu;
    }

    public void setSubMenu(ArrayList<DrawerMenuModel> subMenu) {
        this.subMenu = subMenu;
    }
}
