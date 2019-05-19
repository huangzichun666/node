package com.example.nodepad;

import android.graphics.Bitmap;

import java.sql.Date;

public class Node {
    private int nodeID;
    private String nodeTitle;
    private String nodeContent;
    private Date saveDate;
    private Date changeDate;
    private Bitmap image;

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public Node(String title, String content, Date saveDate, int nodeID,Bitmap image){
        this.nodeTitle=title;
        this.nodeContent=content;
        this.saveDate=saveDate;
        this.nodeID = nodeID;
        this.image=image;
    }

    public int getNodeID() {
        return nodeID;
    }

    public void setNodeID(int nodeID) {
        this.nodeID = nodeID;
    }

    public String getNodeTitle() {
        return nodeTitle;
    }

    public void setNodeTitle(String nodeTitle) {
        this.nodeTitle = nodeTitle;
    }

    public String getNodeContent() {
        return nodeContent;
    }

    public void setNodeContent(String nodeContent) {
        this.nodeContent = nodeContent;
    }

    public Date getSaveDate() {
        return saveDate;
    }

    public void setSaveDate(Date saveDate) {
        this.saveDate = saveDate;
    }

    public Date getChangeDate() {
        return changeDate;
    }

    public void setChangeDate(Date changeDate) {
        this.changeDate = changeDate;
    }

}
