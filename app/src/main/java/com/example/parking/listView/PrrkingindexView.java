package com.example.parking.listView;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.parking.R;
import com.example.parking.bean.http.OrderlistBean;
import com.example.parking.bean.http.SelectSubPlaceBean;
import com.example.parking.fragment.ParkingIndexFragment;
import java.util.List;


public class PrrkingindexView extends BaseAdapter {

    private List<SelectSubPlaceBean.SelectSubPlaceData>  list;  //数据源与配置器建立连接
    private Context context;
    private ParkingIndexFragment fragment;

    public PrrkingindexView(Context context, ParkingIndexFragment fragment, List<SelectSubPlaceBean.SelectSubPlaceData>  list) {
        this.fragment = fragment;
        this.context = context;
        this.list = list;
    }

    //itme的数量
    @Override
    public int getCount() { return list==null?0:(list.size()/2+list.size()%2); }

    //返回第几条itme信息
    @Override
    public Object getItem(int i) { return list.get(i); }

    //返回itme的ID
    @Override
    public long getItemId(int position) { return position; }



    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {


        //第三个参数false：不会为item添加父布局
        View view1 = LayoutInflater.from(context).inflate(R.layout.listview_prrkingindex_item, viewGroup,false);
        setleft(  view1,i );
        setright(  view1,i );
        return view1;
    }


    private void setleft(View view1, int i ){

        RelativeLayout relativeLayout = view1.findViewById(R.id.listview_prrkingindex_item_RelativeLayout_Left);
        relativeLayout.setVisibility( View.VISIBLE );

        TextView id_number_left = view1.findViewById(R.id.id_number_left);
        TextView car_number_left = view1.findViewById(R.id.car_number_left);
        TextView start_time_left = view1.findViewById(R.id.start_time_left);
        TextView earnest_money_left = view1.findViewById(R.id.earnest_money_left);
        TextView prkingind_p = view1.findViewById(R.id.prkingind_p);

        final SelectSubPlaceBean.SelectSubPlaceData selectSubPlaceDate = list.get(i*2);

        if ( selectSubPlaceDate.getHavecar() == 2 ){

            relativeLayout.setBackgroundResource(R.drawable.listview_index_yes);

            id_number_left.setText(selectSubPlaceDate.getCode());
            start_time_left.setText("开始时间:"+selectSubPlaceDate.getParktime().substring(5,16));
            earnest_money_left.setText("预付金额:"+ (selectSubPlaceDate.getPreprice()==null?selectSubPlaceDate.getPreprice()+"元":"0元"));
            car_number_left.setText(selectSubPlaceDate.getCarnum());
            prkingind_p.setVisibility( View.INVISIBLE );

            relativeLayout.setPadding(relativeLayout.getPaddingLeft(),
                    relativeLayout.getPaddingTop(),
                    relativeLayout.getPaddingRight(),
                    relativeLayout.getPaddingBottom());

            relativeLayout.setOnClickListener(new android.view.View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //TODO 打开收费页面
                    fragment.activity.openOrder_details( new OrderlistBean.OrderlistData( selectSubPlaceDate ) );
                }
            });
        }else{

            relativeLayout.setBackgroundResource(R.drawable.listview_index_no);
            id_number_left.setText(selectSubPlaceDate.getCode());

            view1.findViewById(R.id.car_fang_left).setVisibility( View.INVISIBLE );
            car_number_left.setVisibility( View.INVISIBLE );
            start_time_left.setVisibility( View.INVISIBLE );
            earnest_money_left.setVisibility( View.INVISIBLE );

            relativeLayout.setPadding(relativeLayout.getPaddingLeft(),
                    relativeLayout.getPaddingTop(),
                    relativeLayout.getPaddingRight(),
                    relativeLayout.getPaddingBottom());

            relativeLayout.setOnClickListener(new android.view.View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //TODO 打开停车保存页面
                    fragment.openParking(selectSubPlaceDate);
                }
            });
        }



    }


    private void setright( View view1, int i ){


        if ( i*2+1>= list.size() ){ return; }

        RelativeLayout relativeLayout = view1.findViewById(R.id.listview_prrkingindex_item_RelativeLayout_Right);
        relativeLayout.setVisibility( View.VISIBLE );


        TextView id_number_right = view1.findViewById(R.id.id_number_right);
        TextView car_number_right = view1.findViewById(R.id.car_number_right);
        TextView start_time_right = view1.findViewById(R.id.start_time_right);
        TextView earnest_money_right = view1.findViewById(R.id.earnest_money_right);
        TextView prkingind_p2 = view1.findViewById(R.id.prkingind_p2);

        final SelectSubPlaceBean.SelectSubPlaceData selectSubPlaceDate = list.get(i*2+1);

        if ( selectSubPlaceDate.getHavecar() == 2 ){

            relativeLayout.setBackgroundResource(R.drawable.listview_index_yes);

            id_number_right.setText(selectSubPlaceDate.getCode());
            car_number_right.setText(selectSubPlaceDate.getCarnum());
            start_time_right.setText("开始时间:"+selectSubPlaceDate.getParktime().substring(5,16));
            earnest_money_right.setText("预付金额:"+ (selectSubPlaceDate.getPreprice()==null?selectSubPlaceDate.getPreprice()+"元":"0元"));
            car_number_right.setText(selectSubPlaceDate.getCarnum());
            prkingind_p2.setVisibility( View.INVISIBLE );

            relativeLayout.setPadding(relativeLayout.getPaddingLeft(),
                    relativeLayout.getPaddingTop(),
                    relativeLayout.getPaddingRight(),
                    relativeLayout.getPaddingBottom());

            relativeLayout.setOnClickListener(new android.view.View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //TODO 打开收费页面
                    fragment.activity.openOrder_details( new OrderlistBean.OrderlistData( selectSubPlaceDate ) );
                }
            });
        }else{

            relativeLayout.setBackgroundResource(R.drawable.listview_index_no);
            id_number_right.setText(selectSubPlaceDate.getCode());

            view1.findViewById(R.id.car_fang_right).setVisibility( View.INVISIBLE );
            car_number_right.setVisibility( View.INVISIBLE );
            start_time_right.setVisibility( View.INVISIBLE );
            earnest_money_right.setVisibility( View.INVISIBLE );


            relativeLayout.setPadding(relativeLayout.getPaddingLeft(),
                    relativeLayout.getPaddingTop(),
                    relativeLayout.getPaddingRight(),
                    relativeLayout.getPaddingBottom());

            relativeLayout.setOnClickListener(new android.view.View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //TODO 打开停车保存页面
                    fragment.openParking( selectSubPlaceDate);
                }
            });
        }
    }
}
