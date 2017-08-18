package com.anriku.imcheck.LoginAndRegister.View;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.EditText;

import com.anriku.imcheck.LoginAndRegister.Interface.ILoginAct;
import com.anriku.imcheck.LoginAndRegister.Interface.ILoginPre;
import com.anriku.imcheck.LoginAndRegister.Presenter.LoginPresenter;
import com.anriku.imcheck.R;
import com.anriku.imcheck.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity implements ILoginAct{

    private ILoginPre iLoginPre;
    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_login);
        iLoginPre = new LoginPresenter(this);
        setSupportActionBar(binding.acLoginTb);

        iLoginPre.login(this,binding);
        iLoginPre.register(this,binding);
    }

}