package com.example.parking.activety;



import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.parking.R;
import com.example.parking.bean.UserBean;
import com.example.parking.Shared.User_Shared;
import com.example.parking.fragment.AlertFeagment;
import com.example.parking.fragment.MyFragment;
import com.example.parking.fragment.NoticeFragment;
import com.example.parking.fragment.OrderFragment;
import com.example.parking.fragment.OrderListBase;
import com.example.parking.fragment.OrderListFragment;
import com.example.parking.fragment.OrderPayBackFragment;
import com.example.parking.fragment.Order_detailsFragment;
import com.example.parking.fragment.Order_list_detailsFragment;
import com.example.parking.fragment.ParkingBase;
import com.example.parking.fragment.ParkingFragment;
import com.example.parking.fragment.ParkingIndexFragment;
import com.example.parking.fragment.WhiteFragment;
import com.example.parking.service.TokenServiceF5;
import com.example.parking.util.ImageUitls;
import java.util.Timer;
import java.util.TimerTask;



public class MainBaseActivity extends BaseActivity {




    private static final String TAG = "BaseActivity<主Base>";

    protected ImageView user_photo = null;
    protected TextView user_name= null, user_parkingName= null, parking_surplus= null, parking_already= null;
    public RelativeLayout titles;
    protected Timer tokenServiceTimer; //定时刷新token
    public static String FragmentStartTAG = "";

    public ParkingFragment parkingFragment = null;
    public ParkingIndexFragment parkingIndexFragment =  null;
    public OrderFragment orderFragment = null;
    public MyFragment myFragment = null;
    public NoticeFragment noticeFragment = null;
    public Order_detailsFragment order_detailsFragment = null;
    public OrderPayBackFragment orderPayBackFragment = null;
    public OrderListFragment orderListFragment = null;
    public UserBean userBean = null;
    public Order_list_detailsFragment order_list_detailsFragment = null;
    public AlertFeagment alertFeagment = null;
    public WhiteFragment whiteFragment = null;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

            switch (item.getItemId()) {
                case R.id.navigation_1:

                    fragmentTransaction.replace(R.id.activity_fragment, parkingIndexFragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    return true;
                case R.id.navigation_2:

                    fragmentTransaction.replace(R.id.activity_fragment, orderFragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    return true;
                case R.id.navigation_3:

                    fragmentTransaction.replace(R.id.activity_fragment,myFragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    return true;

                case R.id.navigation_4:


                        Log.i(TAG,"BottomNavigationView点击："+4);

                        fragmentTransaction.replace(R.id.activity_fragment,noticeFragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    if (FragmentStartTAG.equals(NoticeFragment.TAG)){
                        noticeFragment.initView();
                    }

                    return true;
            }
            return false;
        }
    };



    @Override
    public void onBackPressed() {

        // 返回键监听
        switch (FragmentStartTAG){

            case AlertFeagment.TAG: break;
            case ParkingFragment.TAG: if (parkingFragment.isShow()){ return; }   break;
            case ParkingBase.TAG:  if (parkingFragment.isShow()){ return; }   break;

            case Order_list_detailsFragment.TAG: break;

            case Order_detailsFragment.TAG: break;
            case OrderPayBackFragment.TAG: break;
            case OrderListBase.TAG: break;
            case OrderListFragment.TAG: break;
            default:return;
        }

        super.onBackPressed();//注释掉这行,back键不退出activity
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        user_parkingName = findViewById(R.id.user_parkingName);
        parking_surplus = findViewById(R.id.parking_surplus);
        parking_already = findViewById(R.id.parking_already);

        user_photo = findViewById(R.id.user_photo);
        user_name = findViewById(R.id.user_name);

        titles = findViewById(R.id.titles);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        myFragment = new MyFragment();
        myFragment.activity = (MainActivity)activity;

        orderFragment = new OrderFragment();
        orderFragment.activity = (MainActivity)activity;

        parkingIndexFragment =  new ParkingIndexFragment();
        parkingIndexFragment.activity = (MainActivity)activity;

        parkingFragment = new ParkingFragment();
        parkingFragment.activity = (MainActivity)activity;

        order_detailsFragment = new Order_detailsFragment();
        order_detailsFragment.activity = (MainActivity)activity;

        orderPayBackFragment = new OrderPayBackFragment();
        orderPayBackFragment.activity = (MainActivity)activity;

        orderListFragment = new OrderListFragment();
        orderListFragment.activity = (MainActivity)activity;

        order_list_detailsFragment = new Order_list_detailsFragment();
        order_list_detailsFragment.activity = (MainActivity)activity;

        noticeFragment = new NoticeFragment();
        noticeFragment.activity = (MainActivity)activity;

        alertFeagment = new AlertFeagment();
        alertFeagment.activity = (MainActivity)activity;

        whiteFragment = new WhiteFragment();
        whiteFragment.activity = (MainActivity)activity;

        userBean = User_Shared.getALL(getApplicationContext());

        //TODO 如果是登录进来的就打开首页，否则打开消息页
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.activity_fragment,
                            getIntent().getStringExtra("joinType") != null ? parkingIndexFragment : myFragment);
        fragmentTransaction.commit();


        if (user_name!=null && userBean!=null && userBean.getNickName()!=null){
            user_name.setText(userBean.getNickName()+"");
        }

        if (user_parkingName!=null && userBean!=null && userBean.getParkname()!=null){
            user_parkingName.setText(userBean.getParkname()+"");
        }

        ImageUitls.setImageHttpURL(this,user_photo,userBean.getAvatarUrl());

        tokenServiceTimer = new Timer(true);
        tokenServiceTimer.schedule(new TimerTask() {
            public void run() {

                Log.d(TAG,"定时刷新token");
                startService(   new Intent(activity, TokenServiceF5.class).putExtra("token",userBean.getToken()));
            }
        }, 0, 1000*60*60*4);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) { return true; }//创建的菜单显示出来

}
