package com.example.parking.bean.http;


public class OrderDetailsBean {

    private int code;
    private String message;
    private OrderDetailsData data;


    public int getCode() { return code; }
    public void setCode(int code) { this.code = code; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public OrderDetailsData getData() { return data; }
    public void setData(OrderDetailsData data) { this.data = data; }


    @Override
    public String toString() {
        return "OrderDetailsBean{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }

    public static class OrderDetailsData {


        private double wait_price;// 34,
        private int id;// 79,
        private double pre_price;// 6,
        private double price;// 40,
        private String panorama; //全景照
        private String inimage;//车牌照

        private String pointId;// "1d466235-5eb7-4a3d-81e8-ce90be5170be",
        private String startTime;// "2019-06-25 20:36:43",
        private String time;// "13小时15分钟",
        private String carnum;// "渝NNMN6"
        private String address;// null,


        public double getWait_price() { return wait_price; }
        public void setWait_price(double wait_price) { this.wait_price = wait_price; }
        public int getId() { return id; }
        public void setId(int id) { this.id = id; }
        public double getPre_price() { return pre_price; }
        public void setPre_price(double pre_price) { this.pre_price = pre_price; }
        public double getPrice() { return price; }
        public void setPrice(double price) { this.price = price; }
        public String getPanorama() { return panorama; }
        public void setPanorama(String panorama) { this.panorama = panorama; }
        public String getInimage() { return inimage; }
        public void setInimage(String inimage) { this.inimage = inimage; }
        public String getPointId() { return pointId; }
        public void setPointId(String pointId) { this.pointId = pointId; }
        public String getStartTime() { return startTime; }
        public void setStartTime(String startTime) { this.startTime = startTime; }
        public String getTime() { return time; }
        public void setTime(String time) { this.time = time; }
        public String getCarnum() { return carnum; }
        public void setCarnum(String carnum) { this.carnum = carnum; }
        public String getAddress() { return address; }
        public void setAddress(String address) { this.address = address; }

        @Override
        public String toString() {
            return "OrderDetailsData{" +
                    "wait_price=" + wait_price +
                    ", id=" + id +
                    ", pre_price=" + pre_price +
                    ", price=" + price +
                    ", panorama='" + panorama + '\'' +
                    ", inimage='" + inimage + '\'' +
                    ", pointId='" + pointId + '\'' +
                    ", startTime='" + startTime + '\'' +
                    ", time='" + time + '\'' +
                    ", carnum='" + carnum + '\'' +
                    ", address='" + address + '\'' +
                    '}';
        }
    }

}
