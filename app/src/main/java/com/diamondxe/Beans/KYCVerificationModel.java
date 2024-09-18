package com.diamondxe.Beans;

import java.io.Serializable;


public class KYCVerificationModel implements Serializable {

    String attachmentId = "", attachmentType = "", fileName = "", fileUrl = "", attachmentDate = "", attachmentDesc = "", verifiedInd = "";
    boolean isSelected=false;
    boolean isFirstPosition = false;

    public String getAttachmentId() {
        return attachmentId;
    }

    public void setAttachmentId(String attachmentId) {
        this.attachmentId = attachmentId;
    }

    public String getAttachmentType() {
        return attachmentType;
    }

    public void setAttachmentType(String attachmentType) {
        this.attachmentType = attachmentType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getAttachmentDate() {
        return attachmentDate;
    }

    public void setAttachmentDate(String attachmentDate) {
        this.attachmentDate = attachmentDate;
    }

    public String getAttachmentDesc() {
        return attachmentDesc;
    }

    public void setAttachmentDesc(String attachmentDesc) {
        this.attachmentDesc = attachmentDesc;
    }

    public String getVerifiedInd() {
        return verifiedInd;
    }

    public void setVerifiedInd(String verifiedInd) {
        this.verifiedInd = verifiedInd;
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
