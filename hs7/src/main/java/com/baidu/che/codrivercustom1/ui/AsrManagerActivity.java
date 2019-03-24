package com.baidu.che.codrivercustom1.ui;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.baidu.che.codrivercustom1.util.LogUtil;
import com.baidu.che.codrivercustom1.util.ToastUtils;
import com.baidu.che.codrivercustom1.widget.FuncButton;
import com.baidu.che.codriversdk.manager.CdAsrManager;

/**
 * 语音识别管理类
 */
public class AsrManagerActivity extends HS7BaseActivity {
    private static final String TAG = "AsrManagerActivity";
    private EditText mEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**
         * 接入该功能,必须先执行setAsrTool(...),接入者才能接收到相关回调
         */
        addFunctionBtn(new FuncButton(mContext, "设置语音对话流界面监听工具", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CdAsrManager.getInstance().setAsrTool(mAsrTool);
                ToastUtils.show("设置语音对话流界面监听工具");
            }
        }), new FuncButton(mContext, "通知DuerOS打开语音功能", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CdAsrManager.getInstance().enableAsr();
                ToastUtils.show("通知DuerOS打开语音功能");
            }
        }), new FuncButton(mContext, "通知DuerOS关闭语音功能", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CdAsrManager.getInstance().disableAsr();
                ToastUtils.show("通知DuerOS关闭语音功能");
            }
        }));

        addFunctionBtn(new FuncButton(mContext, "开启任意打断", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CdAsrManager.getInstance().openFullBargin();
                ToastUtils.show("开启任意打断");
            }
        }), new FuncButton(mContext, "关闭任意打断", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CdAsrManager.getInstance().closeFullBargin();
                ToastUtils.show("关闭任意打断");

            }
        }));

        addFunctionBtn(new FuncButton(mContext, "开启oneshot", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CdAsrManager.getInstance().openOneShot();
                ToastUtils.show("开启oneshot");
            }
        }), new FuncButton(mContext, "关闭oneshot", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CdAsrManager.getInstance().closeOneShot();
                ToastUtils.show("关闭oneshot");
            }
        }));

        addFunctionBtn(new FuncButton(mContext, "打开Activity语音界面", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CdAsrManager.getInstance().clickVrButton();
                ToastUtils.show("打开Activity语音界面");
            }
        }), new FuncButton(mContext, "打开dialog语音界面(可输入文本)", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CdAsrManager.getInstance().clickDialogVrButton("小度来了");
                ToastUtils.show("打开Dialog语音界面");
            }
        }));

        addFunctionBtn(new FuncButton(mContext, "注册非场景化命令词", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mVrCommand.addCommand("mute", "静音模式", "关闭声音");
                mVrCommand.addCommand("play", "篮球", "足球");
                CdAsrManager.getInstance().registerVrCmd(mVrCommand);
                ToastUtils.show("注册非场景化命令词");
            }
        }), new FuncButton(mContext, "取消注册非场景化命令词", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CdAsrManager.getInstance().unRegisterVrCmd("vr_cmd_test");
                ToastUtils.show("取消注册非场景化命令词");
            }
        }));

        mEditText = new EditText(mContext);
        mEditText.setHint("请输入自定义唤醒词");
        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
        mLLContent.addView(mEditText, params);
        mEditText.setVisibility(View.GONE);
    }

    private CdAsrManager.VrCommand mVrCommand = new CdAsrManager.VrCommand() {

        @Override
        public void onCommand(String type, String cmd) {
            LogUtil.e(TAG, "onCommand(): type=" + type + " cmd=" + cmd);
            ToastUtils.show(cmd);
        }

        @Override
        public String getId() {
            return "vr_cmd_test";
        }
    };

    private CdAsrManager.AsrTool mAsrTool = new CdAsrManager.AsrTool() {
        @Override
        public void onVrDialogShow() {
            ToastUtils.show("对话流界面展示");
        }

        @Override
        public void onVrDialogDismiss() {
            ToastUtils.show("对话流界面关闭");
        }

        @Override
        public void onWakeUp() {

        }
    };

}
