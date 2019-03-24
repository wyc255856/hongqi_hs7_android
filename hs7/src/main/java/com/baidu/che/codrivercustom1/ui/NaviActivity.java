package com.baidu.che.codrivercustom1.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.baidu.che.codrivercustom1.util.ToastUtils;
import com.baidu.che.codrivercustom1.widget.FuncButton;
import com.baidu.che.codriversdk.manager.CdNaviManager;

/**
 * 导航模块
 */

public class NaviActivity extends HS7BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**
         * 接入该功能,必须先执行setNaviTool(...),接入者才能接收到相关回调
         */
        addFunctionBtn(new FuncButton(mContext, "设置导航工具", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CdNaviManager.getInstance().setNaviTool(new CdNaviManager.NaviTool() {
                    @Override
                    public void onReceivedNaviStatus(CdNaviManager.NaviStatus status) {
                        Toast.makeText(mContext, status.name(), Toast.LENGTH_LONG).show();
                    }
                });
                ToastUtils.show("设置导航工具");
            }
        }), new FuncButton(mContext, "设置百度地图为导航app", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CdNaviManager.getInstance().setDefaultNaviApp(CdNaviManager.NaviApp.Baidu);
                ToastUtils.show("设置百度地图为导航app");
            }
        }), new FuncButton(mContext, "设置高德地图后视镜版为导航app", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CdNaviManager.getInstance().setDefaultNaviApp(CdNaviManager.NaviApp.Amap_Lite);
                ToastUtils.show("设置后视镜版高德地图为导航app");
            }
        }), new FuncButton(mContext, "设置车机版高德地图为导航app", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CdNaviManager.getInstance().setDefaultNaviApp(CdNaviManager.NaviApp.Amap);
                ToastUtils.show("设置高德地图车机版为导航app");
            }
        }), new FuncButton(mContext, "发起导航", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CdNaviManager.PoiModel poiModel = new CdNaviManager.PoiModel();
                poiModel.poiName = "";  //POI名称
                poiModel.latitude = 1;  //纬度(必填)
                poiModel.longitude = 2; //经度(必填)
                poiModel.poiAddress = "";   //地址
                boolean status = CdNaviManager.getInstance().sendStartNaviCommand(poiModel);
                ToastUtils.show(status ? "发起导航" : "数据有问题,请重新输入");
            }
        }), new FuncButton(mContext, "退出导航", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CdNaviManager.getInstance().closeMap();
            }
        }));

        addFunctionBtn(new FuncButton(mContext, "设置地图为白天模式", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CdNaviManager.getInstance().setDayOrNightMode(true);
                ToastUtils.show("设置地图为白天模式");
            }
        }), new FuncButton(mContext, "设置地图为黑夜模式", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CdNaviManager.getInstance().setDayOrNightMode(false);
                ToastUtils.show("设置地图为黑夜模式");
            }
        }));
        addFunctionBtn(new FuncButton(mContext, "放大地图", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CdNaviManager.getInstance().zoomMap(true);
                ToastUtils.show("放大地图");
            }
        }), new FuncButton(mContext, "缩小地图", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CdNaviManager.getInstance().zoomMap(false);
                ToastUtils.show("缩小地图");
            }
        }), new FuncButton(mContext, "是否导航中", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CdNaviManager.getInstance().isInNavi(new CdNaviManager.IsNaviCallback() {
                    @Override
                    public void isInNavi(boolean isNavi) {
                        ToastUtils.show(isNavi ? "正在导航" : "没有导航");
                    }
                });
            }
        }));

        //尝试触发设置或取消地图为黑夜模式，
        //当地图为自动模式时生效（如果强制设置了地图为白天/黑夜模式，此方法不生效）
        addTitle(20, "尝试触发设置或取消地图为黑夜模式", null);

        addFunctionBtn(new FuncButton(mContext, "触发黑夜模式", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CdNaviManager.getInstance().triggerNightMode(true);
                ToastUtils.show("触发黑夜模式");
            }
        }), new FuncButton(mContext, "取消黑夜模式", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CdNaviManager.getInstance().triggerNightMode(false);
                ToastUtils.show("取消黑夜模式");
            }
        }));

        addFunctionBtn(new FuncButton(mContext, "设置家的位置", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CdNaviManager.PoiModel address = new CdNaviManager.PoiModel();
                address.poiName = "宝安";
                address.type = CdNaviManager.AddressType.home.name();
                address.poiAddress = "坪洲";
                address.longitude = 112;
                address.latitude = 28;
                CdNaviManager.getInstance().setAppointAddress(address);
            }
        }), new FuncButton(mContext, "设置公司位置", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CdNaviManager.PoiModel address = new CdNaviManager.PoiModel();
                address.poiName = "南山";
                address.type = CdNaviManager.AddressType.office.name();
                address.poiAddress = "百度";
                address.longitude = 116;
                address.latitude = 40;
                CdNaviManager.getInstance().setAppointAddress(address);
            }
        }), new FuncButton(mContext, "获取家的位置", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CdNaviManager.getInstance().getAppointAddress(CdNaviManager.AddressType.home,
                        new CdNaviManager.AddressCallback() {
                            @Override
                            public void onResultAddress(CdNaviManager.PoiModel address) {
                                if (address != null) {
                                    ToastUtils.show("家的地址 = " + address.poiName);
                                }
                            }
                        });
            }
        }), new FuncButton(mContext, "获取公司位置", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CdNaviManager.getInstance().getAppointAddress(CdNaviManager.AddressType.office,
                        new CdNaviManager.AddressCallback() {
                            @Override
                            public void onResultAddress(CdNaviManager.PoiModel address) {
                                if (address != null) {
                                    ToastUtils.show("公司的地址 = " + address.poiName);
                                }
                            }
                        });
            }
        }));

        addFunctionBtn(new FuncButton(mContext, "导航去家", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CdNaviManager.getInstance().sendStartNaviHomeOrAddress(CdNaviManager.AddressType.home);
            }
        }), new FuncButton(mContext, "导航去公司", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CdNaviManager.getInstance().sendStartNaviHomeOrAddress(CdNaviManager.AddressType.office);
            }
        }));
    }

}
