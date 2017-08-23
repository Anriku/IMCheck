package com.anriku.imcheck.MainInterface.View.GroupSet;

import android.content.Intent;
import android.databinding.DataBindingUtil;
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

        setSupportActionBar(binding.acMainInterfaceTb);
        iAdminsSetPre.getAdmins(this, binding, obj);
        iAdminsSetPre.getMembers(this, binding, obj);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.admins_set_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.admins_set_add:
                iAdminsSetPre.inviteNewMember(this,binding,obj);
                break;
            case R.id.admins_set_edit:
                break;
        }
        return true;
    }
}
