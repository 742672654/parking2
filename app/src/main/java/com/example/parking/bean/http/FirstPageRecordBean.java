package com.example.parking.bean.http;

public class FirstPageRecordBean {

    private int code;
    private FirstPageRecordDate data;
    private String message;

    public int getCode() { return code; }
    public void setCode(int code) { this.code = code; }
    public FirstPageRecordDate getData() { return data; }
    public void setData(FirstPageRecordDate data) { this.data = data; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    @Override
    public String toString() {
        return "FirstPageRecordBean{" +
                "code=" + code +
                ", data=" + data +
                ", message='" + message + '\'' +
                '}';
    }

    public static class FirstPageRecordDate {

        private int wxnum; //预约数量
        private int nouse; //剩余车位数量

        public int getWxnum() { return wxnum; }
        public void setWxnum(int wxnum) { this.wxnum = wxnum; }
        public int getNouse() { return nouse; }
        public void setNouse(int nouse) { this.nouse = nouse; }

        @Override
        public String toString() {
            return "FirstPageRecordDate{" +
                    "wxnum=" + wxnum +
                    ", nouse=" + nouse +
                    '}';
        }
    }
}
