package com.anriku.imcheck.MainInterface.Interface.GroupSet;

import android.content.Context;

import com.anriku.imcheck.databinding.ActivityGroupModifyBinding;

/**
 * Created by Anriku on 2017/8/23.
 */

public interface IGroupModifyPre {

    void setNameAndDesc(Context context, ActivityGroupModifyBinding binding,String obj);

    void changeNameAndDesc(Context context, ActivityGroupModifyBinding binding,String obj);
}
