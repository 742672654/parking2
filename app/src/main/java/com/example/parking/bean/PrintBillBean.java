package com.example.parking.bean;


import java.io.Serializable;

/**
 * 打印
 */
public class PrintBillBean implements Serializable {

    private int type;
    private String data;
    private String QRcode;

    public PrintBillBean(){ }

    public PrintBillBean(int type,String data,String QRcode){
        this.type=type;
        this.data=data;
        this.QRcode=QRcode;
    }

    public int getType() { return type; }
    public void setType(int type) { this.type = type; }
    public String getData() { return data; }
    public void setData(String data) { this.data = data; }
    public String getQRcode() { return QRcode; }
    public void setQRcode(String QRcode) { this.QRcode = QRcode; }

    @Override
    public String toString() {
        return "PrintBillBean{" +
                "type=" + type +
                ", data='" + data + '\'' +
                ", QRcode='" + QRcode + '\'' +
                '}';
    }
}
