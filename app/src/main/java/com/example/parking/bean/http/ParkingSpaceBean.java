package com.example.parking.bean.http;

import java.util.List;

public class ParkingSpaceBean {

    private int code;
    private List<ParkingSpaceData> data;
    private String message;

    public int getCode() { return code; }
    public void setCode(int code) { this.code = code; }
    public List<ParkingSpaceData> getData() { return data; }
    public void setData(List<ParkingSpaceData> data) { this.data = data; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }


    @Override
    public String toString() {
        return "ParkingSpaceBean{" +
                "code=" + code +
                ", data=" + data +
                ", message='" + message + '\'' +
                '}';
    }
}
