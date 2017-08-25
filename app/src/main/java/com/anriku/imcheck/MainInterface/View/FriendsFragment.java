package com.anriku.imcheck.MainInterface.View;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.anriku.imcheck.MainInterface.Interface.IFriendsFrg;
import com.anriku.imcheck.MainInterface.Interface.IFriendsPre;
import com.anriku.imcheck.MainInterface.Presenter.FriendsPresenter;
import com.anriku.imcheck.R;
import com.anriku.imcheck.databinding.FragmentFriendsBinding;
import com.hyphenate.EMContactListener;
import com.hyphenate.chat.EMClient;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anriku on 2017/8/16.
 */

public class FriendsFragment extends Fragment implements IFriendsFrg{

    private IFriendsPre iFriendsPre;
    private FragmentFriendsBinding binding;
    private List<String> names = new ArrayList<>();
    private List<String> reasons = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_friends,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        iFriendsPre = new FriendsPresenter(this);
        iFriendsPre.getFriends(getContext(),binding);
        iFriendsPre.handleApply(getContext(),names,reasons,binding);
        iFriendsPre.refreshFriends(getContext(),binding);
    }

}
