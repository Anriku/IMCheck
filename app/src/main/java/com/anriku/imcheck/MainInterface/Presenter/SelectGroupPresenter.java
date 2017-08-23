package com.anriku.imcheck.MainInterface.Presenter;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.anriku.imcheck.Adapter.SpinnerAdapter;
import com.anriku.imcheck.MainInterface.Interface.ISelectGroupAct;
import com.anriku.imcheck.MainInterface.Interface.ISelectGroupPre;
import com.anriku.imcheck.databinding.ActivitySelectGroupBinding;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMGroupManager;
import com.hyphenate.chat.EMGroupOptions;
import com.hyphenate.exceptions.HyphenateException;
import com.wilddog.client.SyncReference;
import com.wilddog.client.WilddogSync;

import java.util.ArrayList;
import java.util.HashMap;

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

public class SelectGroupPresenter implements ISelectGroupPre {

    private ISelectGroupAct iSelectGroupAct;

    public SelectGroupPresenter(ISelectGroupAct iSelectGroupAct) {
        this.iSelectGroupAct = iSelectGroupAct;
    }

    @Override
    public void createGroup(final Context context, final ActivitySelectGroupBinding binding) {
        final String[] types = {"只有群主可以邀请的私有群", "群成员可以邀请的私有群",
                "可以申请加入的共有群", "任何人都能加入的共有群"};
        final int[] type = new int[1];

        SpinnerAdapter adapter = new SpinnerAdapter(context, types);

        binding.acSelectGroupSp.setAdapter(adapter);

        binding.acSelectGroupSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                type[0] = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        binding.acSelectGroupBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EMGroupOptions options = new EMGroupOptions();
                options.maxUsers = Integer.valueOf(String.valueOf(binding.acSelectGroupMaxEt.getText()));
                switch (type[0]) {
                    case 0:
                        options.style = EMGroupManager.EMGroupStyle.EMGroupStylePrivateOnlyOwnerInvite;
                        break;
                    case 1:
                        options.style = EMGroupManager.EMGroupStyle.EMGroupStylePrivateMemberCanInvite;
                        break;
                    case 2:
                        options.style = EMGroupManager.EMGroupStyle.EMGroupStylePublicJoinNeedApproval;
                        break;
                    case 3:
                        options.style = EMGroupManager.EMGroupStyle.EMGroupStylePublicOpenJoin;
                        break;
                    default:
                        break;
                }

                Observable.create(new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                        try {
                            String[] strings = new String[0];
                            EMGroup emGroup = EMClient.getInstance().groupManager().createGroup(binding.acSelectGroupNameEt.getText().toString(),
                                    binding.acSelectGroupDescEt.getText().toString(), strings, null, options);

                            if (emGroup.isPublic()) {
                                SyncReference reference = WilddogSync.getInstance().getReference("groups");
                                reference.child(emGroup.getGroupId()).setValue(emGroup.isMemberOnly());
                            }

                            e.onNext("创建成功");
                        } catch (HyphenateException ex) {
                            ex.printStackTrace();
                            e.onNext("创建失败");
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
        });
    }

}
