package com.anriku.imcheck.LoginAndRegister.View;

import android.databinding.DataBindingUtil;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.anriku.imcheck.LoginAndRegister.Interface.IRegisterAct;
import com.anriku.imcheck.LoginAndRegister.Interface.IRegisterPre;
import com.anriku.imcheck.LoginAndRegister.Presenter.RegisterPresenter;
import com.anriku.imcheck.R;
import com.anriku.imcheck.databinding.ActivityRegisterBinding;

public class RegisterActivity extends AppCompatActivity implements IRegisterAct {

    private IRegisterPre iRegisterPre;
    private ActivityRegisterBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_register);
        iRegisterPre = new RegisterPresenter(this);

        binding.acRegisterTb.setTitle("");
        setSupportActionBar(binding.acRegisterTb);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setHomeAsUpIndicator(R.mipmap.back);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        iRegisterPre.register(this,binding);
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
