package com.baidu.che.codrivercustom1.ui;

import android.os.Bundle;
import android.view.View;

import com.baidu.che.codrivercustom1.util.ToastUtils;
import com.baidu.che.codrivercustom1.widget.FuncButton;
import com.baidu.che.codriversdk.manager.CdBlueToothManager;
import com.baidu.che.codriversdk.manager.CdPhoneManager;

/**
 * 蓝牙电话管理
 */

public class BlueToothCallManagerActivity extends HS7BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**
         * 接入该功能,必须先执行setBlueToothTool(...),接入者才能接收到相关回调
         */
        addFunctionBtn(new FuncButton(mContext, "设置蓝牙工具实例", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.show("设置蓝牙工具实例");
                CdBlueToothManager.getInstance().setBlueToothTool(mBlueToothTool);
            }
        }), new FuncButton(mContext, "通知蓝牙状态", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //可设置的蓝牙状态:
                //断开连接  BT_DISCONNECTED,
                //连接中   BT_CONNECTING
                //已连接   BT_CONNECTED
                //正在断开连接    BT_DISCONNECTING
                //正在取消  BT_CANCELLING
                //已经取消  BT_CANCELLED
                //已经配对  BT_PAIRED
                //未配对   BT_NOPAIR
                CdBlueToothManager.getInstance().onNotifyBTStatus(CdBlueToothManager.BtStatus.BT_CONNECTED);
                ToastUtils.show("通知蓝牙状态");
            }
        }), new FuncButton(mContext, "通知电话本授权状态", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //可设置的电话本授权状态:
                //无授权   BT_PHONE_NO_AUTHORIZED,
                //正在授权  BT_PHONE_AUTHORIZING,
                //已授权   BT_PHONE_AUTHORIZED,
                //请求授权中 BT_PHONE_REQUESTING,
                //保留字段  BT_PHONE_RESERVED_1,
                //保留字段  BT_PHONE_RESERVED_2,
                //无法获取授权    BT_PHONE_CANNOT_AUTHORIZED
                CdBlueToothManager.getInstance()
                        .onNotifyBTPhoneStatus(CdBlueToothManager.BTPhoneStatus.BT_PHONE_AUTHORIZED);
                ToastUtils.show("通知电话本授权状态");
            }
        }));

        addFunctionBtn(new FuncButton(mContext, "设置拨打电话工具", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CdPhoneManager.getInstance().setPhoneTool(new CdPhoneManager.PhoneTool() {
                    @Override
                    public void dialNum(String number) {
                        ToastUtils.show("拨打电话:" + number);
                    }

                    @Override
                    public void findNumber(String json) {
                        ToastUtils.show(json);
                    }
                });
                ToastUtils.show("设置拨打电话工具");
            }
        }), new FuncButton(mContext, "通知电话本下载情况", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //电话本下载的值:  参考PhoneBookDownloadStatus   line 160
                //默认状态  CONTACTS_NO_DOWNLOADED,
                //访问配置文件成功  ACTION_PBAP_DOWNLOAD_SUPPORT,
                //电话本下载请求   CONTACTS_DOWNLOAD_REQUEST,
                //电话本下载开始   CONTACTS_DOWNLOAD_STARTED,
                //电话本下载中    CONTACTS_DOWNLOAD_PROGRESS,
                //通讯电话本下载完成 CONTACTS_DOWNLOAD_COMPLETE,
                //联系人准备更新   CONTACTS_UPDATE_READY,
                //联系人更新完毕   CONTACTS_UPDATE_COMPLETE,
                //通讯记录下载开始  CALLLOGS_DOWNLOAD_STARTED,
                //通讯记录下载中   CALLLOGS_DOWNLOAD_PROGRESS,
                //通讯记录下载完成  CALLLOGS_DOWNLOAD_COMPLETE,
                //其他    OTHER
                CdPhoneManager.getInstance()
                        .onNotifyPhoneStatus(CdPhoneManager.PhoneDownloadStatus.CALLLOGS_DOWNLOAD_COMPLETE);
                ToastUtils.show("通知电话本下载情况");
            }
        }), new FuncButton(mContext, "同步联系人通讯录数据", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CdPhoneManager.PhoneContactList list = new CdPhoneManager.PhoneContactList();
                list.addContact("张三", "95555");
                list.addContact("张三", "95554");
                list.addContact("章三", "95553");
                list.addContact("李四", "97890");
                list.addContact("王五", "91234");
                list.addContact("王二", "90001");
                list.addContact("赵四", "99999");
                CdPhoneManager.getInstance().sendPhoneBookData(list);
                ToastUtils.show("同步联系人通讯录数据");
            }
        }));
    }

    private CdBlueToothManager.BlueToothTool mBlueToothTool = new CdBlueToothManager.BlueToothTool() {
        @Override
        public void openBlueToothView() {
            ToastUtils.show("打开蓝牙页面");
            // 在语音打电话给某某人，DuerOS收到状态为未连接蓝牙时调用，目的是打开蓝牙页面，厂商需要在里面重写实现。
        }

        @Override
        public void openContractDownloadView() {
            ToastUtils.show("打开电话本下载页面");
            // 在语音打电话给某某人，DuerOS收到状态已连接蓝牙，但是为无联系人时调用，目的是打开联系人页面，厂商需要在里面重写实现方法
        }
    };

}
