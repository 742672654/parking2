package com.example.parking.bean;

public class ResponseBean {

    private Integer code;
    private String data;
    private String message;
    private String mobile;

    public String getMobile() { return mobile; }
    public void setMobile(String mobile) { this.mobile = mobile; }
    public Integer getCode() {
        return code;
    }
    public void setCode(Integer code) {
        this.code = code;
    }
    public String getData() {
        return data;
    }
    public void setData(String data) {
        this.data = data;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ResponseBean{" +
                "code=" + code +
                ", data='" + data + '\'' +
                ", message='" + message + '\'' +
                ", mobile='" + mobile + '\'' +
                '}';
    }
}
