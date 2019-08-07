package com.example.parking.jiguang;


import android.app.Activity;
import android.util.Log;
import java.util.Set;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;


public class JiGuangAPI {

    public static final String TAG = "JiGuangAPI<极光API>";

    // 这是来自 JPush Example 的设置别名的 Activity 里的代码。一般 App 的设置的调用入口，在任何方便的地方调用都可以。
    //TODO 设置别名
    public void setAlias(Activity activity, String alias) {
        Log.i(TAG,"设置极光别名为停车场ID="+alias);
        JPushInterface.setAliasAndTags(activity, alias, null, new TagAliasCallbackUser(activity));
    }
}


class TagAliasCallbackUser implements TagAliasCallback {

    private Activity activity;

    public TagAliasCallbackUser( Activity activity0 ) { activity = activity0; }

    @Override
    public void gotResult(int code, String alias, Set<String> tags) {
        String logs;
        switch (code) {
            case 0:
                logs = "Set tag and alias success";
                Log.i(JiGuangAPI.TAG, logs);
                // 建议这里往 SharePreference 里写一个成功设置的状态。成功设置一次后，以后不必再次设置了。
                break;
            case 6002:
                logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                Log.i(JiGuangAPI.TAG, logs);
                // 延迟 10 秒重新设置别名
                new TagAliasCallbackAgain(activity, alias).run();
                break;
            default:
                logs = "Failed with errorCode = " + code;
                Log.e(JiGuangAPI.TAG, logs);
        }
        ExampleUtil.showToast(logs, activity);
    }
}

class TagAliasCallbackAgain extends Thread{

    private Activity activity;
    private String alias;

    public TagAliasCallbackAgain(Activity activity0, String alias0) {
        activity = activity0;
        alias = alias0;
    }

    @Override
    public void run() {
        super.run();

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        new JiGuangAPI().setAlias(activity, alias);
    }
}

