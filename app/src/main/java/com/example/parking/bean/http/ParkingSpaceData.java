package com.example.parking.bean.http;

public class ParkingSpaceData {

    private String createdate;// 1557371719000,
    private String havecar;// 1,
    private String id;// 237a7d97-3e80-4371-a109-6a2b9f50cf82,
    private String lockid;// 2200021733,
    private String longitude;// 0,
    private String parkbelongplaceid;// 1d466235-5eb7-4a3d-81e8-ce90be5170be,
    private String parkid;// 0,
    private String placeaddr;// 001,
    private String placecode;// 001,
    private String placetype;// 1,
    private String state;// 1,
    private String subname;// "路外车位2",


    public String getCreatedate() {
        return createdate;
    }
    public void setCreatedate(String createdate) {
        this.createdate = createdate;
    }
    public String getHavecar() {
        return havecar;
    }
    public void setHavecar(String havecar) {
        this.havecar = havecar;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getLockid() {
        return lockid;
    }
    public void setLockid(String lockid) {
        this.lockid = lockid;
    }
    public String getLongitude() {
        return longitude;
    }
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
    public String getParkbelongplaceid() {
        return parkbelongplaceid;
    }
    public void setParkbelongplaceid(String parkbelongplaceid) {
        this.parkbelongplaceid = parkbelongplaceid;
    }
    public String getParkid() {
        return parkid;
    }
    public void setParkid(String parkid) {
        this.parkid = parkid;
    }
    public String getPlaceaddr() {
        return placeaddr;
    }
    public void setPlaceaddr(String placeaddr) {
        this.placeaddr = placeaddr;
    }
    public String getPlacecode() {
        return placecode;
    }
    public void setPlacecode(String placecode) {
        this.placecode = placecode;
    }
    public String getPlacetype() {
        return placetype;
    }
    public void setPlacetype(String placetype) {
        this.placetype = placetype;
    }
    public String getState() {
        return state;
    }
    public void setState(String state) {
        this.state = state;
    }
    public String getSubname() {
        return subname;
    }
    public void setSubname(String subname) {
        this.subname = subname;
    }

    @Override
    public String toString() {
        return "ParkingSpaceBean{" +
                "createdate='" + createdate + '\'' +
                ", havecar='" + havecar + '\'' +
                ", id='" + id + '\'' +
                ", lockid='" + lockid + '\'' +
                ", longitude='" + longitude + '\'' +
                ", parkbelongplaceid='" + parkbelongplaceid + '\'' +
                ", parkid='" + parkid + '\'' +
                ", placeaddr='" + placeaddr + '\'' +
                ", placecode='" + placecode + '\'' +
                ", placetype='" + placetype + '\'' +
                ", state='" + state + '\'' +
                ", subname='" + subname + '\'' +
                '}';
    }

}
