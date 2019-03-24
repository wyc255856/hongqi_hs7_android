package com.baidu.che.codrivercustom1.util;

import android.widget.Toast;

import com.baidu.che.codrivercustom1.CoDriverCustomApp;

/**
 * 可以连续弹的Toast
 */

public class ToastUtils {

    private static Toast toast;

    public static void show(final String text) {
        //防止在子线程中弹Toast导致应用Crash
        CoDriverCustomApp.runUITask(new Runnable() {
            @Override
            public void run() {
                if (toast == null) {
                    toast = Toast.makeText(CoDriverCustomApp.getInstance(), text+"", Toast.LENGTH_SHORT);
                } else {
                    toast.setText(text);
                }
                toast.show();
            }
        });
    }
}
