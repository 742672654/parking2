package com.example.parking.activety;


import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.example.parking.R;
import com.example.parking.Shared.User_Shared;
import com.example.parking.Static_bean;
import com.example.parking.bean.UserBean;
import com.example.parking.bean.http.HttpUserBean;
import com.example.parking.http.HttpManager2;
import com.example.parking.util.FileUtil;
import com.example.parking.util.JsonUtil2;
import java.io.File;
import java.util.HashMap;
import java.util.Map;



public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "LoginActivity<登录界面>";

    private EditText editText_account;
    private EditText editText_pass;
    private EditText editText_edition;



    @Override
    public void onBackPressed() {
        // 返回键监听
        // super.onBackPressed();//注释掉这行,back键不退出activity
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.activity = this;
        setContentView(R.layout.activity_login);

        initView();

        //创建文件夹
        String strPath = FileUtil.getSDCardPath() + "/parking/" ;
        File file = new File(strPath);
        if(!file.exists()){
            file.mkdirs();
        }

    }

    private void initView() {

        findViewById(R.id.button_login).setOnClickListener(this);
        findViewById(R.id.imageView_cuowu).setOnClickListener(this);
        findViewById(R.id.imageView_yanjingbi).setOnClickListener(this);

        editText_account = findViewById(R.id.editText_account);
        editText_pass = findViewById(R.id.editText_pass);
        editText_edition = findViewById(R.id.editText_edition);

        UserBean userBean = User_Shared.getALL(this);
        if (userBean!=null){
            editText_account.setText(userBean.getPhone());
            editText_pass.setText(userBean.getPassword());
        }
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.button_login:button_login();
                break;
            case R.id.imageView_cuowu:imageView_cuowu();
                break;
            case R.id.imageView_yanjingbi:imageView_yanjingbi();
                break;

            default:
                break;
        }
    }

    private void button_login(){

        String phone = editText_account.getText().toString();
        String password = editText_pass.getText().toString();
        String edition = editText_edition.getText().toString();

        if (phone==null || "".equals(phone)){

            Toast.makeText(LoginActivity.this,"请输入账号", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password==null || "".equals(password)){

            Toast.makeText(LoginActivity.this,"请输入密码", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String,String> param = new HashMap<String,String>(3);
            param.put("phone",phone);
            param.put("password",password);
            param.put("edition",edition);
        HttpManager2.requestPost(Static_bean.getlogin(),  param, this, "login");
    }

    //TODO >>>>接收登录
    private void button_login2( final Map<String, String> param, final String object ){


        LoginActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                try{

                    HttpUserBean httpUserBean = JsonUtil2.fromJson(object,HttpUserBean.class);
                    Log.i(TAG,httpUserBean.toString());

                    if (httpUserBean.getCode()!=200){
                        toast_makeText(httpUserBean.getMessage());
                        return;
                    }

                    HttpUserBean.UserInfo userInfo = JsonUtil2.fromJson(httpUserBean.getData().substring(12,httpUserBean.getData().length()-1),HttpUserBean.UserInfo.class);

                    UserBean userBean = new UserBean(userInfo);
                    userBean.setPhone(param.get("phone"));
                    userBean.setPassword(param.get("password"));;

                    //保存用户信息到缓存
                    User_Shared.saveALL(getApplicationContext(),userBean);

                    Log.i(TAG, "跳转到第二页：Activity=" + TAG + ";joinType=" + param.get("joinType"));
                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                    intent.putExtra("joinType", TAG);
                    LoginActivity.this.startActivity(intent);
                }catch (Exception e){

                    Log.w(TAG,e);
                    Toast.makeText(LoginActivity.this,"登录失败.系统错误", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void imageView_cuowu(){

        editText_account.setText("");
        editText_pass.setText("");
    }


    private void imageView_yanjingbi(){

        if (editText_pass.getInputType()==129){
            editText_pass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        }else{
            editText_pass.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD|InputType.TYPE_CLASS_TEXT);
        }

        Log.i(TAG, editText_pass.getInputType()+"---");
    }


    @Override
    public void onResponsePOST(String url, Map<String, String> param, String sign, String object) {
        super.onResponsePOST(url, param, sign, object);
        Log.i(TAG,"url="+url+";param="+param+";sign="+sign+";object="+object);

        button_login2(  param, object );
    }

}