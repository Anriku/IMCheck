package com.anriku.imcheck.MainInterface.Interface;

import android.content.Context;

import com.anriku.imcheck.databinding.ActivityGroupMoreBinding;

/**
 * Created by Anriku on 2017/8/22.
 */

public interface IGroupMorePre {

    void adminsSet(Context context, ActivityGroupMoreBinding binding,String obj);

    void exitOrDissolveGroup(Context context,ActivityGroupMoreBinding binding,String obj);

}
