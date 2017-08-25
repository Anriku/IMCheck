package com.anriku.imcheck.MainInterface.Presenter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.anriku.imcheck.Adapter.SpinnerAdapter;
import com.anriku.imcheck.MainInterface.Interface.ICreateGroupAct;
import com.anriku.imcheck.MainInterface.Interface.ICreateGroupPre;
import com.anriku.imcheck.databinding.ActivityCreateGroupBinding;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMGroupManager;
import com.hyphenate.chat.EMGroupOptions;
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
 * Created by Anriku on 2017/8/22.
 */

public class CreateGroupPresenter implements ICreateGroupPre {

    private ICreateGroupAct iCreateGroupAct;

    public CreateGroupPresenter(ICreateGroupAct iCreateGroupAct) {
        this.iCreateGroupAct = iCreateGroupAct;
    }

    @Override
    public void createGroup(final Context context, final ActivityCreateGroupBinding binding) {
        final String[] types = {"只有群主可以邀请的私有群", "群成员可以邀请的私有群",
                "可以申请加入的共有群", "任何人都能加入的共有群"};
        final int[] type = new int[1];

        SpinnerAdapter adapter = new SpinnerAdapter(context, types);

        binding.acCreateGroupSp.setAdapter(adapter);

        binding.acCreateGroupSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                type[0] = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        binding.acCreateGroupBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EMGroupOptions options = new EMGroupOptions();
                if (TextUtils.isEmpty(String.valueOf(binding.acCreateGroupMaxEt.getText()))) {
                    options.maxUsers = Integer.valueOf(String.valueOf(binding.acCreateGroupMaxEt.getText()));
                } else {
                    options.maxUsers = 200;
                }
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
                                    binding.acCreateGroupDescEt.getText().toString(), strings, null, options);

                            //如果是公有群就写入野狗中
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
