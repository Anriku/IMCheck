package com.anriku.imcheck.MainInterface.Presenter;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;

import com.anriku.imcheck.Adapter.IMCheckVPAdapter;
import com.anriku.imcheck.MainInterface.Interface.IMainInterfaceAct;
import com.anriku.imcheck.MainInterface.Interface.IMainInterfacePre;
import com.anriku.imcheck.MainInterface.View.ChatFragment;
import com.anriku.imcheck.MainInterface.View.DynamicsFragment;
import com.anriku.imcheck.MainInterface.View.FriendsFragment;
import com.anriku.imcheck.databinding.ActivityMainInterfaceBinding;
import com.hyphenate.EMContactListener;
import com.hyphenate.chat.EMClient;

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
    public void setFragments(FragmentManager fragmentManager, ActivityMainInterfaceBinding binding) {
        String[] titles = {"消息", "联系人", "动态"};
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new ChatFragment());
        fragments.add(new FriendsFragment());
        fragments.add(new DynamicsFragment());
        IMCheckVPAdapter adapter = new IMCheckVPAdapter(fragmentManager, fragments, titles);
        binding.acMainInterfaceVp.setAdapter(adapter);
        for (String title : titles) {
            binding.acMainInterfaceTl.addTab(binding.acMainInterfaceTl.newTab().setText(title));
        }
        binding.acMainInterfaceTl.setupWithViewPager(binding.acMainInterfaceVp);
    }
}
