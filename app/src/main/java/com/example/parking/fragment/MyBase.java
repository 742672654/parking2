package com.example.parking.fragment;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.Toast;
import com.example.parking.R;
import com.example.parking.Static_bean;
import com.example.parking.activety.LoginActivity;
import com.example.parking.bean.http.HttpBean;
import com.example.parking.http.HttpCallBack2;
import com.example.parking.http.HttpManager2;
import com.example.parking.util.JsonUtil2;
import java.util.HashMap;
import java.util.Map;


/**
 * 功能说明: 订单Fragment
 * 日期:	2019年5月29日
 * 开发者:	KXD
 */
public class MyBase extends BaseFragment implements OnClickListener, HttpCallBack2 {


    public static final String TAG = "MyBase<我的信息>";


    //声明引用
    private WebView mWVmhtml;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.my_fragment, container, false);

        initView( rootView );
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        onPosition(TAG);
    }


    @JavascriptInterface
    public void orderList() {

        activity.openOrderList();
    }


    @JavascriptInterface
    public void signout() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("重新登录");
        builder.setIcon(R.mipmap.zhongxindenglu);
        builder.setMessage("是否退出登录");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                Toast.makeText(getActivity(), "点击退出了", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(),LoginActivity.class);
                intent.putExtra("joinType", TAG);
                getActivity().startActivity(intent);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder.show();
    }

    @JavascriptInterface
    public void changePassword() {

        Toast.makeText(getActivity(), "修改密码", Toast.LENGTH_SHORT).show();

            LayoutInflater factory = LayoutInflater.from(getActivity());
            final View textEntryView = factory.inflate(R.layout.inputbox2, null);
            final EditText editTextName = (EditText) textEntryView.findViewById(R.id.editTextName);
            final EditText editTextNumEditText = (EditText)textEntryView.findViewById(R.id.editTextNum);
            AlertDialog.Builder ad1 = new AlertDialog.Builder(getActivity());
            ad1.setTitle("修改密码:");
            ad1.setIcon(R.mipmap.mimaxiugai);
            ad1.setView(textEntryView);
            ad1.setPositiveButton("是", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int i) {

                    Log.i(editTextNumEditText.getText().toString()+"------", editTextName.getText().toString());
                    try {
                        if (editTextNumEditText.getText().toString().equals(editTextName.getText().toString())){

                            Map<String, String> param = new HashMap<String, String>(2);
                            param.put("token", activity.userBean.getToken());
                            param.put("chkpwd", editTextName.getText().toString());
                            HttpManager2.requestPost(Static_bean.changePwds, param, MyBase.this, "changePwds");

                        }else {
                            toast_makeText("两次输入的密码不一致");
                            return;
                        }
                    }catch (Exception e){
                        Log.w(TAG,e);
                    }
                }
            });
            ad1.setNegativeButton("否", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int i) { }
            });
            ad1.show();// 显示对话框
    }

    //TODO 初始化控件
    private void initView(View rootView) {

        mWVmhtml = rootView.findViewById(R.id.WV_Id);

        mWVmhtml.getSettings().setJavaScriptEnabled(true);


        StringBuffer buff = new StringBuffer("file:///android_asset/mine.html");


        mWVmhtml.loadUrl(buff.toString());

        mWVmhtml.addJavascriptInterface(this, "justTest");

        //设置在当前WebView继续加载网页
        mWVmhtml.setWebViewClient(new WebViewClient(){
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override  //WebView代表是当前的WebView
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                //表示在当前的WebView继续打开网页
                view.loadUrl(request.getUrl().toString());
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                Log.d("WebView","开始访问网页");
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Log.d("WebView","访问网页结束");

                StringBuffer buf = new StringBuffer("implement(");
                buf.append("'").append(activity.userBean.getParkname()).append("',");
                buf.append("'").append(activity.userBean.getParkaddr()).append("',");
                buf.append("'").append(activity.userBean.getMobile()).append("',");
                buf.append("'").append(activity.userBean.getParkid()).append("',");
                buf.append("'").append(Static_bean.findOrderByDate).append("')");

                //需要参数的JS函数名
                view.evaluateJavascript(buf.toString(), new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String responseJson) { //这里传入的参数就是JS函数的返回值
                        Log.d("JSMethod", "调用js返回值:"+ responseJson);
                    }
                });
            }
        });

        mWVmhtml.setWebChromeClient(new WebChromeClient(){

            @Override //监听加载进度
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
            }
            @Override//接受网页标题
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);

            }
        });
    }

    //TODO 接收修改密码
    public void changePwds(String obj) {


        HttpBean httpBean = JsonUtil2.fromJson(obj, HttpBean.class);

        if (httpBean.getCode()!=200){
            toast_makeText("密码修改失败,"+httpBean.getMessage());
            return;
        }
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("修改成功");
                builder.setIcon(R.mipmap.zhongxindenglu);
                builder.setMessage("密码修改成功，是否重新登录");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getActivity(), "重新登录", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        intent.putExtra("joinType", TAG);
                        getActivity().startActivity(intent);
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i){}
                });
                builder.show();
            }
        });

    }

    @Override
    public void onResponsePOST(String url, Map<String, String> param, String sign, String object) {

        super.onResponsePOST( url,  param,  sign,  object);
        Log.i(TAG, "url="+url + "----param="+param +";sign="+sign + ";object="+object+"'****"+param);

        switch (sign) {
            case "changePwds":changePwds(object);
                break;

            default:
                break;
        }

    }

}
