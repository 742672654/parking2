package com.example.parking.listView;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.parking.R;
import java.util.List;


public class OrderAllView extends BaseAdapter {

    private List<String>  list;  //数据源与配置器建立连接
    private Context context;

    public OrderAllView(Context context, List<String> list) {
        this.context = context;
        this.list = list;
    }

    //itme的数量
    @Override
    public int getCount(){ return list.size(); }

    //返回第几条itme信息
    @Override
    public Object getItem(int i){ return list.get(i); }

    //返回itme的ID
    @Override
    public long getItemId(int position){ return position; }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup){


        //第三个参数false：不会为item添加父布局
        View view1 = LayoutInflater.from(context).inflate(R.layout.listview_order_all_item, viewGroup,false);

        TextView all_cph = view1.findViewById(R.id.all_cph);
        TextView all_jine = view1.findViewById(R.id.all_jine);
        TextView all_in = view1.findViewById(R.id.all_in);
        TextView all_out = view1.findViewById(R.id.all_out);

        return view1;
    }


}
