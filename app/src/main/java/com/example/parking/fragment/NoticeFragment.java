package com.example.parking.fragment;



import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.parking.R;
import com.example.parking.Static_bean;
import com.example.parking.bean.JiguangBean;
import com.example.parking.bean.http.OrderlistBean;
import com.example.parking.bean.http.SelectSubPlaceBean;
import com.example.parking.db.Jiguang_DB;
import com.example.parking.http.HttpManager2;
import com.example.parking.util.JsonUtil2;
import com.example.parking.util.StringUtil;
import java.util.List;
import java.util.Map;


/**
 * 功能说明: 通知Fragment
 * 日期:	2019年5月29日
 * 开发者:	KXD
 */
public class NoticeFragment extends BaseFragment {

    public static final String TAG = "NoticeFragment<通知>";

    private ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.notice_fragment, container, false);

        listView = rootView.findViewById(R.id.notice_listview);

        initView( );
        return rootView;
    }

    // 初始化
    public void initView(){
        Log.i(TAG,"加载消息列表");
        List<JiguangBean> list = Jiguang_DB.query_Jiguang_Yesterday(activity.baseSQL_DB);

        listView.setAdapter(new PageAdapter(list,this));
    }


    @Override
    public void onStart() {
        super.onStart();
        super.onPosition(TAG);
    }



    @Override
    public void onResponsePOST(String url, Map<String, String> param, String sign, String object) {
        super.onResponsePOST(url, param, sign, object);


        switch (sign){

            case "checktoken":break;

            case "selectSubPlace":       //TODO 打开收费页面

                try {

                    if (!StringUtil.is_valid(object)){ return; }

                    SelectSubPlaceBean selectSubPlaceBean = JsonUtil2.fromJson(object,SelectSubPlaceBean.class);

                    if (selectSubPlaceBean.getCode() == 200){

                        if ("out".equals(param.get("type"))){


                            SelectSubPlaceBean.SelectSubPlaceData selectSubPlaceData = selectSubPlaceBean.getData().get(0);

                            if ( !StringUtil.is_valid(selectSubPlaceData.getCarnum()) ){
                                toast_makeText("任务已完成，请勿重复操作");
                                Jiguang_DB.updata_Jiguang(activity.baseSQL_DB,param.get("nOTIFICATION_ID"),null);
                                return;
                            }

                            super.AlertDialog_Builder("提示", "正在加载数据...", "确定");
                            activity.openOrder_details( new OrderlistBean.OrderlistData( selectSubPlaceData ) );
                        }else if ("in".equals(param.get("type"))){

                            SelectSubPlaceBean.SelectSubPlaceData selectSubPlaceData = new SelectSubPlaceBean.SelectSubPlaceData();
                                selectSubPlaceData.setId(selectSubPlaceBean.getData().get(0).getId());
                                selectSubPlaceData.setCode(selectSubPlaceBean.getData().get(0).getCode());

                            if ( StringUtil.is_valid(selectSubPlaceData.getCarnum()) ){
                                toast_makeText("任务已完成，请勿重复操作");
                                Jiguang_DB.updata_Jiguang(activity.baseSQL_DB,param.get("nOTIFICATION_ID"),null);
                                return;
                            }

                            super.AlertDialog_Builder("提示", "正在加载数据...", "确定");
                            activity.openParking( selectSubPlaceData );
                        }
                    }else{

                        super.AlertDialog_Builder("错误", selectSubPlaceBean.getMessage(), "确定");
                    }

                } catch (Exception e) {
                    Log.w(TAG, e);
                }
                break;

        }




    }

    class PageAdapter extends BaseAdapter {

        private List<JiguangBean> itemList;
        private NoticeFragment noticeFragment;

        public PageAdapter(List<JiguangBean> itemList, NoticeFragment noticeFragment) {
            this.itemList = itemList;
            this.noticeFragment=noticeFragment;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if(convertView == null){
                convertView = LayoutInflater.from(activity).inflate(R.layout.listview_notice_item, null);
            }

            RelativeLayout notice_relative = convertView.findViewById(R.id.listview_notice_item_relativelayout);
            TextView notice_devdockname = convertView.findViewById(R.id.notice_devdockname);
            TextView notice_imageview = convertView.findViewById(R.id.notice_imageview);
            TextView notice_pushtime = convertView.findViewById(R.id.notice_pushtime);
            TextView notice_tips = convertView.findViewById(R.id.notice_tips);

            //如果已经处理，就不显示
            if (itemList.get(position).getState()==1){
                notice_tips.setVisibility( View.GONE);
            }


            if ("move".equals(itemList.get(position).getMsgType())){

                if ("in".equals(itemList.get(position).getInOut())){
                    notice_imageview.setText("进");
                    notice_imageview.setTextColor(getContext().getResources().getColorStateList(R.color.E65737));
                    notice_pushtime.setText("进场时间:"+itemList.get(position).getPushTime().substring(5,16));
                    notice_devdockname.setText("所属车位:"+itemList.get(position).getDevDockName());

                    String tagJson = "{\"nOTIFICATION_ID\":\""+itemList.get(position).getnOTIFICATION_ID()
                            +"\",\"subId\":\""+itemList.get(position).getDevDock() +"\",\"type\":\"in\"}";
                    notice_relative.setTag( tagJson );
                }else{
                    notice_imageview.setText("出");
                    notice_imageview.setTextColor(getContext().getResources().getColorStateList(R.color.idle_green));
                    notice_pushtime.setText("出场时间:"+itemList.get(position).getPushTime().substring(5,16));
                    notice_devdockname.setText("所属车位:"+itemList.get(position).getDevDockName());

                    String tagJson = "{\"nOTIFICATION_ID\":\""+itemList.get(position).getnOTIFICATION_ID()
                            +"\",\"subId\":\""+itemList.get(position).getDevDock() +"\",\"type\":\"out\"}";
                    notice_relative.setTag( tagJson );
                }
            }else{
                notice_imageview.setText("事");
                notice_imageview.setTextColor(getContext().getResources().getColorStateList(R.color.bg_blue));
                notice_pushtime.setText("发布时间:"+itemList.get(position).getPushTime().substring(5,16));
                notice_devdockname.setText("所属车位:"+itemList.get(position).getDevDockName());

                String tagJson = "{\"nOTIFICATION_ID\":\""+itemList.get(position).getnOTIFICATION_ID()
                        +"\",\"position\":\""+position
                        +"\",\"subId\":\""+itemList.get(position).getDevDock() +"\",\"type\":\"other\"}";
                notice_relative.setTag( tagJson );
            }


            notice_relative.setOnClickListener(new android.view.View.OnClickListener() {
                @Override
                public void onClick(View v) {

                  Map<String,String> param = JsonUtil2.fromJson(v.getTag().toString(),Map.class) ;

                    if ("other".equals(param.get("type"))){

                            activity.openAlertFeagment(itemList.get(Integer.valueOf(param.get("position"))));
                    }else{

                        param.put("token",activity.userBean.getToken());

                        HttpManager2.requestPost(Static_bean.selectSubPlace(),  param, noticeFragment, "selectSubPlace");
                    }

                }
            });

            return convertView;
        }

        @Override
        public long getItemId(int position) { return position; }

        @Override
        public Object getItem(int position) { return itemList==null?null:itemList.get(position); }

        @Override
        public int getCount() { return itemList==null?0:itemList.size(); }

    }

}