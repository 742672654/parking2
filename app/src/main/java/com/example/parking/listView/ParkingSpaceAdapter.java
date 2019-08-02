package com.example.parking.listView;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.parking.R;
import com.example.parking.bean.http.ParkingSpaceData;
import java.util.List;


public class ParkingSpaceAdapter extends BaseAdapter {


    private static final String TAG = "ParkingSpaceView";


    private List<ParkingSpaceData> list;  //数据源与配置器建立连接
    private LayoutInflater layoutInflater;//初始化布局填充器
    private Context context;


    public ParkingSpaceAdapter(Context context, List<ParkingSpaceData> list) {
        this.layoutInflater = layoutInflater.from(context);
        this.context=context;
        this.list = list;
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
        View view1 = layoutInflater.inflate(R.layout.parking_space_item,viewGroup,false);

        ParkingSpaceData parkingSpaceBean = list.get(i);
        ((TextView)view1.findViewById(R.id.listview_item_title1)).setText(parkingSpaceBean.getSubname());

        return view1;
    }

}
