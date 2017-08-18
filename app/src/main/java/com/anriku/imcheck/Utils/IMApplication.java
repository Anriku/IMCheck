package com.anriku.imcheck.Utils;

import android.app.ActivityManager;
import android.app.Application;
import android.content.pm.PackageManager;
import android.util.Log;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;
import com.wilddog.wilddogcore.WilddogApp;
import com.wilddog.wilddogcore.WilddogOptions;

import java.util.Iterator;
import java.util.List;

/**
 * Created by Anriku on 2017/8/17.
 */

public class IMApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        init();
    }


    private void init() {
        EMOptions options = new EMOptions();
        //取消自动同意
        options.setAcceptInvitationAlways(false);

        int pid = android.os.Process.myPid();
        String processAppName = getAppName(pid);

        if (processAppName == null || !processAppName.equalsIgnoreCase(this.getPackageName())) {
            Log.e("IMApplication", "enter the service process!");
            return;
        }

        EMClient.getInstance().init(this, options);
        EMClient.getInstance().setDebugMode(true);

        //野狗用来存储注册的用户
        WilddogOptions wilddogOptions = new WilddogOptions.Builder().setSyncUrl("https://wd1120523828jodpnz.wilddogio.com").build();
        WilddogApp wilddogApp = WilddogApp.initializeApp(this, wilddogOptions);
    }


    private String getAppName(int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = this.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pID) {
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return processName;
    }

}
