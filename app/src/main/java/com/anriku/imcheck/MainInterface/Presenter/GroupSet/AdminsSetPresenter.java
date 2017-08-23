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

    public AdminsSetPresenter(IAdminsSetAct iAdminsSetAct) {
        this.iAdminsSetAct = iAdminsSetAct;
    }

    @Override
    public void getAdmins(final Context context, final ActivityAdminsSetBinding binding, final String obj) {
        Observable.create(new ObservableOnSubscribe<List<String>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<String>> e) throws Exception {
                try {
                    admins = EMClient.getInstance().groupManager().getGroupFromServer(obj).getAdminList();
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
                        FriendsRecAdapter adapter = new FriendsRecAdapter(context, strings, FriendsRecAdapter.ADMIN);
                        adapter.setGroupId(obj);
                        binding.acAdminsSetAdminRv.setLayoutManager(manager);
                        binding.acAdminsSetAdminRv.setAdapter(adapter);
                    }
                });
    }

    @Override
    public void inviteNewMember(Context context, ActivityAdminsSetBinding binding, String obj) {
        Intent intent = new Intent(context, InviteMembersActivity.class);
        intent.putExtra("id", obj);
        context.startActivity(intent);
    }

    @Override
    public void getMembers(final Context context, final ActivityAdminsSetBinding binding, final String obj) {
        Observable.create(new ObservableOnSubscribe<List<String>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<String>> e) throws Exception {
                List<String> members = new ArrayList<>();
                EMCursorResult<String> result = null;
                int pageSize = 20;
                do {
                    result = EMClient.getInstance().groupManager().fetchGroupMembers(obj,
                            result != null ? result.getCursor() : "", pageSize);
                    members.addAll(result.getData());
                }
                while (!TextUtils.isEmpty(result.getCursor()) && result.getData().size() == pageSize);
                e.onNext(members);
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<String>>() {
                    @Override
                    public void accept(List<String> strings) throws Exception {
                        Toast.makeText(context, String.valueOf(admins.size()) + ":" + String.valueOf(strings.size()), Toast.LENGTH_SHORT).show();
                        strings.removeAll(admins);
                        LinearLayoutManager manager = new LinearLayoutManager(context);
                        FriendsRecAdapter adapter = new FriendsRecAdapter(context, strings, FriendsRecAdapter.MEMBER);
                        adapter.setGroupId(obj);
                        binding.acAdminsSetMemberRv.setLayoutManager(manager);
                        binding.acAdminsSetMemberRv.setAdapter(adapter);
                    }
                });
    }
}
