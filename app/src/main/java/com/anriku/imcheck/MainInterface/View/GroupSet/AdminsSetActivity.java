package com.anriku.imcheck.MainInterface.View.GroupSet;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.anriku.imcheck.MainInterface.Interface.GroupSet.IAdminsSetAct;
import com.anriku.imcheck.MainInterface.Interface.GroupSet.IAdminsSetPre;
import com.anriku.imcheck.MainInterface.Presenter.GroupSet.AdminsSetPresenter;
import com.anriku.imcheck.R;
import com.anriku.imcheck.databinding.ActivityAdminsSetBinding;

public class AdminsSetActivity extends AppCompatActivity implements IAdminsSetAct {

    private IAdminsSetPre iAdminsSetPre;
    private ActivityAdminsSetBinding binding;
    private String obj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_admins_set);
        iAdminsSetPre = new AdminsSetPresenter(this);
        Intent intent = getIntent();
        obj = intent.getStringExtra("id");

        binding.acAdminsSetTb.setTitle("");
        setSupportActionBar(binding.acAdminsSetTb);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.mipmap.back);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        iAdminsSetPre.getAdmins(this, binding, obj);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return true;
    }
}
