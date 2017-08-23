package com.anriku.imcheck.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anriku on 2017/8/20.
 */

public class PermissionUtil {
    private static final int theRequestCode = 0;

    private static PermissionsInterface theInterface;

    private static List<String> permissionsList = new ArrayList<>();

    public static void requestPermissions(Context context, List<String> rawPermissions, PermissionsInterface permissionsInterface){
        theInterface = permissionsInterface;
        for (String permission:rawPermissions){
            if (ContextCompat.checkSelfPermission(context,permission) != PackageManager.PERMISSION_GRANTED){
                permissionsList.add(permission);
            }
        }

        if (!permissionsList.isEmpty()){
            String[] beSolvingPermissions = permissionsList.toArray(new String[permissionsList.size()]);
            ActivityCompat.requestPermissions((Activity) context,beSolvingPermissions,theRequestCode);
        }else {
            permissionsInterface.onDoSomething();
        }
    }


    public static void solvePermissionsResult(int requestCode,int[] grantResults){
        switch (requestCode){
            case theRequestCode:
                if (grantResults.length > 0){
                    for (int result:grantResults){
                        if (result != PackageManager.PERMISSION_GRANTED){
                            theInterface.onSolvePermissionDisAllow();
                            return;
                        }
                    }
                    theInterface.onDoSomething();
                }else {
                    theInterface.onError();
                }
                break;
            default:
                break;
        }
    }

    /**
     * Created by 10393 on 2017/4/7.
     */

    public static interface PermissionsInterface {
        //用于权限请求成功后，自定义要做的事
        void onDoSomething();
        //在用户未允许权限的时候，进行相应的处理
        void onSolvePermissionDisAllow();
        //用于出错时的处理
        void onError();
    }
}
