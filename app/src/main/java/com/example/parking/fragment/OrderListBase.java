package com.example.parking.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.parking.R;
import com.example.parking.Static_bean;
import com.example.parking.bean.http.EscapeListToMinePageBean;
import com.example.parking.bean.http.OrderAddBean;
import com.example.parking.bean.http.OrderlistBean;
import com.example.parking.bean.http.Report_orderlistBean;
import com.example.parking.http.HttpManager2;
import com.example.parking.util.JsonUtil2;
import com.example.parking.util.StringUtil;
import com.example.parking.util.TimeUtil;
import com.example.parking.view.KeyboardViewPager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 功能说明: 全部订单Fragment
 * 日期:	2019年7月24日
 * 开发者:	KXD
 */
public class OrderListBase extends BaseFragment implements AbsListView.OnScrollListener, AdapterView.OnItemClickListener{


    public static final String TAG = "OrderListBase<全部订单>";

    public static String record = "";//页面暂时内容、、taodannum/taodantotal逃单，为空是7天全部订单

    protected PageAdapter adapter;
    protected ListView mListView;
    private View mLoadMoreView;
    protected EditText order_all_textview_cph;//车牌号
    protected RelativeLayout order_all_textview_rucangtime_kaisi,order_all_textview_rucangtime_jiesu;
    protected TextView order_all_textview_rucangtime_kaisi_lian,order_all_textview_rucangtime_kaisi_yue,order_all_textview_rucangtime_kaisi_ri;
    protected TextView order_all_textview_rucangtime_jiesu_lian,order_all_textview_rucangtime_jiesu_yue,order_all_textview_rucangtime_jiesu_ri;
    private Handler handler = new Handler();
    private int visibleLastIndex = 0;   //最后的可视项索引
    private int visibleLastIndex_bianhua = 0; //每次新增变化的值
    public KeyboardViewPager kvp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View rootView = inflater.inflate(R.layout.listview_order_all_fragment, container, false);
            mLoadMoreView = getLayoutInflater().inflate(R.layout.loadmore, null);
            mListView =  rootView.findViewById(R.id.menulist);
            mListView.addFooterView(mLoadMoreView);


        String dataOld = TimeUtil.getDate(TimeUtil.dateAddDay(-7));


        switch ( record = getArguments().getString("record")==null ? "" : getArguments().getString("record") ){

            case "ordernum1": dataOld = TimeUtil.getDate(TimeUtil.dateAddDay(0));break;
            case "ordertotal1": dataOld = TimeUtil.getDate(TimeUtil.dateAddDay(0));break;
            case "taodannum": dataOld = TimeUtil.getDate(TimeUtil.dateAddDay(0));break;
            case "taodantotal": dataOld = TimeUtil.getDate(TimeUtil.dateAddDay(0));break;
            case "ordernum30": dataOld = TimeUtil.getDate(TimeUtil.dateAddDay(-30));break;
            case "ordertotal30": dataOld = TimeUtil.getDate(TimeUtil.dateAddDay(-30));break;
            default:break;
        }


            order_all_textview_rucangtime_kaisi = rootView.findViewById(R.id.order_all_textview_rucangtime_kaisi);
            order_all_textview_rucangtime_kaisi.setOnClickListener(this);
            order_all_textview_rucangtime_kaisi_lian = rootView.findViewById(R.id.order_all_textview_rucangtime_kaisi_lian);
            order_all_textview_rucangtime_kaisi_lian.setText(dataOld.substring(0,4));
            order_all_textview_rucangtime_kaisi_yue = rootView.findViewById(R.id.order_all_textview_rucangtime_kaisi_yue);
            order_all_textview_rucangtime_kaisi_yue.setText(dataOld.substring(5,7));
            order_all_textview_rucangtime_kaisi_ri = rootView.findViewById(R.id.order_all_textview_rucangtime_kaisi_ri);
            order_all_textview_rucangtime_kaisi_ri.setText(dataOld.substring(8,10));

