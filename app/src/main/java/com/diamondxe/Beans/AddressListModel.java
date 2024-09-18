package com.diamondxe.Beans;

import java.io.Serializable;


public class AddressListModel implements Serializable {

    String addressId = "", addressTypeId = "", addressType = "", address1 = "", address2 = "", cityNameS = "", cityName = "",
            stateNameS = "", stateName = "", countryNameS = "", countryName = "", pinCode = "", isDefault = "", emailId = "", mobileNo = "", gSTNo = "", businessName = "", mobileDialCode = "",
            mobileNumber  = "";
    boolean isSelected=false;
    boolean isFirstPosition = false;

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getAddressTypeId() {
        return addressTypeId;
    }

    public void setAddressTypeId(String addressTypeId) {
        this.addressTypeId = addressTypeId;
    }

    public String getAddressType() {
        return addressType;
    }

    public void setAddressType(String addressType) {
        this.addressType = addressType;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getCityNameS() {
        return cityNameS;
    }

    public void setCityNameS(String cityNameS) {
        this.cityNameS = cityNameS;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getStateNameS() {
        return stateNameS;
    }

    public void setStateNameS(String stateNameS) {
        this.stateNameS = stateNameS;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getCountryNameS() {
        return countryNameS;
    }

    public void setCountryNameS(String countryNameS) {
        this.countryNameS = countryNameS;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public String getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getgSTNo() {
        return gSTNo;
    }

    public void setgSTNo(String gSTNo) {
        this.gSTNo = gSTNo;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getMobileDialCode() {
        return mobileDialCode;
    }

    public void setMobileDialCode(String mobileDialCode) {
        this.mobileDialCode = mobileDialCode;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isFirstPosition() {
        return isFirstPosition;
    }

    public void setFirstPosition(boolean firstPosition) {
        isFirstPosition = firstPosition;
    }
}
