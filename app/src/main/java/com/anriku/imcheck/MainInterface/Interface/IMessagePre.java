package com.anriku.imcheck.MainInterface.Interface;

import android.content.Context;

import com.anriku.imcheck.databinding.ActivityMessageBinding;

/**
 * Created by Anriku on 2017/8/18.
 */

public interface IMessagePre {
    void chat(Context context,String obj, ActivityMessageBinding binding);
    void getHistory(Context context,String obj,ActivityMessageBinding binding);
}
