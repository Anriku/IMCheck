package com.anriku.imcheck.LoginAndRegister.Interface;

import android.content.Context;
import android.widget.Button;
import android.widget.EditText;

import com.anriku.imcheck.databinding.ActivityLoginBinding;

/**
 * Created by Anriku on 2017/8/17.
 */

public interface ILoginPre {
    void handleExceptionExit();

    void login(Context context, ActivityLoginBinding binding);

    void register(Context context, ActivityLoginBinding binding);
}
