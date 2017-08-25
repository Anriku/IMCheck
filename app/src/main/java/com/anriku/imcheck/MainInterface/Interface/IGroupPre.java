package com.anriku.imcheck.MainInterface.Interface;

import android.content.Context;

import com.anriku.imcheck.databinding.FragmentGroupBinding;

/**
 * Created by Anriku on 2017/8/22.
 */

public interface IGroupPre {
    void getGroups(Context context, FragmentGroupBinding binding);
    void handleGroupApply(Context context,FragmentGroupBinding binding);
    void reFreshGroups(Context context,FragmentGroupBinding binding);
}
