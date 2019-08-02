package com.example.parking.bean.http;

import java.io.Serializable;

public class OrderAddBean implements Serializable{


    private int code;
    private String message;
    private OrderAddDate data;

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

    public OrderAddDate getData() {
        return data;
    }

    public void setData(OrderAddDate data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "OrderAddBean{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }

    public static class OrderAddDate implements Serializable {



        private String parkTime;//订单创建时间

        private String carnum;//name"闽A12345",
        //创建时间
        private long createtime;//1559185362000,
        //创建人ID
        private String createuserid;//"6cf5f230-908c-4ce6-ba87-a7ed517ddcf6",
        //获取逃单金额
        private String escapeprice;//0,

        private int id;//11,

        private int orderid;//64,

        private String parkDateStr;//"2019-05-30 11:02:28",

        //获取进场时间
        private String parkdate;//1559185348000,

        //泊车点ID
        private String pointid;//"2200021733",


        public String getParkTime() {
            return parkTime;
        }
        public void setParkTime(String parkTime) {
            this.parkTime = parkTime;
        }
        public String getCarnum() {
            return carnum;
        }
        public void setCarnum(String carnum) {
            this.carnum = carnum;
        }
        public long getCreatetime() {
            return createtime;
        }
        public void setCreatetime(long createtime) {
            this.createtime = createtime;
        }
        public String getCreateuserid() {
            return createuserid;
        }
        public void setCreateuserid(String createuserid) {
            this.createuserid = createuserid;
        }
        public String getEscapeprice() {
            return escapeprice;
        }
        public void setEscapeprice(String escapeprice) {
            this.escapeprice = escapeprice;
        }
        public int getId() {
            return id;
        }
        public void setId(int id) {
            this.id = id;
        }
        public int getOrderid() {
            return orderid;
        }
        public void setOrderid(int orderid) {
            this.orderid = orderid;
        }
        public String getParkDateStr() {
            return parkDateStr;
        }
        public void setParkDateStr(String parkDateStr) {
            this.parkDateStr = parkDateStr;
        }
        public String getParkdate() {
            return parkdate;
        }
        public void setParkdate(String parkdate) {
            this.parkdate = parkdate;
        }
        public String getPointid() {
            return pointid;
        }
        public void setPointid(String pointid) {
            this.pointid = pointid;
        }

        @Override
        public String toString() {
            return "OrderAddDate{" +
                    "parkTime='" + parkTime + '\'' +
                    ", carnum='" + carnum + '\'' +
                    ", createtime=" + createtime +
                    ", createuserid='" + createuserid + '\'' +
                    ", escapeprice='" + escapeprice + '\'' +
                    ", id=" + id +
                    ", orderid=" + orderid +
                    ", parkDateStr='" + parkDateStr + '\'' +
                    ", parkdate='" + parkdate + '\'' +
                    ", pointid='" + pointid + '\'' +
                    '}';
        }

    }

}
