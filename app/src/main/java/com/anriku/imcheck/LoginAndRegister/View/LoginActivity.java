package com.anriku.imcheck.LoginAndRegister.View;

import android.databinding.DataBindingUtil;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.anriku.imcheck.LoginAndRegister.Interface.ILoginAct;
import com.anriku.imcheck.LoginAndRegister.Interface.ILoginPre;
import com.anriku.imcheck.LoginAndRegister.Presenter.LoginPresenter;
import com.anriku.imcheck.R;
import com.anriku.imcheck.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity implements ILoginAct {

    private ILoginPre iLoginPre;
    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        iLoginPre = new LoginPresenter(this);

        binding.acLoginTb.setTitle("");
        setSupportActionBar(binding.acLoginTb);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.mipmap.back);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //用于处理异常退出后,账号未登出
        iLoginPre.handleExceptionExit();
        iLoginPre.login(this, binding);
        iLoginPre.register(this, binding);
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
