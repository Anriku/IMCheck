package com.anriku.imcheck.MainInterface.Interface;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;

import com.anriku.imcheck.databinding.ActivityMainInterfaceBinding;

/**
 * Created by Anriku on 2017/8/17.
 */

public interface IMainInterfacePre {

    void setFragments(Context context,FragmentManager fragmentManager, ActivityMainInterfaceBinding binding);

    void setNickName(Context context,ActivityMainInterfaceBinding binding);
}
