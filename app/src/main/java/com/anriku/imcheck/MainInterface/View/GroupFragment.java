package com.anriku.imcheck.MainInterface.View;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anriku.imcheck.MainInterface.Interface.IGroupFrg;
import com.anriku.imcheck.MainInterface.Interface.IGroupPre;
import com.anriku.imcheck.MainInterface.Presenter.GroupPresenter;
import com.anriku.imcheck.R;
import com.anriku.imcheck.databinding.FragmentGroupBinding;

/**
 * Created by Anriku on 2017/8/22.
 */

public class GroupFragment extends Fragment implements IGroupFrg{

    private FragmentGroupBinding binding;
    private IGroupPre iGroupPre;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_group,container,false);
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        iGroupPre.getGroups(getContext(),binding);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        iGroupPre = new GroupPresenter(this);

        iGroupPre.reFreshGroups(getContext(),binding);
        iGroupPre.getGroups(getContext(),binding);
        iGroupPre.handleGroupApply(getContext(),binding);
    }
}
