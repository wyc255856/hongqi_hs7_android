package com.baidu.che.codrivercustom1.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.che.codrivercustom1.util.CommonUtils;
import com.baidu.che.codrivercustom1.widget.FuncButton;
import com.wyc.hs7.R;

/**
 * 基类Activity
 */

public class HS7BaseActivity extends Activity {

    protected Context mContext;
    protected LinearLayout mLLContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_common);
//
//        mContext = this;
//        Intent intent = getIntent();
//        String title = "";
//        if (intent != null) {
//            title = intent.getStringExtra("title");
//        }
//        if (TextUtils.isEmpty(title)) {
//            title = "示例demo";
//        }
//        ((TextView) findViewById(R.id.txt_title)).setText(title);
//        mLLContent = (LinearLayout) findViewById(R.id.ll_content);
//        findViewById(R.id.img_back).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent();
//// 为Intent设置Action、Category属性
//                intent.setAction(Intent.ACTION_MAIN);// "android.intent.action.MAIN"
//                intent.addCategory(Intent.CATEGORY_HOME); //"android.intent.category.HOME"
//               startActivity(intent);
//            }
//        });
    }

    protected void addFunctionBtn(FuncButton... funcButtons) {
        LinearLayout linearLayout = new LinearLayout(mContext);
        mLLContent.addView(linearLayout);

        int screenWidth = CommonUtils.getScreenWidth(mContext);
        LinearLayout.LayoutParams lp =
                new LinearLayout.LayoutParams(screenWidth / funcButtons.length - 20,
                        ViewGroup.LayoutParams.WRAP_CONTENT);

        for (int i = 0; i < funcButtons.length; i++) {
            FuncButton btn = funcButtons[i];
            FrameLayout fm = new FrameLayout(mContext);
            fm.setPadding(10, 10, 10, 10);
            fm.addView(btn, lp);
            linearLayout.addView(fm);
        }
    }

    /**
     * 添加标题
     *
     * @param txtSize
     * @param name
     * @param params
     */
    protected void addTitle(int txtSize, String name, LinearLayout.LayoutParams params) {
        TextView textView = new TextView(mContext);
        textView.setTextSize(txtSize);
        textView.setText(name);
        if (params != null) {
            mLLContent.addView(textView, params);
        } else {
            LinearLayout.LayoutParams lp =
                    new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.gravity = Gravity.CENTER;
            mLLContent.addView(textView, lp);
        }
    }
}
