package com.anriku.imcheck.MainInterface.View.GroupSet;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.anriku.imcheck.MainInterface.Interface.GroupSet.IGroupModifyAct;
import com.anriku.imcheck.MainInterface.Interface.GroupSet.IGroupModifyPre;
import com.anriku.imcheck.MainInterface.Presenter.GroupSet.GroupModifyPresenter;
import com.anriku.imcheck.R;
import com.anriku.imcheck.databinding.ActivityGroupModifyBinding;

public class GroupModifyActivity extends AppCompatActivity implements IGroupModifyAct {

    private IGroupModifyPre iGroupModifyPre;
    private ActivityGroupModifyBinding binding;
    private String obj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_group_modify);
        iGroupModifyPre = new GroupModifyPresenter(this);

        binding.acGroupModifyTb.setTitle("");
        setSupportActionBar(binding.acGroupModifyTb);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setHomeAsUpIndicator(R.mipmap.back);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();
        obj = intent.getStringExtra("id");

        iGroupModifyPre.setNameAndDesc(this,binding,obj);
        iGroupModifyPre.changeNameAndDesc(this,binding,obj);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return true;
    }
}
