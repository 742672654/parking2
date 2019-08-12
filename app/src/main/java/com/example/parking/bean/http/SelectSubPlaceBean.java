package com.example.parking.bean.http;

import java.io.Serializable;
import java.util.List;

public class SelectSubPlaceBean implements Serializable{



    private Integer code;
    private String message;
    private List<SelectSubPlaceData> data;


    public Integer getCode() { return code; }
    public void setCode(Integer code) { this.code = code; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public List<SelectSubPlaceData> getData() { return data; }
    public void setData(List<SelectSubPlaceData> data) { this.data = data; }

    @Override
    public String toString() {
        return "SelectSubPlaceBean{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }


    public static class SelectSubPlaceData implements Serializable {

        //{"havecar":1,"code":"测试7","id":"03ad37b3-6597-4646-8fd0-feb1206287d2"}
        private String id;  //车位id
        private String code;// "测试6",  车位名
        private Integer havecar;// 2,   /2是有车，1是没车
        private String parktime;// 1561460260000, 入场时间
        private Double preprice;// 8,  //预支付金额
        private String carnum;//"渝M5434N"

        private String orderid;// 73  订单id
        private String orderNo;//VW73", 订单编号
        private Integer orderType;// 1,  1、现场创建2、抢车位、预定车位创建


        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public String getCode() { return code; }
        public void setCode(String code) { this.code = code; }
        public Integer getHavecar() { return havecar; }
        public void setHavecar(Integer havecar) { this.havecar = havecar; }
        public String getParktime() { return parktime; }
        public void setParktime(String parktime) { this.parktime = parktime; }
        public Double getPreprice() {
            return preprice;
        }
        public void setPreprice(Double preprice) {
            this.preprice = preprice;
        }
        public String getCarnum() {
            return carnum;
        }
        public void setCarnum(String carnum) {
            this.carnum = carnum;
        }
        public String getOrderid() { return orderid; }
        public void setOrderid(String orderid) { this.orderid = orderid; }
        public String getOrderNo() {
            return orderNo;
        }
        public void setOrderNo(String orderNo) {
            this.orderNo = orderNo;
        }
        public Integer getOrderType() {
            return orderType;
        }
        public void setOrderType(Integer orderType) {
            this.orderType = orderType;
        }


        @Override
        public String toString() {
            return "SelectSubPlaceData{" +
                    "id='" + id + '\'' +
                    ", code='" + code + '\'' +
                    ", havecar=" + havecar +
                    ", parktime=" + parktime +
                    ", preprice=" + preprice +
                    ", carnum='" + carnum + '\'' +
                    ", orderid='" + orderid + '\'' +
                    ", orderNo='" + orderNo + '\'' +
                    ", orderType=" + orderType +
                    '}';
        }
    }

}

