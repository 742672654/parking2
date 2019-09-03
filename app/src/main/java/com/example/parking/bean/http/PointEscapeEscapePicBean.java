package com.example.parking.bean.http;

public class PointEscapeEscapePicBean {

    private Integer code;
    private String message;
    private pointEscapeEscapePicData data;

    public Integer getCode() { return code; }
    public void setCode(Integer code) { this.code = code; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public pointEscapeEscapePicData getData() { return data; }
    public void setData(pointEscapeEscapePicData data) { this.data = data; }

    @Override
    public String toString() {
        return "pointEscapeEscapePicBean{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }

    public static class pointEscapeEscapePicData{

        private String panorama;
        private String inimage;

        public String getPanorama() { return panorama; }
        public void setPanorama(String panorama) { this.panorama = panorama; }
        public String getInimage() { return inimage; }
        public void setInimage(String inimage) { this.inimage = inimage; }

        @Override
        public String toString() {
            return "pointEscapeEscapePicData{" +
                    "panorama='" + panorama + '\'' +
                    ", inimage='" + inimage + '\'' +
                    '}';
        }

    }

}
