package com.anriku.imcheck.Utils;

import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

/**
 * Created by Anriku on 2017/8/8.
 */

public class PopupWindowUtil {

    private int resource;//资源文件
    private int animationStyle;//下面出来的框的动画文件
    private Window window;//获取一个Window对象来调Window属性
    private PopupWindow popupWindow;
    private float alpha = 1f;
    private LayoutInflater inflater;
    private FrameLayout frameLayout;//用于返回后后面好调用布局文件的控件
    private static PopupWindowUtil popupWindowUtil;
    public static final int BOTTOM = 0;
    public static final int CENTER = 1;
    private static int local;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    backgroudAlpha((float) msg.obj);
                    break;
                default:
                    break;
            }
        }
    };


    private PopupWindowUtil(int resource, int animationStyle, LayoutInflater inflater, Window window) {
        this.resource = resource;
        this.animationStyle = animationStyle;
        this.inflater = inflater;
        this.window = window;
        frameLayout = (FrameLayout) inflater.inflate(resource, null);
    }

    //使用单例模式防止不停按button不停建,开始一直不知道为什么会重建，怎了好久!!!555
    public static synchronized PopupWindowUtil getInstance(int resource, int animationStyle, LayoutInflater inflater, Window window, int location) {
        local = location;
//        if (popupWindowUtil == null) {
//            popupWindowUtil = new PopupWindowUtil(resource, animationStyle, inflater, window);
//        }
        return new PopupWindowUtil(resource, animationStyle, inflater, window);
    }

    public PopupWindow getPopupWindow() {
        return popupWindow;
    }

    public FrameLayout getFrameLayout() {
        return frameLayout;
    }

    public void bottomWindow(View view) {
        if (popupWindow != null && popupWindow.isShowing()) {
            return;
        }

        popupWindow = new PopupWindow(frameLayout, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setFocusable(false);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setAnimationStyle(animationStyle);
        //特别重要,定位置
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        if (local == BOTTOM) {
            popupWindow.showAtLocation(view, Gravity.LEFT | Gravity.BOTTOM, 0, -location[1]);
        } else {
            popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        }
        popupWindow.setOnDismissListener(new PopupDismissListener());

        //用于实现渐变效果
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (alpha > 0.5f) {
                    try {
                        Thread.sleep(3);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    Message msg = handler.obtainMessage();
                    msg.what = 1;
                    alpha -= 0.01f;
                    msg.obj = alpha;
                    handler.sendMessage(msg);
                }
            }
        }).start();
    }

    private void backgroudAlpha(float obj) {
        WindowManager.LayoutParams lp = window.getAttributes();
        alpha = lp.alpha = obj;
        window.setAttributes(lp);
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }

    class PopupDismissListener implements PopupWindow.OnDismissListener {

        @Override
        public void onDismiss() {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (alpha < 1f) {
                        try {
                            Thread.sleep(2);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        Message msg = handler.obtainMessage();
                        msg.what = 1;
                        alpha += 0.01f;
                        msg.obj = alpha;
                        handler.sendMessage(msg);
                    }
                }
            }).start();
            popupWindowUtil = null;
        }
    }
}
