package com.example.parking.listView;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parking.R;
import com.example.parking.activety.MainActivity;
import com.example.parking.bean.http.OrderlistBean;
import com.example.parking.fragment.OrderBase;

import java.util.List;
import java.util.Map;


public class OrderView extends BaseAdapter {

    List<OrderlistBean.OrderlistData> list;  //数据源与配置器建立连接
    LayoutInflater layoutInflater;//初始化布局填充器
    MainActivity activity;
    OrderBase orderBase;


    public OrderView(MainActivity activity, OrderBase orderBase, List<OrderlistBean.OrderlistData> list) {
        this.layoutInflater = layoutInflater.from(activity);
        this.activity=activity;
        this.list = list;
        this.orderBase=orderBase;
    }

    //itme的数量
    @Override
    public int getCount() {
        return list.size();
    }

    //返回第几条itme信息
    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    //返回itme的ID
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        //第三个参数false：不会为item添加父布局
        View view1 = layoutInflater.inflate(R.layout.listview_order_item,viewGroup,false);

        TextView item_title1 = view1.findViewById(R.id.item_title1);
        TextView item_title2 = view1.findViewById(R.id.item_title2);
        TextView item_title3 = view1.findViewById(R.id.item_title3);
        TextView item_title4 = view1.findViewById(R.id.item_title4);

        OrderlistBean.OrderlistData map = list.get(i);

        item_title1.setText("车牌号码:"+map.getCarNo());
        item_title2.setText("所属车位:"+map.getSubname());
        item_title3.setText("订单编号:"+map.getOrderNo());
        item_title4.setText("入场时间:"+map.getTime().substring(5,map.getTime().length()-3));

         Button btn = view1.findViewById(R.id.item_but1);


        btn.setTag(map.getId());//设置标签
        btn.setOnClickListener(new android.view.View.OnClickListener() {

            @Override
            public void onClick(View v) {

            for (int i = 0; i < list.size(); i++) {

                if (v.getTag().equals(list.get(i).getId())) {

                    activity.openOrder_details(list.get(i));
                    break;
                }
            }

            }
        });


        return view1;
    }
}
