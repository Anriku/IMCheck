package com.anriku.imcheck.MainInterface.View;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anriku.imcheck.MainInterface.Interface.IFriendsAndBlackPre;
import com.anriku.imcheck.MainInterface.Interface.IFriendsAndBlackFrg;
import com.anriku.imcheck.MainInterface.Presenter.FriendsAndBlackPresenter;
import com.anriku.imcheck.R;
import com.anriku.imcheck.databinding.FragmentFriendsAndBlackBinding;

/**
 * Created by Anriku on 2017/8/22.
 */

public class FriendsAndBlackFragment extends Fragment implements IFriendsAndBlackFrg{

    private FragmentFriendsAndBlackBinding binding;
    private IFriendsAndBlackPre iFriendsAndBlackPre;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_friends_and_black,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        iFriendsAndBlackPre = new FriendsAndBlackPresenter(this);

        iFriendsAndBlackPre.setTaLayout(getContext(),getFragmentManager(),binding);
    }
}
