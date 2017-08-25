package com.anriku.imcheck.MainInterface.View.UserSet;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.anriku.imcheck.MainInterface.Interface.UserSet.IUserSetAct;
import com.anriku.imcheck.MainInterface.Interface.UserSet.IUserSetPre;
import com.anriku.imcheck.MainInterface.Presenter.UserSet.UserSetPresenter;
import com.anriku.imcheck.R;
import com.anriku.imcheck.databinding.ActivityUserSetBinding;

public class UserSetActivity extends AppCompatActivity implements IUserSetAct {

    private ActivityUserSetBinding binding;
    private IUserSetPre iUserSetPre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_user_set);
        iUserSetPre = new UserSetPresenter(this);

        iUserSetPre.changNickName(this,getWindow(),binding);
    }
}
