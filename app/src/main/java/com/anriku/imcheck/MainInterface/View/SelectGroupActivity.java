package com.anriku.imcheck.MainInterface.View;

import android.databinding.DataBindingUtil;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

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

        binding.acSelectGroupTb.setTitle("");
        setSupportActionBar(binding.acSelectGroupTb);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setHomeAsUpIndicator(R.mipmap.back);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        iSelectGroupPre = new SelectGroupPresenter(this);
        iSelectGroupPre.createGroup(this,binding);
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
