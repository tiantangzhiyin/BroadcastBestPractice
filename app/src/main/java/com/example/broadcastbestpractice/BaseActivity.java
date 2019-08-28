package com.example.broadcastbestpractice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
/*作为所有活动的父类
广播接收器需要弹出一个对话框阻塞用户操作，静态注册的广播接收器无法弹出对话框，因此需要动态注册，
同时为了不在每个活动中注册广播接收器，需要在所有活动的父类BaseActivity中动态注册一个广播接收器
 */
public class BaseActivity extends AppCompatActivity {

    private ForceOfflineReceiver receiver;

    class ForceOfflineReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(final Context context,Intent intent){
            AlertDialog.Builder builder=new AlertDialog.Builder(context);
            builder.setTitle("Waring");
            builder.setMessage("You are force to be offline.Please try to login again.");
            builder.setCancelable(false);//强制用户确认,true点击其他地方可关闭弹窗，false不可关闭
            //为确认按钮设置逻辑
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ActivityCollector.finishAll();//销毁所有活动
                    Intent intent=new Intent(context,LoginActivity.class);
                    context.startActivity(intent);//重新启动登录界面
                }
            });
            builder.show();
        }
    }

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        ActivityCollector.addActivity(this);
    }
    @Override
    protected void onResume(){
        super.onResume();
        //动态注册广播接收器
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction("com.example.broadcastbestpractice.FORCE_OFFLINE");
        receiver=new ForceOfflineReceiver();
        registerReceiver(receiver,intentFilter);
    }
    @Override
    protected void onPause(){
        super.onPause();
        //为了不让活动在暂停状态也接收到广播，需要在暂停时注销广播接收器，在onResume()中注册接收器
        if(receiver!=null){
            unregisterReceiver(receiver);
            receiver=null;
        }
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}
