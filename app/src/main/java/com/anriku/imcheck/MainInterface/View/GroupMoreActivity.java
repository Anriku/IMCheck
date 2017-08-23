package com.anriku.imcheck.MainInterface.View;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.anriku.imcheck.MainInterface.Interface.IGroupMoreAct;
import com.anriku.imcheck.MainInterface.Interface.IGroupMorePre;
import com.anriku.imcheck.MainInterface.Presenter.GroupMorePresenter;
import com.anriku.imcheck.R;
import com.anriku.imcheck.databinding.ActivityGroupMoreBinding;

public class GroupMoreActivity extends AppCompatActivity implements IGroupMoreAct {

    private IGroupMorePre iGroupMorePre;
    private ActivityGroupMoreBinding binding;
    private String obj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        iGroupMorePre = new GroupMorePresenter(this);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_group_more);
        Intent intent = getIntent();
        obj = intent.getStringExtra("id");

        iGroupMorePre.adminsSet(this,binding,obj);
        iGroupMorePre.exitOrDissolveGroup(this,binding,obj);
    }
}
