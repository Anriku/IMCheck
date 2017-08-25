package com.anriku.imcheck.MainInterface.Interface;

import android.content.Context;

import com.anriku.imcheck.databinding.FragmentBlackBinding;

/**
 * Created by Anriku on 2017/8/22.
 */

public interface IBlackPre {
    void refreshBlackList(Context context,FragmentBlackBinding binding);
    void getBlackList(Context context,FragmentBlackBinding binding);
}
