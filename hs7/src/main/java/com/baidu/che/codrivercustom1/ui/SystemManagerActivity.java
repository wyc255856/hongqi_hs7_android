package com.baidu.che.codrivercustom1.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.baidu.che.codrivercustom1.util.ToastUtils;
import com.baidu.che.codrivercustom1.widget.FuncButton;
import com.baidu.che.codriversdk.manager.CdCameraManager;
import com.baidu.che.codriversdk.manager.CdSystemManager;

/**
 * 系统控制
 */

public class SystemManagerActivity extends HS7BaseActivity {
    private static final String TAG = "SystemManagerActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addFunctionBtn(new FuncButton(mContext, "跳转到语音设置界面", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CdSystemManager.getInstance().jumpToAsrSetting();
            }
        }), new FuncButton(mContext, "跳转到帮助设置界面", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CdSystemManager.getInstance().jumpToHelpSetting();
            }
        }));

        /**
         * 必须先执行setRecorderTool(...)/setSystemTool(...), 接入者才能接收到相关回调
         */
        addFunctionBtn(new FuncButton(mContext, "设置行车记录仪控制工具", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CdCameraManager.getInstance().setCameraTool(mRecordTool);
                ToastUtils.show("设置行车记录仪控制工具");
            }
        }), new FuncButton(mContext, "设置系统控制工具", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CdSystemManager.getInstance().setSystemTool(mSystemTool);
                ToastUtils.show("设置系统控制工具");
            }
        }));

        addFunctionBtn(new FuncButton(mContext, "设置车辆数据回调", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CdSystemManager.getInstance().setCarStateListener(new CdSystemManager.CarStateListener() {
                    @Override
                    public void carStateCmd(String feature, String extra) {
                        if (TextUtils.equals(feature, CdSystemManager.CarState.AIR_TEMPERATURE.name())) {
                            // 需要把空调数据返回给DuerOS
                            ToastUtils.show("DuerOS需要空调温度");
                            String data = "";
                            // data 格式如下, code值类型:String
                            // {code: AIR_TEMPERATURE,
                            //  ext: {
                            //         ext1: value1,
                            //         ext2: value2
                            //        }
                            // }
                            CdSystemManager.getInstance().sendCarInfo(data);
                        } else if (TextUtils.equals(feature, CdSystemManager.CarState.FUEL_OIL.name())) {
                            // 需要把燃油数据返回给DuerOS
                            ToastUtils.show("DuerOS需要燃油数据");

                        }
                    }
                });
            }
        }));

    }

    private CdCameraManager.DrivingRecorderTool mRecordTool = new CdCameraManager.DrivingRecorderTool() {
        @Override
        public void drivingRecorder(CdCameraManager.DrivingRecorderTool.DrivingRecorderState drivingRecorderState) {
            //操作录像
            switch (drivingRecorderState) {
                case WATCH:
                    ToastUtils.show("查看录像");
                    break;
                case START:
                    ToastUtils.show("开始录像");
                    break;
                case STOP:
                    ToastUtils.show("停止录像");
                    break;
                default:
                    break;
            }
        }

        @Override
        public void openCamera(CameraType cameraType) {
            //打开摄像头
            switch (cameraType) {
                case FRONT_CAMERA:
                    ToastUtils.show("前置摄像头");
                    break;
                case INNER_CAMERA:
                    ToastUtils.show("内置摄像头");
                    break;
                case BACK_CAMERA:
                    ToastUtils.show("后置摄像头");
                    break;
                default:
                    break;
            }
        }

        @Override
        public void takePicture() {
            ToastUtils.show("拍照");
        }
    };

    private CdSystemManager.SystemTool mSystemTool = new CdSystemManager.SystemTool() {
        // 在下面所有的回调方法内,如果厂商已经实现对相应功能的支持,则return true
        @Override
        public boolean closeFeature(String s, String value) {
            // 关闭某个特性     s =  蓝牙 车灯 wifi ...
            ToastUtils.show("closeFeature : " + s + ", value = " + value);
            return true;
        }

        @Override
        public boolean openFeature(String s, String value) {
            ToastUtils.show("openFeature : " + s + ", value = " + value);
            return true;
        }

        @Override
        public boolean increaseFeature(String s, String value) {
            //放大特性：音量，亮度等:up,down/max,min
            ToastUtils.show("increaseFeature : " + s + ", value = " + value);
            return true;
        }

        @Override
        public boolean reduceFeature(String s, String value) {
            //减少特性：音量，亮度等:up,down/max,min
            ToastUtils.show("reduceFeature : " + s + ", value = " + value);
            return true;
        }

        @Override
        public boolean maxFeature(String s, String value) {
            //最大化特性：音量，亮度等:up,down/max,min
            ToastUtils.show("maxFeature : " + s + ", value = " + value);
            return true;
        }

        @Override
        public boolean minFeature(String s, String value) {
            //最小化特性：音量，亮度等:up,down/max,min
            ToastUtils.show("minFeature : " + s + ", value = " + value);
            return true;
        }

        @Override
        public boolean operateFeature(String name, String value) {
            //调整某个特性到某个值：调节空调，调节风量等
            ToastUtils.show("operateFeature : " + name + ", value : " + value);
            return true;
        }

    };
}
