package com.anriku.imcheck.MainInterface.View;

import android.databinding.DataBindingUtil;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.anriku.imcheck.MainInterface.Interface.ICreateGroupAct;
import com.anriku.imcheck.MainInterface.Interface.ICreateGroupPre;
import com.anriku.imcheck.MainInterface.Presenter.CreateGroupPresenter;
import com.anriku.imcheck.R;
import com.anriku.imcheck.databinding.ActivityCreateGroupBinding;


public class CreateGroupActivity extends AppCompatActivity implements ICreateGroupAct {

    private ActivityCreateGroupBinding binding;
    private ICreateGroupPre iCreateGroupPre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_create_group);

        binding.acCreateGroupTb.setTitle("");
        setSupportActionBar(binding.acCreateGroupTb);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setHomeAsUpIndicator(R.mipmap.back);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        iCreateGroupPre = new CreateGroupPresenter(this);
        iCreateGroupPre.createGroup(this,binding);
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
