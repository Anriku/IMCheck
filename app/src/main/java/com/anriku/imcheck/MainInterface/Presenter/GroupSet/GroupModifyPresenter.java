package com.anriku.imcheck.MainInterface.Presenter.GroupSet;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.anriku.imcheck.MainInterface.Interface.GroupSet.IGroupModifyAct;
import com.anriku.imcheck.MainInterface.Interface.GroupSet.IGroupModifyPre;
import com.anriku.imcheck.databinding.ActivityGroupModifyBinding;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Anriku on 2017/8/23.
 */

public class GroupModifyPresenter implements IGroupModifyPre {

    private IGroupModifyAct iGroupModifyAct;

    public GroupModifyPresenter(IGroupModifyAct iGroupModifyAct) {
        this.iGroupModifyAct = iGroupModifyAct;
    }

    @Override
    public void setNameAndDesc(Context context, final ActivityGroupModifyBinding binding, final String obj) {
        Observable.create(new ObservableOnSubscribe<EMGroup>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<EMGroup> e) throws Exception {
                EMGroup group = EMClient.getInstance().groupManager().getGroupFromServer(obj);
                e.onNext(group);
            }
        })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<EMGroup>() {
                    @Override
                    public void accept(EMGroup s) throws Exception {
                        binding.acGroupModifyNameEt.setText(s.getGroupName());
                        binding.acGroupModifyDescEt.setText(s.getDescription());
                    }
                });
    }

    @Override
    public void changeNameAndDesc(final Context context, final ActivityGroupModifyBinding binding, final String obj) {

        binding.acGroupModifyBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Observable.create(new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                        EMClient.getInstance().groupManager().changeGroupName(obj, binding.acGroupModifyNameEt.getText().toString());
                        EMClient.getInstance().groupManager().changeGroupDescription(obj, binding.acGroupModifyDescEt.getText().toString());
                        e.onNext("修改成功");
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
        });
    }
}
