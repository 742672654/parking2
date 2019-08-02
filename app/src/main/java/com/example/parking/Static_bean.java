package com.example.parking;

public class Static_bean {

    /**
     * TODO 登录，返回个人信息
     * @param phone 账号
     * @param password 密码
     * @param edition 版本号
     */
    public final static String login = "https://192.168.0.106:18442/park/usersub/login";

    /**
     * TODO 车牌照片上传，返回车牌
     * @param token
     * @param file 文件
     * @param type 1是需要识别车牌，2是不需要
     */
    public final static String photoToOss = "https://192.168.0.106:18442/park/upload/photoToOss";

    /**
     * TODO 查询全部车位
     * @param token
     */
    public final static String selectSubPlace = "https://192.168.0.106:18442/point/order/parkListToFirstPage";

    /**
     * TODO 查询可以车位
     * @param token
     * @param id 订单id
     */
    public final static String selectOrderSubPlace = "https://192.168.0.106:18442/park/parkplacesub/selectOrderSubPlace";

    /**
     * TODO 订单保存
     * @param token
     */
    public final static String orderAdd = "https://192.168.0.106:18442/point/order/add";

    /**
     * TODO 订单查询接口
     * @param token
     */
    public final static String orderlist = "https://192.168.0.106:18442/point/order/orderlist";

    /**
     * TODO 订单详情
     * @param token
     * @param id 订单id
     * @param camum 车牌号
     */
    public final static String getLeavePageOrder = "https://192.168.0.106:18442/point/order/getLeavePageOrder";

    /**
     * TODO 订单收费
     * @param token
     * @param id Integer 订单id
     * @param orderprice Double订单总价
     * @param subid Stirng 车位id
     * @param subname Stirng 车位名称
     * @param outimage String 出场识别照片URL
     */
    public final static String payPointOrderToPoint = "https://192.168.0.106:18442/point/order/payPointOrderToPoint";

    /**
     * TODO 逃费补缴
     * @param token
     * @param id Integer 逃单id
     */
    public final static String escapedelete = " https://192.168.0.106:18442/point/escape/delete";

    /**
     * TODO 逃费处理
     * @param token
     * @param id Integer 订单id
     * @param carnum 车牌号
     * @param subid Stirng 车位id
     * @param subname Stirng 车位名称
     */
    public final static String escape_add = "https://192.168.0.106:18442/point/escape/add";

    /**
     * TODO 一键出场
     * @param token
     */
    public final static String allCharge = "https://192.168.0.106:18442/point/order/allCharge";

    /**
     * TODO 统计收费
     * @param token
     */
    public final static String getMinePage = "https://192.168.0.106:18442/point/order/getMinePage";


    /**
     * TODO 修改密码
     * @param token
     * @param chkpwd
     */
    public final static String changePwds = "https://192.168.0.106:18442/park/usersub/changePwds";

    /**
     * TODO 剩余车位和预约车位数量
     * @param token
     */
    public final static String firstPageRecord = "https://192.168.0.106:18442/point/order/firstPageRecord";

    /**
     * TODO 刷新tken
     * @param token
     */
    public final static String tokenF5 = "https://192.168.0.106:18442/park/usersube/token";

    /**
     * TODO 检测token是否有效
     * @param token
     */
    public final static String checktoken = "https://192.168.0.106:18442/park/usersub/checktoken";

    /**
     * TODO 获取统计
     * @param token
     * @param pointid 停车场id
     */
    public final static String findOrderByDate = "https://192.168.0.106:18442/pointOrderReport/findOrderByDate";

    /**
     * TODO 收费二维码
     * @param orderid 订单id
     * @param pointid 停车场id
     */
    public final static String QRcode_redict = "http://wx.yilufa.net/pointpay/pointzgr/redict";

    /**
     * TODO 查看详情二维码
     * @param orderid 订单id
     */
    public final static String QRcode_orderdetail = "http://wx.yilufa.net/pointinfo/orderdetail";

    /**
     * TODO 全部订单
     * @param carnum 车牌号
     * @param page
     * @param size
     * @param startdate 开始日期
     * @param enddate 结束日期
     * @param pointid 停车场id
     */
    public final static String pointOrderReport_orderlist = "https://192.168.0.106:18442/pointOrderReport/orderlist";


}
