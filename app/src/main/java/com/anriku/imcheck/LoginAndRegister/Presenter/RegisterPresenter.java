package com.anriku.imcheck.LoginAndRegister.Presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.anriku.imcheck.LoginAndRegister.Interface.IRegisterAct;
import com.anriku.imcheck.LoginAndRegister.Interface.IRegisterPre;
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
import io.reactivex.schedulers.Schedulers;

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
                    Observable.create(new ObservableOnSubscribe<String>() {
                        @Override
                        public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                            try {
                                //进行注册
                                EMClient.getInstance().createAccount(binding.acRegisterAccountEt.getText().toString(),
                                        binding.acRegisterFirstPasswordEt.getText().toString());

                                //进行用户的保存以便查询
                                SyncReference reference = WilddogSync.getInstance().getReference("accounts");
                                reference.push().setValue(binding.acRegisterAccountEt.getText().toString());
                                context.startActivity(new Intent(context, MainInterfaceActivity.class));
                                e.onNext("注册成功");
                                ((Activity) context).finish();
                            } catch (HyphenateException ex) {
                                e.onNext("注册失败,用户已存在");
                                ex.printStackTrace();
                            }
                        }
                    })
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribe(new Consumer<String>() {
                                @Override
                                public void accept(String s) throws Exception {
                                    Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });
    }
}
