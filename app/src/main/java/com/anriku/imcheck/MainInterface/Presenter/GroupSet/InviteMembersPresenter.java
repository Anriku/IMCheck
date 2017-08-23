package com.anriku.imcheck.MainInterface.Presenter.GroupSet;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.anriku.imcheck.Adapter.FriendsRecAdapter;
import com.anriku.imcheck.MainInterface.Interface.GroupSet.IInviteMembersAct;
import com.anriku.imcheck.MainInterface.Interface.GroupSet.IInviteMembersPre;
import com.anriku.imcheck.MainInterface.View.GroupSet.InviteMembersActivity;
import com.anriku.imcheck.databinding.ActivityInviteMembersBinding;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCursorResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

public class InviteMembersPresenter implements IInviteMembersPre {

    private IInviteMembersAct iInviteMembersAct;
    private FriendsRecAdapter adapter;
    private List<String> names;

    public InviteMembersPresenter(IInviteMembersAct iInviteMembersAct) {
        this.iInviteMembersAct = iInviteMembersAct;
    }

    @Override
    public void getFriends(final Context context, final ActivityInviteMembersBinding binding, final String obj) {
        Observable.create(new ObservableOnSubscribe<List<String>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<String>> e) throws Exception {
                names = EMClient.getInstance().contactManager().getAllContactsFromServer();

                List<String> members = new ArrayList<>();
                EMCursorResult<String> result = null;
                final int pageSize = 20;
                do {
                    result = EMClient.getInstance().groupManager().fetchGroupMembers(obj,
                            result != null ? result.getCursor() : "", pageSize);
                    members.addAll(result.getData());
                }
                while (!TextUtils.isEmpty(result.getCursor()) && result.getData().size() == pageSize);

                names.removeAll(members);

                e.onNext(names);
            }
        })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<List<String>>() {
                    @Override
                    public void accept(List<String> strings) throws Exception {
                        LinearLayoutManager manager = new LinearLayoutManager(context);
                        adapter = new FriendsRecAdapter(context, strings, FriendsRecAdapter.INVITE);
                        adapter.setGroupId(obj);
                        binding.acInviteMembersRv.setLayoutManager(manager);
                        binding.acInviteMembersRv.setAdapter(adapter);

                    }
                });
    }

    @Override
    public void inviteFriends(final Context context, ActivityInviteMembersBinding binding, final String obj) {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                EMClient.getInstance().groupManager().addUsersToGroup(obj, adapter.getInvites());
                e.onNext("邀请成功");
            }
        })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
                        //进行即时更新UI
                        List<String> invites;
                        invites = Arrays.asList(adapter.getInvites());
                        names.removeAll(invites);
                        adapter.notifyDataSetChanged();
                    }
                });
    }
}
