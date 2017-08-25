package com.anriku.imcheck.MainInterface.Presenter;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.anriku.imcheck.MainInterface.Interface.IMainInterfaceAct;
import com.anriku.imcheck.MainInterface.Interface.IMainInterfacePre;
import com.anriku.imcheck.MainInterface.View.ChatFragment;
import com.anriku.imcheck.MainInterface.View.FriendsAndBlackFragment;
import com.anriku.imcheck.R;
import com.anriku.imcheck.databinding.ActivityMainInterfaceBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anriku on 2017/8/17.
 */

public class MainInterfacePresenter implements IMainInterfacePre {

    private IMainInterfaceAct iMainInterfaceAct;

    public MainInterfacePresenter(IMainInterfaceAct iMainInterfaceAct) {
        this.iMainInterfaceAct = iMainInterfaceAct;
    }


    @Override
    public void setFragments(final Context context, final FragmentManager fragmentManager, final ActivityMainInterfaceBinding binding) {
        String[] titles = {"消息", "联系人"};
        int[] images = {R.drawable.chat, R.drawable.friends};
        final List<Fragment> fragments = new ArrayList<>();
        fragments.add(new ChatFragment());
        fragments.add(new FriendsAndBlackFragment());

        //设置图标文字
        for (int i = 0; i < titles.length; i++) {
            binding.acMainInterfaceTl.addTab(binding.acMainInterfaceTl.newTab().setText(titles[i]).setIcon(images[i]));
        }

        replaceFragment(fragments.get(0), fragmentManager);

        binding.acMainInterfaceTl.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                replaceFragment(fragments.get(tab.getPosition()),fragmentManager);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void setNickName(Context context, ActivityMainInterfaceBinding binding) {

    }

    private void replaceFragment(Fragment fragment, FragmentManager manager) {
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.ac_main_interface_fl, fragment);
        transaction.commit();
    }

}
