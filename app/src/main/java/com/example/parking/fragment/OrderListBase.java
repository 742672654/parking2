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
import com.example.parking.bean.http.Report_orderlistBean;
import com.example.parking.http.HttpManager2;
import com.example.parking.util.JsonUtil2;
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

    protected PageAdapter adapter;
    protected ListView mListView;
    private View mLoadMoreView;
    protected EditText order_all_textview_cph;//车牌号
    protected RelativeLayout order_all_textview_rucangtime_kaisi,order_all_textview_rucangtime_jiesu;
    protected TextView order_all_textview_rucangtime_kaisi_lian,order_all_textview_rucangtime_kaisi_yue,order_all_textview_rucangtime_kaisi_ri;
    protected TextView order_all_textview_rucangtime_jiesu_lian,order_all_textview_rucangtime_jiesu_yue,order_all_textview_rucangtime_jiesu_ri;
    private Handler handler = new Handler();
    private int visibleLastIndex = 0;   //最后的可视项索引
    public KeyboardViewPager kvp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View rootView = inflater.inflate(R.layout.listview_order_all_fragment, container, false);
            mLoadMoreView = getLayoutInflater().inflate(R.layout.loadmore, null);
            mListView =  rootView.findViewById(R.id.menulist);
            mListView.addFooterView(mLoadMoreView);

        String dataOld = TimeUtil.getDate(TimeUtil.dateAddDay(-7));
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
            order_all_textview_rucangtime_jiesu_lian.setText(dataOld.substring(0,4));
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
    public void onStart() {
        super.onStart();
        super.onPosition(TAG);
    }

   protected void pointOrderReport_orderlist(String sign){

       Map<String,String> param = new HashMap<String,String>(6);
       param.put("page",String.valueOf(visibleLastIndex/10+1));
       param.put("size","10");
       param.put("token",activity.userBean.getToken());
       param.put("startdate",order_all_textview_rucangtime_kaisi_lian.getText()+"-"
               +order_all_textview_rucangtime_kaisi_yue.getText()+"-"+order_all_textview_rucangtime_kaisi_ri.getText());
       param.put("enddate",order_all_textview_rucangtime_jiesu_lian.getText()+"-"
               +order_all_textview_rucangtime_jiesu_yue.getText()+"-"+order_all_textview_rucangtime_jiesu_ri.getText());

      if ( !order_all_textview_cph.getText().toString().contains("车牌号") && !order_all_textview_cph.getText().toString().equals("")){
          param.put("carnum",order_all_textview_cph.getText().toString());
      }
       HttpManager2.requestPost(Static_bean.pointOrderReport_orderlist(),  param, this, sign);
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

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        visibleLastIndex = totalItemCount;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

        //不滚动
        if(scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE){
            //滚动最底部
            if(view.getLastVisiblePosition() == view.getCount() -1){
                mLoadMoreView.setVisibility( View.VISIBLE );
                handler.postDelayed(new MyRunnable(),2000);
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {
        ListView listView = (ListView) parent;
        Report_orderlistBean.Report_orderlistList article = (Report_orderlistBean.Report_orderlistList) listView.getItemAtPosition(position);

        if (article==null)return;


        Log.i("点击事件",article.toString());

        activity.openOrder_list_details(article);
    }

    @Override
    public void onResponsePOST(String url, Map<String, String> param, final String sign, final String object) {
        super.onResponsePOST(url, param, sign, object);
        Log.i(TAG,"url="+url+";param="+param+";sign="+sign+";\r\n /r/n object="+object);

        switch (sign) {

            case "onStart":
                List<Report_orderlistBean.Report_orderlistList> itemList = getArticleList(object);
                if ( itemList==null ){return;}
                adapter = new PageAdapter(itemList);
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mListView.setAdapter(adapter);
                    }});
                break;

            case "onAdd":
                List<Report_orderlistBean.Report_orderlistList> articleList = getArticleList(object);
                if ( articleList==null ){return;}

                for (Report_orderlistBean.Report_orderlistList article : articleList) {
                    adapter.itemList.add(article);
                }
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //更新UI
                        adapter.notifyDataSetChanged();
                    }});
                break;

            default:
                break;

        }



    }


    class MyRunnable implements Runnable{
        @Override
        public void run() {
            Log.e("visibleLastIndex", visibleLastIndex+"");
            if (visibleLastIndex>10){
               pointOrderReport_orderlist("onAdd");
            }else{
                mLoadMoreView.setVisibility( View.GONE );
            }
        }
    }

    class PageAdapter extends BaseAdapter {

        List<Report_orderlistBean.Report_orderlistList> itemList;

        public PageAdapter(List<Report_orderlistBean.Report_orderlistList> itemList){ this.itemList = itemList; }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if(convertView == null){
                convertView = LayoutInflater.from(activity).inflate(R.layout.listview_order_all_item, null);
            }

            ((TextView) convertView.findViewById(R.id.all_cph)).setText("车牌 : "+itemList.get(position).CarNum);
            ((TextView) convertView.findViewById(R.id.all_jine)).setText("金额 : "+itemList.get(position).OrderPrice+"元");
            ((TextView) convertView.findViewById(R.id.all_in)).setText("入场时间 : "+itemList.get(position).ParkDate.substring(5,16));
            ((TextView) convertView.findViewById(R.id.all_out)).setText("出场时间 : "+itemList.get(position).LeaveDate.substring(5,16));
            return convertView;
        }

        @Override
        public long getItemId(int position) { return position; }

        @Override
        public Object getItem(int position) { return itemList.get(position); }

        @Override
        public int getCount() { return itemList.size(); }

    }

}