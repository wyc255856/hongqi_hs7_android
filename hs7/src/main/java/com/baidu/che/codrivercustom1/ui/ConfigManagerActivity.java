package com.baidu.che.codrivercustom1.ui;

import android.os.Bundle;
import android.view.View;

import com.baidu.che.codrivercustom1.util.ToastUtils;
import com.baidu.che.codrivercustom1.widget.FuncButton;
import com.baidu.che.codriversdk.manager.CdAsrManager;
import com.baidu.che.codriversdk.manager.CdConfigManager;

/**
 * 配置管理
 */

public class ConfigManagerActivity extends HS7BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addFunctionBtn(new FuncButton(mContext, "休眠DuerOS", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CdConfigManager.getInstance().notifySystemSleep();
                ToastUtils.show("休眠DuerOS");
            }
        }), new FuncButton(mContext, "唤醒DuerOS", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CdConfigManager.getInstance().notifySystemWakeUp();
                ToastUtils.show("唤醒DuerOS");
            }
        }));

        addFunctionBtn(new FuncButton(mContext, "启动CoDriver", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CdAsrManager.getInstance().openDialog("小度来了");
                ToastUtils.show("打开语音对话流");
            }
        }), new FuncButton(mContext, "关闭CoDriver", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CdAsrManager.getInstance().closeDialog();
                ToastUtils.show("关闭语音对话流");
            }
        }));

//        addTitle(20, "设置对话流界面样式", null);

        // 任意选择一种样式进行设置
//        addFunctionBtn(new FuncButton(mContext, "activity样式", new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                CdConfigManager.getInstance().setDisplayView(CdConfigManager.DisplayView.ACTIVITY);
//                ToastUtils.show("设置为activity样式");
//            }
//        }), new FuncButton(mContext, "dialog样式", new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                CdConfigManager.getInstance().setDisplayView(CdConfigManager.DisplayView.DIALOG);
//                ToastUtils.show("设置为dialog样式");
//            }
//        }));

    }
}
