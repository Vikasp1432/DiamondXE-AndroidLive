package com.diamondxe.Beans;

import java.io.Serializable;
import java.util.ArrayList;


public class HomeListModel implements Serializable {

    private String id="", user_id="";

    String giftID="", giftImage = "";

    String arrivalsId="", arrivalsImage = "";

    public ArrayList<HomeListModel> allGiftArrayList;
    public ArrayList<HomeListModel> allArrivalsArrayList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getGiftID() {
        return giftID;
    }

    public void setGiftID(String giftID) {
        this.giftID = giftID;
    }

    public String getGiftImage() {
        return giftImage;
    }

    public void setGiftImage(String giftImage) {
        this.giftImage = giftImage;
    }

    public String getArrivalsId() {
        return arrivalsId;
    }

    public void setArrivalsId(String arrivalsId) {
        this.arrivalsId = arrivalsId;
    }

    public String getArrivalsImage() {
        return arrivalsImage;
    }

    public void setArrivalsImage(String arrivalsImage) {
        this.arrivalsImage = arrivalsImage;
    }

    public ArrayList<HomeListModel> getAllGiftArrayList() {
        return allGiftArrayList;
    }

    public void setAllGiftArrayList(ArrayList<HomeListModel> allGiftArrayList) {
        this.allGiftArrayList = allGiftArrayList;
    }

    public ArrayList<HomeListModel> getAllArrivalsArrayList() {
        return allArrivalsArrayList;
    }

    public void setAllArrivalsArrayList(ArrayList<HomeListModel> allArrivalsArrayList) {
        this.allArrivalsArrayList = allArrivalsArrayList;
    }
}
