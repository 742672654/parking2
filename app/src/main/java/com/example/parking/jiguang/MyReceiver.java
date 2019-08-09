package com.example.parking.jiguang;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import com.example.parking.Static_bean;
import com.example.parking.activety.BaseActivity;
import com.example.parking.activety.MainActivity;
import com.example.parking.bean.JiguangBean;
import com.example.parking.bean.OrderDbBean;
import com.example.parking.bean.http.HttpBean;
import com.example.parking.bean.http.Report_orderlistBean;
import com.example.parking.db.Jiguang_DB;
import com.example.parking.db.Order_DB;
import com.example.parking.fragment.Order_detailsFragment;
import com.example.parking.http.HttpCallBack2;
import com.example.parking.http.HttpManager2;
import com.example.parking.util.IntentUtil;
import com.example.parking.util.JsonUtil2;
import com.example.parking.util.StringUtil;
import com.example.parking.util.TimeUtil;

import java.util.HashMap;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;


/**
 * 自定义接收器
 * 
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class MyReceiver extends BroadcastReceiver{
	private static final String TAG = "MyReceiver<jiguang极光推送>";

	@Override
	public void onReceive(Context context, Intent intent) {

		try {

			Bundle bundle = intent.getExtras();
			Logger.d(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + IntentUtil.printBundle(bundle));
			if (bundle.get("cn.jpush.android.ALERT")==null)return;
			if (bundle.get("cn.jpush.android.NOTIFICATION_ID")==null)return;


			Logger.i(TAG+"<接收的内容>","cn.jpush.android.ALERT="+bundle.get("cn.jpush.android.ALERT"));
			Logger.i(TAG+"<接收消息的ID>","cn.jpush.android.NOTIFICATION_ID="+bundle.get("cn.jpush.android.NOTIFICATION_ID"));

			JiguangBean jiguangBean = JsonUtil2.fromJson(bundle.get("cn.jpush.android.ALERT").toString(), com.example.parking.bean.JiguangBean.class);
			if (jiguangBean==null)return;

			jiguangBean.setnOTIFICATION_ID(StringUtil.getUuid()+ String.valueOf((int)bundle.get("cn.jpush.android.NOTIFICATION_ID")) );
			jiguangBean.setPushTimeLong(TimeUtil.dateToStamp(jiguangBean.getPushTime()));

			Log.i(TAG,"保存="+jiguangBean.toString());


			if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
				String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
				Log.d(TAG, "[MyReceiver62] 接收Registration Id : " + regId);

			} else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
				Log.d(TAG, "[MyReceiver66] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));

			} else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {


				//保存到数据库
				Jiguang_DB.insert_in_Jiguang(((MainActivity)MainActivity.activity).baseSQL_DB,jiguangBean);

				//添加语音播报
				Bundle bundle2 = new Bundle();
				bundle2.putString( "joinType",TAG );
				bundle2.putString("text","接收到推送,播放音乐");
				Message message2 = new Message();
				message2.obj = jiguangBean;
				message2.what = MainActivity.JiguangPush;
				((MainActivity)MainActivity.activity).handler.sendMessage(message2);

				//如果是在订单详情页面
				if (Order_detailsFragment.TAG.equals(((MainActivity)MainActivity.activity).FragmentStartTAG)){

					//刚好也在查看这个订单
					if (((MainActivity)MainActivity.activity)
							.order_detailsFragment.orderlistData.getId().equals(jiguangBean.getDevId())){

						Map<String, String> param = new HashMap<String, String>(3);
						param.put("id",jiguangBean.getDevId());
						param.put("joinType",TAG);
						HttpBean httpBean = new HttpBean();
						httpBean.setCode(200);
						httpBean.setData(jiguangBean.getPushTime());
						((MainActivity)MainActivity.activity).order_detailsFragment.payPointOrderToPoint(param,JsonUtil2.toJson(httpBean));
					}
				}

			} else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
				Log.d(TAG, "[MyReceiver75] 用户点击打开了通知");

				//打开对应车位页面
				if (jiguangBean.getMsgType().equals("move")){

					Map<String,String> param = new HashMap<String, String>(5);
					param.put("subId",jiguangBean.getDevDock());
					param.put("type", jiguangBean.getInOut());
					param.put("token", ((MainActivity)BaseActivity.activity).userBean.getToken());
					HttpManager2.requestPost(Static_bean.selectSubPlace(),  param, ((MainActivity)BaseActivity.activity).noticeFragment, "selectSubPlace");
				}else if (jiguangBean.getMsgType().equals("finish")){

					Map<String,String> param = new HashMap<String,String>(6);
					param.put("page","1");
					param.put("size","1");
					param.put("orderid",jiguangBean.getDevId());
					param.put("token",((MainActivity)BaseActivity.activity).userBean.getToken());
					HttpManager2.requestPost(Static_bean.pointOrderReport_orderlist(),  param, ((MainActivity)BaseActivity.activity).order_list_detailsFragment, "pointOrderReport_orderlist");

					//去除<未处理>标记
					Jiguang_DB.updata_Jiguang(((MainActivity)BaseActivity.activity).baseSQL_DB,jiguangBean.getnOTIFICATION_ID(),null);
				}else {

					((MainActivity)BaseActivity.activity).openWhite();
					((MainActivity)BaseActivity.activity).openAlert(jiguangBean );
				}

			}

		} catch (Exception e){
			Log.w(TAG,e);
		}
	}

}
