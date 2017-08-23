package com.anriku.imcheck.MainInterface.Presenter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Toast;

import com.anriku.imcheck.MainInterface.Interface.IGroupMoreAct;
import com.anriku.imcheck.MainInterface.Interface.IGroupMorePre;
import com.anriku.imcheck.MainInterface.View.GroupSet.AdminsSetActivity;
import com.anriku.imcheck.databinding.ActivityGroupMoreBinding;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.exceptions.HyphenateException;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Anriku on 2017/8/22.
 */

public class GroupMorePresenter implements IGroupMorePre {

    private IGroupMoreAct iGroupMoreAct;

    public GroupMorePresenter(IGroupMoreAct iGroupMoreAct) {
        this.iGroupMoreAct = iGroupMoreAct;
    }

    @Override
    public void adminsSet(final Context context, ActivityGroupMoreBinding binding, final String obj) {
        binding.acGroupMoreAdminTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AdminsSetActivity.class);
                intent.putExtra("id", obj);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public void exitOrDissolveGroup(final Context context, final ActivityGroupMoreBinding binding, final String obj) {

        SharedPreferences pref = context.getSharedPreferences("account", Context.MODE_PRIVATE);
        final String name = pref.getString("name", "");

        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                EMGroup emGroup = null;
                try {
                    emGroup = EMClient.getInstance().groupManager().getGroupFromServer(obj);
                    e.onNext(emGroup.getOwner());
                } catch (HyphenateException ex) {
                    ex.printStackTrace();
                }
            }
        })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        if (s.equals(name)) {
                            binding.acGroupMoreExitTv.setVisibility(View.GONE);
                            binding.acGroupMoreDissolveTv.setVisibility(View.VISIBLE);

                            binding.acGroupMoreDissolveTv.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    //进行解散群的操作
                                    Observable.create(new ObservableOnSubscribe<String>() {
                                        @Override
                                        public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                                            EMClient.getInstance().groupManager().destroyGroup(obj);
                                            e.onNext("解散成功");
                                        }
                                    })
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribeOn(Schedulers.io())
                                            .subscribe(new Consumer<String>() {
                                                @Override
                                                public void accept(String s) throws Exception {
                                                    Toast.makeText(context, "解散成功", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            });
                        } else {
                            binding.acGroupMoreExitTv.setVisibility(View.VISIBLE);
                            binding.acGroupMoreDissolveTv.setVisibility(View.GONE);

                            binding.acGroupMoreExitTv.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Observable.create(new ObservableOnSubscribe<String>() {
                                        @Override
                                        public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                                            EMClient.getInstance().groupManager().leaveGroup(obj);
                                            e.onNext("退出成功");
                                        }
                                    })
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(new Consumer<String>() {
                                                @Override
                                                public void accept(String s) throws Exception {
                                                    Toast.makeText(context,"退出成功",Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            });
                        }
                    }
                });
    }
}
