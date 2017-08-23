package com.anriku.imcheck.MainInterface.Presenter.GroupSet;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.widget.Toast;

import com.anriku.imcheck.Adapter.FriendsRecAdapter;
import com.anriku.imcheck.MainInterface.Interface.GroupSet.IAdminsSetAct;
import com.anriku.imcheck.MainInterface.Interface.GroupSet.IAdminsSetPre;
import com.anriku.imcheck.MainInterface.View.GroupSet.InviteMembersActivity;
import com.anriku.imcheck.databinding.ActivityAdminsSetBinding;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCursorResult;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.exceptions.HyphenateException;

import java.util.ArrayList;
import java.util.List;

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

public class AdminsSetPresenter implements IAdminsSetPre {

    private IAdminsSetAct iAdminsSetAct;
        private List<String> admins = new ArrayList<>();
    private FriendsRecAdapter adapter;
    private int adminSize;

    public AdminsSetPresenter(IAdminsSetAct iAdminsSetAct) {
        this.iAdminsSetAct = iAdminsSetAct;
    }

    @Override
    public void getAdmins(final Context context, final ActivityAdminsSetBinding binding, final String obj) {
        Observable.create(new ObservableOnSubscribe<List<String>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<String>> e) throws Exception {
                try {
                    //加入群主
                    EMGroup emGroup = EMClient.getInstance().groupManager().getGroup(obj);
                    String owner = emGroup.getOwner();
                    admins.add(owner);
                    //加入管理员
                    admins.addAll(EMClient.getInstance().groupManager().getGroupFromServer(obj).getAdminList());
                    //加入群成员
                    List<String> members = new ArrayList<>();
                    EMCursorResult<String> result = null;
                    int pageSize = 20;
                    do {
                        result = EMClient.getInstance().groupManager().fetchGroupMembers(obj,
                                result != null ? result.getCursor() : "", pageSize);
                        members.addAll(result.getData());
                    }
                    while (!TextUtils.isEmpty(result.getCursor()) && result.getData().size() == pageSize);
                    admins.addAll(members);
                    //输入管理员的个数
                    adminSize = EMClient.getInstance().groupManager().getGroupFromServer(obj).getAdminList().size();
                    e.onNext(admins);
                } catch (HyphenateException ex) {
                    ex.printStackTrace();
                }
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<String>>() {
                    @Override
                    public void accept(List<String> strings) throws Exception {
                        LinearLayoutManager manager = new LinearLayoutManager(context);
                        adapter = new FriendsRecAdapter(context, strings, FriendsRecAdapter.LIST);
                        adapter.setGroupId(obj);
                        adapter.setAdmins(adminSize);
                        binding.acAdminsSetAdminRv.setLayoutManager(manager);
                        binding.acAdminsSetAdminRv.setAdapter(adapter);
                    }
                });

    }

}
