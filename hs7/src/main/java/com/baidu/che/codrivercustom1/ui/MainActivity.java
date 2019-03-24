/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.che.codrivercustom1.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.baidu.che.codrivercustom1.util.ToastUtils;
import com.baidu.che.codrivercustom1.widget.FuncButton;
import com.baidu.che.codriversdk.manager.CdAsrManager;
import com.baidu.che.codriversdk.manager.CdCarInfoQueryManager;
import com.baidu.che.codriversdk.manager.CdConfigManager;
import com.faw.hs7.HS7ManualWebActivity;

public class MainActivity extends HS7BaseActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addFunctionBtn(new FuncButton(mContext, "配置管理", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDemo("配置管理", ConfigManagerActivity.class);
            }
        }), new FuncButton(mContext, "系统控制", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDemo("车辆控制", SystemManagerActivity.class);
            }
        }), new FuncButton(mContext, "TTS语音播报管理", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDemo("TTS语音播报管理", TTSManagerActivity.class);
            }
        }));

        addFunctionBtn(new FuncButton(mContext, "多媒体功能管理", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDemo("多媒体功能管理", MediaManagerActivity.class);
            }
        }), new FuncButton(mContext, "蓝牙电话功能", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDemo("蓝牙电话功能", BlueToothCallManagerActivity.class);
            }
        }), new FuncButton(mContext, "导航功能", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDemo("导航功能", NaviActivity.class);
            }
        }));

        addFunctionBtn(new FuncButton(mContext, "音乐功能", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDemo("音乐功能", MusicActivity.class);
            }
        }), new FuncButton(mContext, "语音识别", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDemo("语音识别", AsrManagerActivity.class);
            }
        }), new FuncButton(mContext, "电子手册", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CdCarInfoQueryManager.getInstance().setQueryCarInfoTool(new CdCarInfoQueryManager.QueryCarInfoTool() {
                    @Override
                    public boolean answerContent(String feature, String extra) {
                        ToastUtils.show("1234567"+feature + extra);
                        Log.e("tag","------"+feature + extra);
                        startActivity(new Intent(MainActivity.this, HS7ManualWebActivity.class));
                        return false;
                    }
                });
                ToastUtils.show("设置电子手册监听");
            }
        }));
        CdAsrManager.getInstance().addWakeUpWord("你好");  //添加唤醒词
        CdAsrManager.getInstance().addWakeUpWord("你好百度汽车");  //添加唤醒词
    }

    private void startDemo(String name, Class<? extends Activity> activity) {
        try {
            if (!CdConfigManager.getInstance().isInitSuccess()) {
                ToastUtils.show("sdk初始化未成功, 请安装DuerOS.apk");
                return;
            }
            Intent intent = new Intent(mContext, activity);
            intent.putExtra("title", name);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
