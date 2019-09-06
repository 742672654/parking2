package com.example.parking.bean.http;

import java.io.Serializable;
import java.util.List;

public class Report_orderlistBean{

    private int code;
    private String message;
    private Report_orderlistData data;

    public int getCode() { return code; }
    public void setCode(int code) { this.code = code; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public Report_orderlistData getData() { return data; }
    public void setData(Report_orderlistData data) { this.data = data; }

    @Override
    public String toString() {
        return "Report_orderlistBean{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }

    public static class Report_orderlistData{

        private int total;// 3,
        private String enddate;// "2019-07-29",  出场,有的地方时间格式不对
        private int size;// 10,
        private String pointid;// "a51e5f23-7632-40e6-be6b-ff307e30cbd5",
        private int page;// 1,
        private String startdate;// "2019-07-22",   入场,有的地方时间格式不对
        private List<Report_orderlistList> list;

        public int getTotal() { return total; }
        public void setTotal(int total) { this.total = total; }
        public String getEnddate() { return enddate; }
        public void setEnddate(String enddate) { this.enddate = enddate; }
        public int getSize() { return size; }
        public void setSize(int size) { this.size = size; }
        public String getPointid() { return pointid; }
        public void setPointid(String pointid) { this.pointid = pointid; }
        public int getPage() { return page; }
        public void setPage(int page) { this.page = page; }
        public String getStartdate() { return startdate; }
        public void setStartdate(String startdate) { this.startdate = startdate; }
        public List<Report_orderlistList> getList() { return list; }
        public void setList(List<Report_orderlistList> list) { this.list = list; }

        @Override
        public String toString() {
            return "Report_orderlistData{" +
                    "total=" + total +
                    ", enddate='" + enddate + '\'' +
                    ", size=" + size +
                    ", pointid='" + pointid + '\'' +
                    ", page=" + page +
                    ", startdate='" + startdate + '\'' +
                    ", list=" + list +
                    '}';
        }
    }

    public static class Report_orderlistList implements Serializable {

        public String PlaceName;// "泉秀街市农行",
        public String OutImage;// "",
        public String CarNum;// "苏GNSNML",
        public String InImage;//"http://parkbucket.oss-cn-shenzhen.aliyuncs.com/point/images/2d01bab305164e9ca8be729f9f959933.png"
        public String Panorama;// "http://parkbucket.oss-cn-shenzhen.aliyuncs.com/point/images/9c58f5152a54469fae5eb632720c4f3b.png"
        public String OrderPrice;// "20.0",
        public String LeaveDate;// "2019-07-26 00:47:14",离场时间
        public String id;// "208", //订单id
        public String PrePrice;// "5.0",
        public String SubName;// "农行车位15",
        public String ParkDate;// "2019-07-25 21:34:45" 入场时间
        public String State; //1.未结算订单 2.已经结算订单3.逃单未补缴 4.逃单已补缴
        public Integer esId;// 46, 逃单id
        public String SubId;//车位id

        @Override
        public String toString() {
            return "Report_orderlistList{" +
                    "PlaceName='" + PlaceName + '\'' +
                    ", OutImage='" + OutImage + '\'' +
                    ", CarNum='" + CarNum + '\'' +
                    ", InImage='" + InImage + '\'' +
                    ", Panorama='" + Panorama + '\'' +
                    ", OrderPrice='" + OrderPrice + '\'' +
                    ", LeaveDate='" + LeaveDate + '\'' +
                    ", id='" + id + '\'' +
                    ", PrePrice='" + PrePrice + '\'' +
                    ", SubName='" + SubName + '\'' +
                    ", ParkDate='" + ParkDate + '\'' +
                    ", State='" + State + '\'' +
                    ", esId=" + esId +
                    ", SubId='" + SubId + '\'' +
                    '}';
        }

    }

}

