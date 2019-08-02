package com.example.parking.bean;

public class OrderDbBean {


    private String id;
    private String photo1_path;
    private String photo1_url;
    private String photo2_path;
    private String photo2_url;
    private String time;


    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getPhoto1_path() { return photo1_path; }
    public void setPhoto1_path(String photo1_path) { this.photo1_path = photo1_path; }
    public String getPhoto1_url() { return photo1_url; }
    public void setPhoto1_url(String photo1_url) { this.photo1_url = photo1_url; }
    public String getPhoto2_path() { return photo2_path; }
    public void setPhoto2_path(String photo2_path) { this.photo2_path = photo2_path; }
    public String getPhoto2_url() { return photo2_url; }
    public void setPhoto2_url(String photo2_url) { this.photo2_url = photo2_url; }
    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }

    @Override
    public String toString() {
        return "OrderDbBean{" +
                "id='" + id + '\'' +
                ", photo1_path='" + photo1_path + '\'' +
                ", photo1_url='" + photo1_url + '\'' +
                ", photo2_path='" + photo2_path + '\'' +
                ", photo2_url='" + photo2_url + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
