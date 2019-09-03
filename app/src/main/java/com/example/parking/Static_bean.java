package com.example.parking;

public class Static_bean {

    public final static String printBillType = "android"; //打印机版本：android 或 PDA

    public final static String ChargingTime = "收费时间：7：30 - 21：30\r\n\r\n";
    public final static String ChargingStandard = "收费标准：单次收费5元，车辆离开车位视为订单结算\r\n";
    public final static String ChargingUnit = "收费单位：泉州市畅顺停车管理有限公司\r\n";
    public final static String ComplaintTelephone = "监督电话：0595-28282818";




    public final static String url = "https://parking.yilufa.net:18443";
 //public final static String url = "http://192.168.0.104:18442";

    /**
     * TODO 登录，返回个人信息
     * @param phone 账号
     * @param password 密码
     * @param edition 版本号
     */
    public final static String getlogin(){ return url+"/park/usersub/login";}

    /**
     * TODO 车牌照片上传，返回车牌
     * @param token
     * @param file 文件
     * @param type 1是需要识别车牌，2是不需要
     */
    public final static String photoToOss(){ return url+"/park/upload/photoToOss";}

    /**
     * TODO 查询全部车位
     * @param token
     * @param subId 车位id
     */
    public final static String selectSubPlace(){ return url+"/point/order/parkListToFirstPage";}

    /**
     * TODO 查询可以车位
     * @param token
     * @param id 订单id
     */
    public final static String selectOrderSubPlace(){ return url+"/park/parkplacesub/selectOrderSubPlace";}

    /**
     * TODO 订单保存
     * @param token
     */
    public final static String orderAdd(){ return url+"/point/order/add";}

    /**
     * TODO 订单查询接口
     * @param token
     */
    public final static String orderlist(){ return url+"/point/order/orderlist";}

    /**
     * TODO 订单详情
     * @param token
     * @param id 订单id
     * @param camum 车牌号
     */
    public final static String getLeavePageOrder(){ return url+"/point/order/getLeavePageOrder";}

    /**
     * TODO 订单收费
     * @param token
     * @param id Integer 订单id
     * @param orderprice Double订单总价
     * @param subid Stirng 车位id
     * @param subname Stirng 车位名称
     * @param outimage String 出场识别照片URL
     */
    public final static String payPointOrderToPoint(){ return url+"/point/order/payPointOrderToPoint";}

    /**
     * TODO 逃费补缴
     * @param token
     * @param id Integer 逃单id
     */
    public final static String escapedelete(){ return url+"/point/escape/delete";}

    /**
     * TODO 逃费处理
     * @param token
     * @param id Integer 订单id
     * @param carnum 车牌号
     * @param subid Stirng 车位id
     * @param subname Stirng 车位名称
     */
    public final static String escape_add(){ return url+"/point/escape/add";}

    /**
     * TODO 一键出场
     * @param token
     */
    public final static String allCharge(){ return url+"/point/order/allCharge";}



    /**
     * TODO 修改密码
     * @param token
     * @param chkpwd
     */
    public final static String changePwds(){ return url+"/park/usersub/changePwds";}

    /**
     * TODO 剩余车位和预约车位数量
     * @param token
     */
    public final static String firstPageRecord(){ return url+"/point/order/firstPageRecord";}


    /**
     * TODO 检测token是否有效
     * @param token
     */
    public final static String checktoken(){ return url+"/park/usersub/checktoken";}

    /**
     * TODO 获取统计
     * @param token
     * @param pointid 停车场id
     */
    public final static String findOrderByDate(){ return url+"/pointOrderReport/findOrderByDate";}

    /**
     * TODO 收费二维码
     * @param orderid 订单id
     * @param pointid 停车场id
     */
    public final static String QRcode_redict(){return  "http://wx.yilufa.net/localpay/localprepayredir";}

    /**
     * TODO 查看详情二维码
     * @param orderid 订单id
     */
    public final static String QRcode_orderdetail(){ return  "http://wx.yilufa.net/pointinfo/orderdetail";}

    /**
     * TODO 全部订单
     * @param carnum 车牌号
     * @param page
     * @param size
     * @param startdate 开始日期
     * @param enddate 结束日期
     * @param pointid 停车场id
     */
    public final static String pointOrderReport_orderlist(){ return url+"/pointOrderReport/orderlist";}


    /**
     * TODO 全部逃费订单
     * @param carnum 车牌号
     * @param page
     * @param size
     * @param String pStartTime, String pEndTime 创建逃单时间
     * @param enddate 结束日期
     * @param pointid 停车场id
     */
    public final static String escapeListToMinePage(){ return url+"/point/escape/escapeListToMinePage";}

    /**
     * TODO 警告拍照上传
     * @param file 文件
     * @param subId 车位ID
     */
    public final static String photoToAlert(){ return url+"/abnormalSubMonitorReport/park/upload/photoToAlert";}

    /**
     * TODO 获取逃单图片接口
     * @param token
     * @param id 逃费的id
     */
    public final static String pointEscapeEscapePic(){ return url+"/point/escape/escapePic";}


}
