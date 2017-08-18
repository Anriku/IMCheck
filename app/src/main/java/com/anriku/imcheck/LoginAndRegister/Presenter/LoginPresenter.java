package com.anriku.imcheck.LoginAndRegister.Presenter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.anriku.imcheck.LoginAndRegister.Interface.ILoginAct;
import com.anriku.imcheck.LoginAndRegister.Interface.ILoginPre;
import com.anriku.imcheck.LoginAndRegister.View.LoginActivity;
import com.anriku.imcheck.LoginAndRegister.View.RegisterActivity;
import com.anriku.imcheck.MainInterface.View.MainInterfaceActivity;
import com.anriku.imcheck.databinding.ActivityLoginBinding;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

/**
 * Created by Anriku on 2017/8/17.
 */

public class LoginPresenter implements ILoginPre {

    private ILoginAct iLoginAct;

    public LoginPresenter(ILoginAct iLoginAct) {
        this.iLoginAct = iLoginAct;
    }

    @Override
    public void login(final Context context, final ActivityLoginBinding binding) {
        binding.acLoginLoginBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EMClient.getInstance().login(binding.acLoginAccountEt.getText().toString(), binding.acLoginPasswordEt.getText().toString()
                        , new EMCallBack() {
                            @Override
                            public void onSuccess() {
                                //进行登录
                                EMClient.getInstance().groupManager().loadAllGroups();
                                EMClient.getInstance().chatManager().loadAllConversations();
                                context.startActivity(new Intent(context, MainInterfaceActivity.class));
                                ((LoginActivity)context).finish();
                                Looper.prepare();
                                Toast.makeText(context,"登录成功",Toast.LENGTH_SHORT).show();
                                //对登录账户进行记录
                                SharedPreferences.Editor editor = context.getSharedPreferences("account",Context.MODE_PRIVATE).edit();
                                editor.putString("name",binding.acLoginAccountEt.getText().toString());
                                editor.apply();

                                Looper.loop();
                            }

                            @Override
                            public void onError(int i, String s) {
                                Looper.prepare();
                                Toast.makeText(context,"密码错误哟",Toast.LENGTH_SHORT).show();
                                Looper.loop();
                            }

                            @Override
                            public void onProgress(int i, String s) {
                                Log.d("login","登录聊天服务器失败");
                            }
                        });
            }
        });
    }

    @Override
    public void register(final Context context, ActivityLoginBinding binding) {
        binding.acLoginRegisterBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, RegisterActivity.class));
                ((LoginActivity)context).finish();
            }
        });
    }
}
