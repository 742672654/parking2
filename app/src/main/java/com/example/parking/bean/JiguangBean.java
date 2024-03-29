package com.example.parking.bean;

import java.io.Serializable;

public class JiguangBean implements Serializable {

    private String nOTIFICATION_ID; //极光id
    private String pushTime; //接收到的时间
    private long pushTimeLong; //接收到的时间
    private String msgid;   //uuid
    private String devId;   //订单id
    private String devDock; // 具体车位id
    private String devDockName; //车位名
    private String inOut;   // 出场 out 入场 in
    private String msgType; // move 出入信息  alert 告警信 , finish用户扫码缴费
    private String photo_path;//警告的照片地址
    private String subid;//订单ID

    private int state = 0; // 0未处理，1已经处理
    private String stateTime; // 处理时间

    public String getnOTIFICATION_ID() { return nOTIFICATION_ID; }
    public void setnOTIFICATION_ID(String nOTIFICATION_ID) { this.nOTIFICATION_ID = nOTIFICATION_ID; }
    public String getPushTime() { return pushTime; }
    public void setPushTime(String pushTime) { this.pushTime = pushTime; }
    public long getPushTimeLong() { return pushTimeLong; }
    public void setPushTimeLong(long pushTimeLong) { this.pushTimeLong = pushTimeLong; }
    public String getMsgid() { return msgid; }
    public void setMsgid(String msgid) { this.msgid = msgid; }
    public String getDevId() { return devId; }
    public void setDevId(String devId) { this.devId = devId; }
    public String getDevDock() { return devDock; }
    public void setDevDock(String devDock) { this.devDock = devDock; }
    public String getDevDockName() { return devDockName; }
    public void setDevDockName(String devDockName) { this.devDockName = devDockName; }
    public String getInOut() { return inOut; }
    public void setInOut(String inOut) { this.inOut = inOut; }
    public String getMsgType() { return msgType; }
    public void setMsgType(String msgType) { this.msgType = msgType; }
    public String getPhoto_path() { return photo_path; }
    public void setPhoto_path(String photo_path) { this.photo_path = photo_path; }
    public String getSubid() { return subid; }
    public void setSubid(String subid) { this.subid = subid; }
    public int getState() { return state; }
    public void setState(int state) { this.state = state; }
    public String getStateTime() { return stateTime; }
    public void setStateTime(String stateTime) { this.stateTime = stateTime; }

    @Override
    public String toString() {
        return "JiguangBean{" +
                "nOTIFICATION_ID='" + nOTIFICATION_ID + '\'' +
                ", pushTime='" + pushTime + '\'' +
                ", pushTimeLong=" + pushTimeLong +
                ", msgid='" + msgid + '\'' +
                ", devId='" + devId + '\'' +
                ", devDock='" + devDock + '\'' +
                ", devDockName='" + devDockName + '\'' +
                ", inOut='" + inOut + '\'' +
                ", msgType='" + msgType + '\'' +
                ", photo_path='" + photo_path + '\'' +
                ", subid='" + subid + '\'' +
                ", state=" + state +
                ", stateTime='" + stateTime + '\'' +
                '}';
    }
}
