package com.anriku.imcheck.MainInterface.View;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.anriku.imcheck.MainInterface.Interface.ISelectGroupAct;
import com.anriku.imcheck.MainInterface.Interface.ISelectGroupPre;
import com.anriku.imcheck.MainInterface.Presenter.SelectGroupPresenter;
import com.anriku.imcheck.R;
import com.anriku.imcheck.databinding.ActivitySelectGroupBinding;

public class SelectGroupActivity extends AppCompatActivity implements ISelectGroupAct{

    private ActivitySelectGroupBinding binding;
    private ISelectGroupPre iSelectGroupPre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_select_group);

        iSelectGroupPre = new SelectGroupPresenter(this);
        iSelectGroupPre.createGroup(this,binding);
    }
}
