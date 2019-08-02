package com.example.parking.bean;


import com.example.parking.bean.http.HttpUserBean;

public class UserBean {


    private String phone;
    private String password;
    private String edition;
    private String avatarUrl;
    private String nickName;
    private String mobile;
    private String token;
    private String parkname;
    private String parkid;
    private String parkaddr;
    public UserBean(){}

    public UserBean(HttpUserBean.UserInfo httpUserBean){

        avatarUrl = httpUserBean.getAvatarUrl();
        nickName = httpUserBean.getNickName();
        mobile = httpUserBean.getMobile();
        token = httpUserBean.getToken();
        parkname = httpUserBean.getParkname();
        parkid = httpUserBean.getParkid().replace("'","");
        parkaddr = httpUserBean.getParkaddr();
    }



    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getEdition() { return edition; }
    public void setEdition(String edition) { this.edition = edition; }
    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }
    public String getNickName() { return nickName; }
    public void setNickName(String nickName) { this.nickName = nickName; }
    public String getMobile() { return mobile; }
    public void setMobile(String mobile) { this.mobile = mobile; }
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public String getParkname() { return parkname; }
    public void setParkname(String parkname) { this.parkname = parkname; }
    public String getParkid() { return parkid; }
    public void setParkid(String parkid) { this.parkid = parkid; }
    public String getParkaddr() { return parkaddr; }
    public void setParkaddr(String parkaddr) { this.parkaddr = parkaddr; }

    @Override
    public String toString() {
        return "UserBean{" +
                "phone='" + phone + '\'' +
                ", password='" + password + '\'' +
                ", edition='" + edition + '\'' +
                ", avatarUrl='" + avatarUrl + '\'' +
                ", nickName='" + nickName + '\'' +
                ", mobile='" + mobile + '\'' +
                ", token='" + token + '\'' +
                ", parkname='" + parkname + '\'' +
                ", parkid='" + parkid + '\'' +
                ", parkaddr='" + parkaddr + '\'' +
                '}';
    }
}
