package com.faw.hs7;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.faw.hs7.util.HS7ManuaConfig;
import com.faw.hs7.util.HS7SharedpreferencesUtil;
import com.faw.hs7.util.FireUtil;
import com.faw.hs7.util.GetPostUrl;
import com.faw.hs7.util.LogUtil;
import com.wyc.hs7.R;

import java.util.ArrayList;


public class HS7ManuaWelecomActivity extends HS7BaseActivity {


    public int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 2000;
    public View manua_mzsm;
    int time = 6;
    TextView next;

    @Override
    protected void initData() {
        LogUtil.logError("android.os.Build.VERSION.RELEASE = " + android.os.Build.VERSION.RELEASE);

        setContentView(R.layout.hs7_activity_welecom);
        if (getIntent() != null && getIntent().getStringExtra("carModel") != null) {
            HS7SharedpreferencesUtil.setCarModel(this, getIntent().getStringExtra("carModel"));
        }
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
//        if (currentapiVersion >= 23) {
//            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                    != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
//                    != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
//                    != PackageManager.PERMISSION_GRANTED) {
//                //申请WRITE_EXTERNAL_STORAGE权限
//                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
//                                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE, Manifest.permission.SYSTEM_ALERT_WINDOW},
//                        WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
//            } else {
//                goMainActivity();
//            }
//        } else {
            goMainActivity();
//        }
        if (HS7SharedpreferencesUtil.getIsFirst(this)) {
            FireUtil.isExist(this);
        }
        new Thread() {
            @Override
            public void run() {
                super.run();
                String result = GetPostUrl.get("http://www.e-guides.faw.cn/ev_admin/index.php?m=home&c=index&a=get_new_version&car_type=1");
                LogUtil.logError("result = " + result);
                HS7SharedpreferencesUtil.setVersion(HS7ManuaWelecomActivity.this, result);
                HS7ManuaConfig.VERSION = result;
            }
        }.start();

        manua_mzsm = findViewById(R.id.manua_mzsm);
        next = (TextView) findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (time <= 3) {
                    handler.removeMessages(100001);
                    goNext();
                }
            }
        });

    }

    @Override
    protected void initWidgetActions() {
        LogUtil.logError("============isServiceRunning1==============");
//        if (!isServiceRunning(this, "HS7TargetService")) {
//            LogUtil.logError("============isServiceRunning==============");
//            Intent i = new Intent();
//            i.setClass(this, HS7TargetService.class);
//            startService(i);
//        }
    }


    private void goMainActivity() {
        handler.sendEmptyMessageDelayed(0, 2000);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == WRITE_EXTERNAL_STORAGE_REQUEST_CODE)
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                // permission was granted, yay! Do the
                // contacts-related task you need to do.
                goMainActivity();

            } else {

                finish();
                // permission denied, boo! Disable the
                // functionality that depends on this permission.
            }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeMessages(100001);
        handler.removeMessages(10000);
        handler.removeMessages(0);
    }

    public static boolean isServiceRunning(Context context, String ServiceName) {
        if (("").equals(ServiceName) || ServiceName == null)
            return false;
        ActivityManager myManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        ArrayList<ActivityManager.RunningServiceInfo> runningService = (ArrayList<ActivityManager.RunningServiceInfo>) myManager
                .getRunningServices(30);
        for (int i = 0; i < runningService.size(); i++) {
            if (runningService.get(i).service.getClassName().toString()
                    .equals(ServiceName)) {
                return true;
            }
        }
        return false;
    }

    private void goNext() {


        boolean isFirst = HS7SharedpreferencesUtil.getIsFirst(com.faw.hs7.HS7ManuaWelecomActivity.this);
//        if (!isFirst) {
        handler.removeMessages(100001);
        handler.removeMessages(10000);
        if (HS7SharedpreferencesUtil.isGuest(this)) {
            Intent intent = new Intent(com.faw.hs7.HS7ManuaWelecomActivity.this, HS7ManualSelecteCarActivity.class);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(com.faw.hs7.HS7ManuaWelecomActivity.this, HS7ManualWebActivity.class);
            startActivity(intent);
            HS7SharedpreferencesUtil.setIsFirst(HS7ManuaWelecomActivity.this, false);
            finish();
        }

    }

    public boolean onPause = false;

    @Override
    public void onPause() {
        LogUtil.logError("============onPause==============");
        super.onPause();
        onPause = true;
        handler.removeMessages(100001);
        handler.removeMessages(10000);
        handler.removeMessages(0);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (onPause) {
            onPause = false;
            handler.sendEmptyMessageDelayed(100001, 1000);
        }
    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            super.handleMessage(msg);

            boolean isFirst = HS7SharedpreferencesUtil.getIsFirst(com.faw.hs7.HS7ManuaWelecomActivity.this);
            if (isFirst) {
                if (msg.what == 0) {
                    manua_mzsm.setVisibility(View.VISIBLE);
                    handler.sendEmptyMessage(100001);
                } else if (msg.what == 10000) {
                    goNext();
                } else if (msg.what == 100001) {
                    if (time == 0) {
                        goNext();
                        return;
                    }
                    if (time > 3) {
                        next.setText("剩余" + time + "秒");
                    } else {
                        next.setText("剩余" + time + "秒 跳过>");
                    }
                    time--;
                    handler.sendEmptyMessageDelayed(100001, 1000);
                }
            } else {
                goNext();
            }
        }
    };


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
//
            handler.removeMessages(100001);
            handler.removeMessages(10000);
            handler.removeMessages(0);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void removeHandler() {

    }
}