        String dataNow = TimeUtil.getDate();
            order_all_textview_rucangtime_jiesu = rootView.findViewById(R.id.order_all_textview_rucangtime_jiesu);
            order_all_textview_rucangtime_jiesu.setOnClickListener(this);
            order_all_textview_rucangtime_jiesu_lian = rootView.findViewById(R.id.order_all_textview_rucangtime_jiesu_lian);
            order_all_textview_rucangtime_jiesu_lian.setText(dataNow.substring(0,4));
            order_all_textview_rucangtime_jiesu_yue = rootView.findViewById(R.id.order_all_textview_rucangtime_jiesu_yue);
            order_all_textview_rucangtime_jiesu_yue.setText(dataNow.substring(5,7));
            order_all_textview_rucangtime_jiesu_ri = rootView.findViewById(R.id.order_all_textview_rucangtime_jiesu_ri);
            order_all_textview_rucangtime_jiesu_ri.setText(dataNow.substring(8,10));

            mLoadMoreView.setVisibility(View.GONE);
            //下拉滚动触发事件
            mListView.setOnScrollListener(this);
            mListView.setOnItemClickListener(this);

        return rootView;
    }

    @Override
    public void onStart() { super.onStart();super.onPosition(TAG); }

    protected void pointOrderReport_orderlist(String sign){

       Map<String,String> param = new HashMap<String,String>(12);
       param.put("page",String.valueOf(visibleLastIndex/10+1));
       param.put("size","10");
       param.put("token",activity.userBean.getToken());
       param.put("startdate",order_all_textview_rucangtime_kaisi_lian.getText()+"-"
               +order_all_textview_rucangtime_kaisi_yue.getText()+"-"+order_all_textview_rucangtime_kaisi_ri.getText());
       param.put("enddate",order_all_textview_rucangtime_jiesu_lian.getText()+"-"
               +order_all_textview_rucangtime_jiesu_yue.getText()+"-"+order_all_textview_rucangtime_jiesu_ri.getText());

       param.put("pStartTime",order_all_textview_rucangtime_kaisi_lian.getText()+"-"
               +order_all_textview_rucangtime_kaisi_yue.getText()+"-"+order_all_textview_rucangtime_kaisi_ri.getText());
       param.put("pEndTime",order_all_textview_rucangtime_jiesu_lian.getText()+"-"
               +order_all_textview_rucangtime_jiesu_yue.getText()+"-"+order_all_textview_rucangtime_jiesu_ri.getText());

      if ( !order_all_textview_cph.getText().toString().contains("车牌号") && !order_all_textview_cph.getText().toString().equals("")){
          param.put("carnum",order_all_textview_cph.getText().toString());
      }
        //如果是taodantotal或taodannum就是逃单的
       HttpManager2.requestPost("taodannum".equals(record) || "taodantotal".equals(record) ?
               Static_bean.escapeListToMinePage():  Static_bean.pointOrderReport_orderlistToAll(),
               param, this, sign);
   }

    //TODO 接收数据返回
    protected List<Report_orderlistBean.Report_orderlistList> getArticleList(String object ) {

        Report_orderlistBean report_orderlistBean = JsonUtil2.fromJson(object, Report_orderlistBean.class);
        Log.i(TAG,"解析http返回的json数据="+report_orderlistBean.toString());

        if (report_orderlistBean.getCode()==200 && report_orderlistBean.getData()!=null ){

            return report_orderlistBean.getData().getList();
        }

        toast_makeText(report_orderlistBean.getMessage());
        return null;
    }

    protected List<EscapeListToMinePageBean.EscapeListToMinePageBeanList> getArticleList2(String object ) {

        EscapeListToMinePageBean escapeListToMinePageBean = JsonUtil2.fromJson(object, EscapeListToMinePageBean.class);

        Log.i(TAG,"解析http返回的json数据="+escapeListToMinePageBean.toString());

        if (escapeListToMinePageBean==null || escapeListToMinePageBean.getData() == null ){
            return null;
        }

        if (escapeListToMinePageBean.getCode()==200 && escapeListToMinePageBean.getData()!=null ){

            return escapeListToMinePageBean.getData().getList();
        }

        toast_makeText(escapeListToMinePageBean.getData().getList().toString());
        return null;
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        if (visibleLastIndex == totalItemCount){ return; }
       Log.i(TAG,"totalItemCount======"+totalItemCount +";visibleLastIndex="+visibleLastIndex);

        visibleLastIndex_bianhua = totalItemCount - visibleLastIndex;

        visibleLastIndex = totalItemCount;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

        //不滚动
        if(scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE){
            //滚动最底部
            if(view.getLastVisiblePosition() == view.getCount() -1){
                mLoadMoreView.setVisibility( View.VISIBLE );
                handler.postDelayed(new MyRunnable(),100);
            }
        }
    }

    //TODO 公共点击事件
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {
//        ListView listView = (ListView) parent;
//
//
//        Object article =  listView.getItemAtPosition(position);
//        if (article==null)return;
//
//        Log.i("点击事件",article.toString());
//
//
//        if ("taodannum".equals(record) || "taodantotal".equals(record)){
//
//
//            EscapeListToMinePageBean.EscapeListToMinePageBeanList escapeList = (EscapeListToMinePageBean.EscapeListToMinePageBeanList)article;
//            OrderAddBean.OrderAddDate orderAddDate = new OrderAddBean.OrderAddDate();
//
//            orderAddDate.setParkdate(escapeList.ParkDate);
//            orderAddDate.setEscapeprice(String.valueOf(escapeList.EscapePrice));
//            orderAddDate.setCarnum(escapeList.CarNum);
//            orderAddDate.setParkDateStr(!StringUtil.is_valid(escapeList.CreateTime)?"":escapeList.CreateTime.substring(0,10)+" "+escapeList.CreateTime.substring(11,19));
//            orderAddDate.setOrderid(escapeList.OrderId);
//            orderAddDate.setId(escapeList.id);
//
//            if (escapeList.isdeleted==2){
//                orderAddDate.sfjiaofei = true;
//            }else{
//                orderAddDate.sfjiaofei = false;
//            }
//            //查看逃单详情
//            activity.openOrderPayBack(orderAddDate);
//        }else{
//            //查看普通订单详情
//            activity.openOrder_list_details((Report_orderlistBean.Report_orderlistList)article);
//        }
    }

    @Override
    public void onResponsePOST(String url, Map<String, String> param, final String sign, final String object) {
        super.onResponsePOST(url, param, sign, object);
        Log.i(TAG,"url="+url+";param="+param+";sign="+sign+";\r\n /r/n object="+object);

        switch (sign) {

            case "onStart":

                if ("taodannum".equals(record) || "taodantotal".equals(record)){

                    List<EscapeListToMinePageBean.EscapeListToMinePageBeanList> itemList = getArticleList2(object);
                    if ( itemList==null ){return;}
                    adapter = new PageAdapter(null, itemList);
                }else{

                    List<Report_orderlistBean.Report_orderlistList> itemList = getArticleList(object);
                    if ( itemList==null ){return;}
                    adapter = new PageAdapter(itemList, null);
                }
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() { mListView.setAdapter(adapter); }});
                break;

            case "onAdd":

                if ("taodannum".equals(record) || "taodantotal".equals(record)){

                    List<EscapeListToMinePageBean.EscapeListToMinePageBeanList> itemList = getArticleList2(object);


//                    for (EscapeListToMinePageBean.EscapeListToMinePageBeanList article: itemList) {
//                        adapter.itemList2.add(article);
//                    }
                        for (int i=0; itemList !=null && i< itemList.size();i++){
                            adapter.itemList2.add(itemList.get(i));
                        }

                }else{

                    List<Report_orderlistBean.Report_orderlistList> itemList = getArticleList(object);

                    for (int i=0;itemList!=null && i<itemList.size();i++){
                        adapter.itemList.add(itemList.get(i));
                    }

//                    for (Report_orderlistBean.Report_orderlistList article : itemList) {
//                        adapter.itemList.add(article);
//                    }
                }
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mLoadMoreView.setVisibility( View.GONE );
                        adapter.notifyDataSetChanged(); }});
                break;

            default:
                break;
        }
    }

    class MyRunnable implements Runnable{
        @Override
        public void run() {
            Log.e("visibleLastIndex==", visibleLastIndex+"=visibleLastIndex_bianhua="+visibleLastIndex_bianhua);
            if (visibleLastIndex>10 && visibleLastIndex_bianhua>=10){
               pointOrderReport_orderlist("onAdd");
           }else if (visibleLastIndex == 11){
                pointOrderReport_orderlist("onAdd");
            }else{


              mLoadMoreView.setVisibility( View.GONE );
                // pointOrderReport_orderlist("onStart");
           }
        }
    }

    class PageAdapter extends BaseAdapter {

        List<Report_orderlistBean.Report_orderlistList> itemList; //普通订单

        List<EscapeListToMinePageBean.EscapeListToMinePageBeanList> itemList2; //逃费订单

        public PageAdapter(List<Report_orderlistBean.Report_orderlistList> itemList,List<EscapeListToMinePageBean.EscapeListToMinePageBeanList> itemList2) {
            this.itemList = itemList;
            this.itemList2 = itemList2;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if(convertView == null){ convertView = LayoutInflater.from(activity).inflate(R.layout.listview_order_all_item, null); }

            //TODO 如果是只查看逃单
            if ("taodannum".equals(record) || "taodantotal".equals(record)){

                ((TextView) convertView.findViewById(R.id.all_cph)).setText("车牌 : "+itemList2.get(position).CarNum);
                ((TextView) convertView.findViewById(R.id.all_jine)).setText("金额 : "+itemList2.get(position).EscapePrice+"元");
                ((TextView) convertView.findViewById(R.id.all_in)).setText("入场时间 : "+itemList2.get(position).ParkDate.substring(5,16));
                ((TextView) convertView.findViewById(R.id.all_out)).setText("逃单时间 : "+itemList2.get(position).CreateTime.substring(5,16));

                // <!-- 已补费orange  逃费colorAccent  已缴费idle_green  已出场idle_green-->
                TextView tips_down = ((TextView) convertView.findViewById(R.id.order_all_textview_tips_down));
                tips_down.setText(" 逃    单 ");

                tips_down.setBackgroundResource(R.color.colorAccent);
                tips_down.setPadding(tips_down.getPaddingLeft(),
                        tips_down.getPaddingTop(),
                        tips_down.getPaddingRight(),
                        tips_down.getPaddingBottom());

                if (itemList2.get(position).isdeleted==2){
                    TextView tips_up = ((TextView) convertView.findViewById(R.id.order_all_textview_tips_up));
                    tips_up.setText(" 已补缴 ");
                    tips_up.setBackgroundResource(R.color.orange);
                    tips_up.setPadding(tips_up.getPaddingLeft(),
                            tips_up.getPaddingTop(),
                            tips_up.getPaddingRight(),
                            tips_up.getPaddingBottom());
                }else{
                    TextView tips_up = ((TextView) convertView.findViewById(R.id.order_all_textview_tips_up));
                    tips_up.setVisibility( View.INVISIBLE );
                }

                RelativeLayout relative = convertView.findViewById(R.id.listview_order_all_item_relativeLayoutmax);
                relative.setTag(String.valueOf(position));
                relative.setOnClickListener(new android.view.View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        EscapeListToMinePageBean.EscapeListToMinePageBeanList escapeList =  itemList2.get(Integer.valueOf((String) v.getTag()));
                        OrderAddBean.OrderAddDate orderAddDate = new OrderAddBean.OrderAddDate();
                        orderAddDate.setParkdate(escapeList.ParkDate);
                        orderAddDate.setEscapeprice(String.valueOf(escapeList.EscapePrice));
                        orderAddDate.setCarnum(escapeList.CarNum);
                        orderAddDate.setParkDateStr(!StringUtil.is_valid(escapeList.CreateTime) ? "" : escapeList.CreateTime.substring(0, 10) + " " + escapeList.CreateTime.substring(11, 19));
                        orderAddDate.setOrderid(escapeList.OrderId);
                        orderAddDate.setId(escapeList.id);
                        orderAddDate.sfjiaofei = escapeList.isdeleted == 2 ? true : false;

                        //查看逃单详情
                        activity.openOrderPayBack(orderAddDate);
                    }
                });
                return convertView;
            }

            ((TextView) convertView.findViewById(R.id.all_cph)).setText("车牌 : "+itemList.get(position).CarNum);
            ((TextView) convertView.findViewById(R.id.all_in)).setText("入场时间 : "+itemList.get(position).ParkDate.substring(5,16));



            //TODO 已结算订单
            if("2".equals(itemList.get(position).State)){

                ((TextView) convertView.findViewById(R.id.all_out)).setText("出场时间 : "+ itemList.get(position).LeaveDate.substring(5,16));

                ((TextView) convertView.findViewById(R.id.all_jine)).setText("金额 : "+itemList.get(position).OrderPrice+"元");

//                TextView tips_up = ((TextView) convertView.findViewById(R.id.order_all_textview_tips_up));
//                tips_up.setText(" 已结算 ");
//                tips_up.setBackgroundResource(R.color.idle_green);
//                tips_up.setPadding(tips_up.getPaddingLeft(),
//                        tips_up.getPaddingTop(),
//                        tips_up.getPaddingRight(),
//                        tips_up.getPaddingBottom());


                TextView tips_down = ((TextView) convertView.findViewById(R.id.order_all_textview_tips_down));
                tips_down.setText(" 已出场 ");
                tips_down.setBackgroundResource(R.color.idle_green);
                tips_down.setPadding(tips_down.getPaddingLeft(),
                        tips_down.getPaddingTop(),
                        tips_down.getPaddingRight(),
                        tips_down.getPaddingBottom());



                RelativeLayout relative = convertView.findViewById(R.id.listview_order_all_item_relativeLayoutmax);
                relative.setTag(String.valueOf(position));
                relative.setOnClickListener(new android.view.View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        int position = Integer.valueOf((String) v.getTag());
                        Report_orderlistBean.Report_orderlistList article = itemList.get(position);

                        article.ParkDate = article.ParkDate.substring(0,10)+" "+article.ParkDate.substring(11,19);
                        article.LeaveDate = article.LeaveDate.substring(0,10)+" "+article.LeaveDate.substring(11,19);
                        //打开查看订单页面
                        activity.openOrder_list_details( article );
                    }
                });

                return convertView;
            }else if ("1".equals(itemList.get(position).State)){

                ((TextView) convertView.findViewById(R.id.all_jine)).setText("");
                ((TextView) convertView.findViewById(R.id.all_out)).setText("出场时间 :未出场");

                TextView tips_down = ((TextView) convertView.findViewById(R.id.order_all_textview_tips_down));
                tips_down.setText(" 未出场 ");
                tips_down.setBackgroundResource(R.color.E65737);
                tips_down.setPadding(tips_down.getPaddingLeft(),
                        tips_down.getPaddingTop(),
                        tips_down.getPaddingRight(),
                        tips_down.getPaddingBottom());

                RelativeLayout relative = convertView.findViewById(R.id.listview_order_all_item_relativeLayoutmax);
                relative.setTag(String.valueOf(position));
                relative.setOnClickListener(new android.view.View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        int position = Integer.valueOf((String) v.getTag());

                        Report_orderlistBean.Report_orderlistList report_order =  itemList.get(position);

                        OrderlistBean.OrderlistData orderAddDate = new  OrderlistBean.OrderlistData();
                            orderAddDate.setSubid(report_order.SubId);
                            orderAddDate.setId(report_order.id);
                            orderAddDate.setSubname(report_order.SubName);
                            orderAddDate.setCarNo(report_order.CarNum);

                        //打开收费页面
                        activity.openOrder_details(orderAddDate);
                    }
                });

                return convertView;

            }else if ( "3".equals(itemList.get(position).State) || "4".equals(itemList.get(position).State )){

                ((TextView) convertView.findViewById(R.id.all_jine)).setText("金额 : "+itemList.get(position).PrePrice+"元");
                ((TextView) convertView.findViewById(R.id.all_out)).setText("逃单时间 : "+ itemList.get(position).LeaveDate.substring(5,16));

                // <!-- 已补费orange  逃费colorAccent  已缴费idle_green  已出场idle_green-->
                TextView tips_down = ((TextView) convertView.findViewById(R.id.order_all_textview_tips_down));
                tips_down.setText(" 逃    单 ");

                tips_down.setBackgroundResource(R.color.occupy_red);
                tips_down.setPadding(tips_down.getPaddingLeft(),
                        tips_down.getPaddingTop(),
                        tips_down.getPaddingRight(),
                        tips_down.getPaddingBottom());

                if ("4".equals(itemList.get(position).State )){
                    TextView tips_up = ((TextView) convertView.findViewById(R.id.order_all_textview_tips_up));
                    tips_up.setText(" 已补缴 ");
                    tips_up.setBackgroundResource(R.color.orange);
                    tips_up.setPadding(tips_up.getPaddingLeft(),
                            tips_up.getPaddingTop(),
                            tips_up.getPaddingRight(),
                            tips_up.getPaddingBottom());
                }else{
                    TextView tips_up = ((TextView) convertView.findViewById(R.id.order_all_textview_tips_up));
                    tips_up.setVisibility( View.INVISIBLE );
                }

                RelativeLayout relative = convertView.findViewById(R.id.listview_order_all_item_relativeLayoutmax);
                relative.setTag(String.valueOf(position));
                relative.setOnClickListener(new android.view.View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        int position = Integer.valueOf((String) v.getTag());

                        Report_orderlistBean.Report_orderlistList report_order =  itemList.get(position);
                        OrderAddBean.OrderAddDate orderAddDate = new OrderAddBean.OrderAddDate();
                        orderAddDate.setParkdate(report_order.ParkDate);
                        orderAddDate.setEscapeprice(String.valueOf(report_order.PrePrice));
                        orderAddDate.setCarnum(report_order.CarNum);
                        orderAddDate.setParkDateStr(report_order.LeaveDate);
                        orderAddDate.setOrderid(Integer.valueOf(report_order.id));
                //TODO 加一个逃单ID       // orderAddDate.setId(escapeList.id);
                        orderAddDate.sfjiaofei = "4".equals(itemList.get(position).State ) ? true : false;

                        //查看逃单详情
                        activity.openOrderPayBack(orderAddDate);
                    }
                });
                return convertView;
            }


            return convertView;
        }

        @Override
        public long getItemId(int position) { return position; }

        @Override
        public Object getItem(int position) {
            return "taodannum".equals(record) || "taodantotal".equals(record)
                    ? itemList2.get(position):itemList.get(position);
        }

        @Override
        public int getCount() {
            return "taodannum".equals(record) || "taodantotal".equals(record)
                    ? itemList2.size():itemList.size();
        }

    }

}