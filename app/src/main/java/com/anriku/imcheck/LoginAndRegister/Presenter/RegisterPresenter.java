package com.anriku.imcheck.LoginAndRegister.Presenter;

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

import com.anriku.imcheck.LoginAndRegister.Interface.IRegisterAct;
import com.anriku.imcheck.LoginAndRegister.Interface.IRegisterPre;
import com.anriku.imcheck.LoginAndRegister.View.RegisterActivity;
import com.anriku.imcheck.MainInterface.View.MainInterfaceActivity;
import com.anriku.imcheck.databinding.ActivityRegisterBinding;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.wilddog.client.SyncReference;
import com.wilddog.client.WilddogSync;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Created by Anriku on 2017/8/17.
 */

public class RegisterPresenter implements IRegisterPre {

    private IRegisterAct iRegisterAct;

    public RegisterPresenter(IRegisterAct iRegisterAct) {
        this.iRegisterAct = iRegisterAct;
    }

    @Override
    public void register(final Context context, final ActivityRegisterBinding binding) {
        binding.acRegisterRegisterBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(binding.acRegisterAccountEt.getText().toString()) || TextUtils.isEmpty(binding.acRegisterFirstPasswordEt.getText().toString())
                        || TextUtils.isEmpty(binding.acRegisterSecondPasswordEt.getText().toString())) {
                    Toast.makeText(context, "亲,所填的数据不能为空哟!", Toast.LENGTH_SHORT).show();
                } else if (!binding.acRegisterFirstPasswordEt.getText().toString().equals(binding.acRegisterSecondPasswordEt.getText().toString())) {
                    Toast.makeText(context, "亲,你的两次密码填写不同哟", Toast.LENGTH_SHORT).show();
                } else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                //进行注册并登录
                                EMClient.getInstance().createAccount(binding.acRegisterAccountEt.getText().toString(), binding.acRegisterFirstPasswordEt.getText().toString());
                                EMClient.getInstance().login(binding.acRegisterAccountEt.getText().toString(), binding.acRegisterFirstPasswordEt.getText().toString()
                                        , new EMCallBack() {
                                            @Override
                                            public void onSuccess() {
                                                EMClient.getInstance().groupManager().loadAllGroups();
                                                EMClient.getInstance().chatManager().loadAllConversations();

                                                //对登录账户进行记录
                                                SharedPreferences.Editor editor = context.getSharedPreferences("account", Context.MODE_PRIVATE).edit();
                                                editor.putString("name", binding.acRegisterAccountEt.getText().toString());
                                                editor.apply();
                                            }

                                            @Override
                                            public void onError(int i, String s) {

                                            }

                                            @Override
                                            public void onProgress(int i, String s) {

                                            }
                                        });

                                //进行用户的保存以便查询
                                SyncReference reference = WilddogSync.getInstance().getReference("accounts");
                                reference.push().setValue(binding.acRegisterAccountEt.getText().toString());
                                context.startActivity(new Intent(context, MainInterfaceActivity.class));
                                ((RegisterActivity) context).finish();

                            } catch (HyphenateException e) {
                                e.printStackTrace();
                                //进行Toast
                                Observable.create(new ObservableOnSubscribe<String>() {
                                    @Override
                                    public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                                        e.onNext("注册失败，用户已存在");
                                    }
                                }).observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new Consumer<String>() {
                                            @Override
                                            public void accept(String s) throws Exception {
                                                Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        }
                    }).start();
                }
            }
        });
    }
}
