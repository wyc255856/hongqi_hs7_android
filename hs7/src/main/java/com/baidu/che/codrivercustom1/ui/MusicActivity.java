package com.baidu.che.codrivercustom1.ui;

import android.os.Bundle;
import android.view.View;

import com.baidu.che.codrivercustom1.util.ToastUtils;
import com.baidu.che.codrivercustom1.widget.FuncButton;
import com.baidu.che.codriversdk.manager.CdAsrManager;
import com.baidu.che.codriversdk.manager.CdConfigManager;
import com.baidu.che.codriversdk.manager.CdMusicManager;

import java.util.ArrayList;
import java.util.List;

/**
 * 音乐模块
 */

public class MusicActivity extends HS7BaseActivity {

    private String[] names = {"吻别", "红玫瑰", "十年", "笨小孩", "江南", "一千年以后", "老街", "成都", "朋友", "后来"};
    private String[] singers = {"张学友", "陈奕迅", "陈奕迅", "刘德华", "林俊杰", "林俊杰", "李荣浩", "赵雷", "周华健", "刘若英"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**
         * 接入该功能,必须先执行setMusicTool(...),接入者才能接收到相关回调
         */
        addFunctionBtn(new FuncButton(mContext, "设置音乐工具实例", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CdMusicManager.getInstance().setMusicTool(mMusicTool);
                ToastUtils.show("设置音乐工具实例");
            }
        }));

        addTitle(20, "设置默认播放器", null);

        addFunctionBtn(new FuncButton(mContext, "百度随心听", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CdConfigManager.getInstance().setMusicType(CdConfigManager.MusicType.BAIDU_RADIO);
                ToastUtils.show("设置默认播放器: 百度随心听");
            }
        }), new FuncButton(mContext, "Customer", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CdConfigManager.getInstance().setMusicType(CdConfigManager.MusicType.CUSTOM_MUSIC);
                ToastUtils.show("设置默认播放器: Customer");
            }
        }), new FuncButton(mContext, "酷我", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CdConfigManager.getInstance().setMusicType(CdConfigManager.MusicType.KUWO_MUSIC);
                ToastUtils.show("设置默认播放器: 酷我");
            }
        }), new FuncButton(mContext, "QQ", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CdConfigManager.getInstance().setMusicType(CdConfigManager.MusicType.QQ_MUSIC);
                ToastUtils.show("设置默认播放器: QQ");
            }
        }));

        addFunctionBtn(new FuncButton(mContext, "同步音乐列表", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<CdMusicManager.MusicModel> list = new ArrayList<>();
                for (int i = 0; i < names.length; i++) {
                    CdMusicManager.MusicModel musicModel = new CdMusicManager.MusicModel();
                    musicModel.name = names[i];
                    musicModel.albumArtistName = singers[i];
                    list.add(musicModel);
                }
                CdMusicManager.getInstance().syncLocalMusicData(list);
            }
        }));
    }

    private CdMusicManager.MusicTool mMusicTool = new CdMusicManager.MusicTool() {
        @Override
        public void playMusic(CdMusicManager.MusicModel musicModel) {
            ToastUtils.show("播放搜索到的音乐: " + musicModel);
        }

        @Override
        public void playList(List<CdMusicManager.MusicModel> list, int pos) {
            ToastUtils.show("将搜索到的音乐列表传给DuerOS:" + list.toString());
        }

        @Override
        public void searchMusic(CdMusicManager.MusicModel musicModel,
                                CdMusicManager.OnSearchResultListener onSearchResultListener) {
            // 可以根据musicModel信息直接发起播放，也可以把搜索结果返回给DuerOS语音模块
            CdAsrManager.getInstance().closeDialog();
            ToastUtils.show("搜索音乐:" + musicModel);
        }
    };
}
