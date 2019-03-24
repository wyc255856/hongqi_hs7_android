package com.baidu.che.codrivercustom1.ui;

import android.os.Bundle;
import android.view.View;

import com.baidu.che.codrivercustom1.util.ToastUtils;
import com.baidu.che.codrivercustom1.widget.FuncButton;
import com.baidu.che.codriversdk.manager.CdAsrManager;
import com.baidu.che.codriversdk.manager.CdConfigManager;
import com.baidu.che.codriversdk.manager.CdMediaManager;
import com.baidu.che.codriversdk.manager.CdPlayerManager;
import com.baidu.che.codriversdk.manager.CdRecordManager;
import com.baidu.che.codriversdk.manager.CdTTSPlayerManager;

/**
 * 多媒体管理类
 */

public class MediaManagerActivity extends HS7BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**
         * 必须先执行setMediaTool(...)/setRecordTool(...)/setPlayerTool(...),接入者才能接收到相关回调
         */
        addFunctionBtn(new FuncButton(mContext, "设置多媒体工具", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CdMediaManager.getInstance().setMediaTool(mMediaTool);
                ToastUtils.show("设置多媒体工具");
            }
        }), new FuncButton(mContext, "设置录音控制工具", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CdRecordManager.getInstance().setRecordTool(mRecordTool);
                ToastUtils.show("设置录音控制工具");
            }
        }), new FuncButton(mContext, "设置本地播放工具", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CdPlayerManager.getInstance().setPlayerTool(mPlayerTool);
                ToastUtils.show("设置本地播放工具");
            }
        }));

        addFunctionBtn(new FuncButton(mContext, "通知DuerOS当前正在播放的播放器类型", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //百度随心听  酷我  QQ  customer
                CdPlayerManager.getInstance().notifyInUse(CdConfigManager.MusicType.CUSTOM_MUSIC);
            }
        }));

        addFunctionBtn(new FuncButton(mContext, "触发播放，切歌等操作的时候，是否拉起播放器到前台", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //拉起播放器 true  反之false
                CdPlayerManager.getInstance().notifyNeedLaunchApp(true);
            }
        }));

        addFunctionBtn(new FuncButton(mContext, "输送录音数据", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //数组长度根据实际情况确定
                byte[] rawData = new byte[1024];
                CdRecordManager.getInstance().feedRawAudioBuffer(rawData);
                ToastUtils.show("输送录音数据");
            }
        }), new FuncButton(mContext, "输送mic信号和spk信号录音数据", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //数组长度根据实际情况确定
                byte[] micData = new byte[1024];
                byte[] spkData = new byte[1024];
                CdRecordManager.getInstance().feedAudioBuffer(micData, spkData);
                ToastUtils.show("输送mic信号和spk信号录音数据");
            }
        }));

        addTitle(20, "设置AEC和录音相关的特性", null);

        addFunctionBtn(new FuncButton(mContext, "MIC_LEFT", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //目前支持的RecordType, 根据需要填写
                //内部录音,无AEC，单声道，只有Mic信号    INSIDE_RAW,
                //内部录音,有AEC，左声道是Mic，右声道是Speaker   INSIDE_AEC_MIC_LEFT,
                //内部录音,有AEC，右声道是Mic，左声道是Speaker   INSIDE_AEC_MIC_RIGHT,
                //外部录音,无AEC，单声道，只有Mic信号   OUTSIDE_RAW,
                //外部录音,有AEC，左声道是Mic，右声道是Speaker   OUTSIDE_AEC_MIC_LEFT,
                //外部录音,有AEC，右声道是Mic，左声道是Speaker   OUTSIDE_AEC_MIC_RIGHT,
                //外部录音,有AEC，厂商自己分离Mic和Speaker信号   OUTSIDE_AEC_DUAL_CHANNEL
                CdRecordManager.getInstance().
                        setRecordType(CdRecordManager.RecordType.INSIDE_AEC_MIC_LEFT);
                ToastUtils.show("设置type = MIC_LEFT");
            }
        }), new FuncButton(mContext, "MIC_RIGHT", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CdRecordManager.getInstance().
                        setRecordType(CdRecordManager.RecordType.INSIDE_AEC_MIC_RIGHT);
                ToastUtils.show("设置type = MIC_RIGHT");
            }
        }));

    }

    private CdPlayerManager.PlayerTool mPlayerTool = new CdPlayerManager.PlayerTool() {

        @Override
        public void play() {
            ToastUtils.show("播放");
            CdAsrManager.getInstance().closeDialog();
        }

        @Override
        public void pause() {
            ToastUtils.show("暂停");
            CdAsrManager.getInstance().closeDialog();
        }

        @Override
        public void prev() {
            ToastUtils.show("上一首");
            CdAsrManager.getInstance().closeDialog();
        }

        @Override
        public void next() {
            ToastUtils.show("下一首");
            CdAsrManager.getInstance().closeDialog();
        }

        @Override
        public void exit() {
            ToastUtils.show("退出");
            CdAsrManager.getInstance().closeDialog();
        }

        @Override
        public void switchMode(int mode) {
            ToastUtils.show("切换播放模式");
            CdAsrManager.getInstance().closeDialog();
            //mode对应的值:
            //单曲循环  PlayerTool.MODE_SINGLE_LOOP
            //随机播放  PlayerTool.MODE_RANDOM
            //循环播放  PlayerTool.MODE_FULL_LOOP
        }

        @Override
        public void change() {
            ToastUtils.show("换一首");
            CdAsrManager.getInstance().closeDialog();
        }
    };

    private CdRecordManager.RecordTool mRecordTool = new CdRecordManager.RecordTool() {
        @Override
        public void startRecord() {
            ToastUtils.show("开始录音");
        }

        @Override
        public void endRecord() {
            ToastUtils.show("结束录音");
        }

        @Override
        public void initRecorder() {
            ToastUtils.show("录音初始化");
        }
    };

    private CdMediaManager.MediaTool mMediaTool = new CdMediaManager.MediaTool() {
        //下面所有的方法中,加入CdAsrManager.getInstance().closeDialog();
        //例:openRadio();
        @Override
        public void openRadio() {
            ToastUtils.show("打开电台");
            CdAsrManager.getInstance().closeDialog();
        }

        @Override
        public void closeRadio() {
            ToastUtils.show("关闭电台");
        }

        @Override
        public void openFM() {
            ToastUtils.show("打开FM");
        }

        @Override
        public void openFMChannel(String s) {
            ToastUtils.show("打开指定频率的FM" + s);
        }

        @Override
        public void openAM() {
            ToastUtils.show("打开AM");
        }

        @Override
        public void openAMChannel(String s) {
            ToastUtils.show("打开指定频率的AM" + s);
        }

        @Override
        public void openMusicUsb() {
            ToastUtils.show("打开USB音乐");
        }

        @Override
        public void openMusicCd() {
            ToastUtils.show("打开CD音乐");
        }

        @Override
        public void openMusicAux() {
            ToastUtils.show("打开AUX音乐");
        }

        @Override
        public void openMusicIpod() {
            ToastUtils.show("打开ipod音乐");
        }

        @Override
        public void openMusicBt() {
            ToastUtils.show("打开蓝牙音乐");
        }

        @Override
        public void openMyMusic() {
            ToastUtils.show("打开我的音乐");
        }

        @Override
        public void playCollectionFM() {
            ToastUtils.show("播放收藏的电台");
        }

        @Override
        public void collectFMChannel() {
            ToastUtils.show("收藏当前电台");
            // 放FM打开状态查询的回调中
            if (true) {  // FM已经打开
                // 执行收藏的操作,厂商实现
                // 放收藏操作的回调中
                if (true) {
                    CdTTSPlayerManager.getInstance().play("好的,收藏了");
                } else {
                    CdTTSPlayerManager.getInstance().play("收藏电台失败");
                }
            } else {  // FM未打开
                CdTTSPlayerManager.getInstance().play("FM未打开,暂不支持该指令");
            }
        }

        @Override
        public void cancelFMChannel() {
            ToastUtils.show("取消收藏当前电台");
            // 放FM打开状态查询的回调中
            if (true) {  // FM已经打开
                // 执行取消收藏的操作,厂商实现
                // 放取消收藏操作的回调中
                if (true) {
                    CdTTSPlayerManager.getInstance().play("好的,已取消收藏");
                } else {
                    CdTTSPlayerManager.getInstance().play("取消收藏收藏电台失败");
                }
            } else {  // FM未打开
                CdTTSPlayerManager.getInstance().play("FM未打开,暂不支持该指令");
            }
        }

        @Override
        public void searchAndRefreshFMChannel() {
            ToastUtils.show("更新电台列表");
        }
    };
}
