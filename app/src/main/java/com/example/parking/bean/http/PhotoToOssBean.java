package com.example.parking.bean.http;


public class PhotoToOssBean {

    private String carmun;
    private String imgurl;

    public String getCarmun() {
        return carmun;
    }

    public void setCarmun(String carmun) {
        this.carmun = carmun;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    @Override
    public String toString() {
        return "PhotoToOssBean{" +
                "carmun='" + carmun + '\'' +
                ", imgurl='" + imgurl + '\'' +
                '}';
    }

    //    private int code;
//    private String message;
//    private PhotoToOssData data;
//
//    public int getCode() { return code; }
//    public void setCode(int code) { this.code = code; }
//    public String getMessage() { return message; }
//    public void setMessage(String message) { this.message = message; }
//    public PhotoToOssData getData() { return data; }
//    public void setData(PhotoToOssData data) { this.data = data; }
//
//    @Override
//    public String toString() {
//        return "PhotoToOssBean{" +
//                "code=" + code +
//                ", message='" + message + '\'' +
//                ", data=" + data +
//                '}';
//    }
//
//    public static class PhotoToOssData{
//
//        private String carmun;
//        private String imgurl;
//
//
//        public String getCarmun() { return carmun; }
//        public void setCarmun(String carmun) { this.carmun = carmun; }
//        public String getImgurl() { return imgurl; }
//        public void setImgurl(String imgurl) { this.imgurl = imgurl; }
//
//        @Override
//        public String toString() {
//            return "PhotoToOssData{" +
//                    "carmun='" + carmun + '\'' +
//                    ", imgurl='" + imgurl + '\'' +
//                    '}';
//        }
//    }

}
