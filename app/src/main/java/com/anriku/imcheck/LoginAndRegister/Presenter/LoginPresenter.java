package com.anriku.imcheck.LoginAndRegister.Presenter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
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

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

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

                if (TextUtils.isEmpty(binding.acLoginPasswordEt.getText().toString()) ||
                        TextUtils.isEmpty(binding.acLoginPasswordEt.getText().toString())) {
                    Toast.makeText(context, "输入不能为空哟", Toast.LENGTH_SHORT).show();
                } else {

                    final ProgressDialog progressDialog = new ProgressDialog(context);
                    progressDialog.setTitle("正在登录中...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    EMClient.getInstance().login(binding.acLoginAccountEt.getText().toString(), binding.acLoginPasswordEt.getText().toString()
                            , new EMCallBack() {
                                @Override
                                public void onSuccess() {

                                    //进行登录
                                    EMClient.getInstance().groupManager().loadAllGroups();
                                    EMClient.getInstance().chatManager().loadAllConversations();
                                    context.startActivity(new Intent(context, MainInterfaceActivity.class));
                                    ((LoginActivity) context).finish();
                                    //对登录账户进行记录
                                    SharedPreferences.Editor editor = context.getSharedPreferences("account", Context.MODE_PRIVATE).edit();
                                    editor.putString("name", binding.acLoginAccountEt.getText().toString());
                                    editor.apply();

                                    progressDialog.dismiss();
                                }

                                @Override
                                public void onError(int i, String s) {
                                    //进行Toast
                                    Observable.create(new ObservableOnSubscribe<String>() {
                                        @Override
                                        public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                                            e.onNext("密码错误哟");
                                        }
                                    }).observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(new Consumer<String>() {
                                                @Override
                                                public void accept(String s) throws Exception {
                                                    Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
                                                    progressDialog.dismiss();
                                                }
                                            });
                                }

                                @Override
                                public void onProgress(int i, String s) {
                                }
                            });
                }
            }
        });
    }

    @Override
    public void register(final Context context, ActivityLoginBinding binding) {
        binding.acLoginRegisterBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, RegisterActivity.class));
                ((LoginActivity) context).finish();
            }
        });
    }
}
