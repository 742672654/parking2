package com.example.parking.bean.http;


import android.util.Log;
import com.example.parking.util.TimeUtil;
import java.io.Serializable;
import java.util.List;


public class OrderlistBean implements Serializable {

    private static final String TAG = "OrderlistBean";

    private int code;
    private String message;
    private List<OrderlistData> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<OrderlistData> getData() {
        return data;
    }

    public void setData(List<OrderlistData> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "OrderlistBean{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }

    public static class OrderlistData implements Serializable{

        private String subid;//": "106be2f5-55f5-454d-841c-8beaefd5aa7f",
        private String subname;//测试33",
        private int orderType;// 1,  1、现场创建2、抢车位、预定车位创建
        private String carNo;//闽C12345",
        private String time;//2019-06-25 16:22:25",
        private String id;// 73  订单id
        private String orderNo;//VW73", 订单编号

        public OrderlistData(){};

        public OrderlistData(SelectSubPlaceBean.SelectSubPlaceData selectSubPlaceData){

            this.subid=selectSubPlaceData.getId();
            this.subname=selectSubPlaceData.getCode();
            this.orderType=selectSubPlaceData.getOrderType();
            this.carNo=selectSubPlaceData.getCarnum();
            this.id=selectSubPlaceData.getOrderid();
            this.orderNo=selectSubPlaceData.getOrderNo();

            this.time= selectSubPlaceData.getParktime();

        };

        public String getSubid() {
            return subid;
        }

        public void setSubid(String subid) {
            this.subid = subid;
        }

        public int getOrderType() {
            return orderType;
        }

        public void setOrderType(int orderType) {
            this.orderType = orderType;
        }

        public String getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(String orderNo) {
            this.orderNo = orderNo;
        }

        public String getCarNo() {
            return carNo;
        }

        public void setCarNo(String carNo) {
            this.carNo = carNo;
        }

        public String getSubname() {
            return subname;
        }

        public void setSubname(String subname) {
            this.subname = subname;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        @Override
        public String toString() {
            return "OrderlistData{" +
                    "subid='" + subid + '\'' +
                    ", orderType=" + orderType +
                    ", orderNo='" + orderNo + '\'' +
                    ", carNo='" + carNo + '\'' +
                    ", subname='" + subname + '\'' +
                    ", time='" + time + '\'' +
                    ", id='" + id + '\'' +
                    '}';
        }
    }

}
