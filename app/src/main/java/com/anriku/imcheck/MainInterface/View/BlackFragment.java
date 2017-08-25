package com.anriku.imcheck.MainInterface.View;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anriku.imcheck.MainInterface.Interface.IBlackFrg;
import com.anriku.imcheck.MainInterface.Interface.IBlackPre;
import com.anriku.imcheck.MainInterface.Presenter.BlackPresenter;
import com.anriku.imcheck.R;
import com.anriku.imcheck.databinding.FragmentBlackBinding;

/**
 * Created by Anriku on 2017/8/22.
 */

public class BlackFragment extends Fragment implements IBlackFrg {

    private FragmentBlackBinding binding;
    private IBlackPre iBlackPre;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_black,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        iBlackPre = new BlackPresenter(this);

        iBlackPre.refreshBlackList(getContext(),binding);
        iBlackPre.getBlackList(getContext(),binding);
    }
}
