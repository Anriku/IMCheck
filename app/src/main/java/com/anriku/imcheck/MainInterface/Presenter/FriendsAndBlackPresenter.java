package com.anriku.imcheck.MainInterface.Presenter;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.anriku.imcheck.Adapter.IMCheckVPAdapter;
import com.anriku.imcheck.MainInterface.Interface.IFriendsAndBlackPre;
import com.anriku.imcheck.MainInterface.Interface.IFriendsAndBlackFrg;
import com.anriku.imcheck.MainInterface.View.BlackFragment;

import com.anriku.imcheck.MainInterface.View.FriendsFragment;
import com.anriku.imcheck.MainInterface.View.GroupFragment;

import com.anriku.imcheck.R;
import com.anriku.imcheck.databinding.FragmentFriendsAndBlackBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anriku on 2017/8/22.
 */

public class FriendsAndBlackPresenter implements IFriendsAndBlackPre {

    private IFriendsAndBlackFrg iFriendsAndBlackFrg;

    public FriendsAndBlackPresenter(IFriendsAndBlackFrg iFriendsAndBlackFrg) {
        this.iFriendsAndBlackFrg = iFriendsAndBlackFrg;
    }


    @Override
    public void setTaLayout(Context context, final FragmentManager fragmentManager, FragmentFriendsAndBlackBinding binding) {
        String[] titles = {"联系人", "群","黑名单"};
        final List<Fragment> fragments = new ArrayList<>();
        fragments.add(new FriendsFragment());
        fragments.add(new GroupFragment());
        fragments.add(new BlackFragment());

        for (String title : titles) {
            binding.frgFriendsAndBlackTl.addTab(binding.frgFriendsAndBlackTl.newTab().setText(title));
        }

        replaceFragment(fragments.get(0),fragmentManager);

        binding.frgFriendsAndBlackTl.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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

    private void replaceFragment(Fragment fragment,FragmentManager manager) {
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.frg_friends_and_black_fl,fragment);
        transaction.commit();
    }
}
