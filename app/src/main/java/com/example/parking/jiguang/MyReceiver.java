package com.example.parking.jiguang;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import com.example.parking.activety.MainActivity;
import com.example.parking.bean.JiguangBean;
import com.example.parking.bean.OrderDbBean;
import com.example.parking.db.Jiguang_DB;
import com.example.parking.db.Order_DB;
import com.example.parking.util.IntentUtil;
import com.example.parking.util.JsonUtil2;
import com.example.parking.util.StringUtil;
import com.example.parking.util.TimeUtil;




/**
 * 自定义接收器
 * 
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class MyReceiver extends BroadcastReceiver {
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
			//保存到数据库
			Jiguang_DB.insert_in_Jiguang(((MainActivity)MainActivity.activity).baseSQL_DB,jiguangBean);


			Bundle bundle2 = new Bundle();
			bundle2.putString( "joinType",TAG );
			bundle2.putString("text","接收到推送");
			Message message2 = new Message();
			message2.obj = jiguangBean;
			message2.what = MainActivity.JiguangPush;
			((MainActivity)MainActivity.activity).handler.sendMessage(message2);

		} catch (Exception e){
			Log.w(TAG,e);
		}
	}




	//			if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
//				String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
//				Logger.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
//				//send the Registration Id to your server...
//
//			} else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
//				Logger.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
//				processCustomMessage(context, bundle);
//
//			} else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
//				Logger.d(TAG, "[MyReceiver] 接收到推送下来的通知");
//				int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
//				Logger.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);
//
//			} else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
//				Logger.d(TAG, "[MyReceiver] 用户点击打开了通知");
//
//				//打开自定义的Activity
////				Intent i = new Intent(context, TestActivity.class);
////				i.putExtras(bundle);
////				//i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
////				context.startActivity(i);
//
//			} else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
//				Logger.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
//				//在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..
//
//			} else if(JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
//				boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
//				Logger.w(TAG, "[MyReceiver]" + intent.getAction() +" connected state change to "+connected);
//			} else {
//				Logger.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
//			}
	//send msg to MainActivity
//	private void processCustomMessage(Context context, Bundle bundle) {
//		if (MainActivity.isForeground) {
//			String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
//			String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
//			Intent msgIntent = new Intent(MainActivity.MESSAGE_RECEIVED_ACTION);
//			msgIntent.putExtra(MainActivity.KEY_MESSAGE, message);
//			if (!ExampleUtil.isEmpty(extras)) {
//				try {
//					JSONObject extraJson = new JSONObject(extras);
//					if (extraJson.length() > 0) {
//						msgIntent.putExtra(MainActivity.KEY_EXTRAS, extras);
//					}
//				} catch (JSONException e) {
//
//				}
//			}
//			LocalBroadcastManager.getInstance(context).sendBroadcast(msgIntent);
//		}
//	}


}
