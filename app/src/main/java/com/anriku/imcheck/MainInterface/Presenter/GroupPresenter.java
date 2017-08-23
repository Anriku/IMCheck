package com.anriku.imcheck.MainInterface.Presenter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.anriku.imcheck.Adapter.GroupRecAdapter;
import com.anriku.imcheck.MainInterface.Interface.IGroupFrg;
import com.anriku.imcheck.MainInterface.Interface.IGroupPre;
import com.anriku.imcheck.MainInterface.Model.GroupApply;
import com.anriku.imcheck.MainInterface.View.ApplyActivity;
import com.anriku.imcheck.MainInterface.View.SelectGroupActivity;
import com.anriku.imcheck.R;
import com.anriku.imcheck.databinding.FragmentGroupBinding;
import com.hyphenate.EMGroupChangeListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMMucSharedFile;

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

public class GroupPresenter implements IGroupPre {

    private IGroupFrg iGroupFrg;
    private List<String> groupIds = new ArrayList<>();
    private List<String> groupNames = new ArrayList<>();
    private List<String> inviters = new ArrayList<>();
    private List<String> reasons = new ArrayList<>();

    public GroupPresenter(IGroupFrg iGroupFrg) {
        this.iGroupFrg = iGroupFrg;
    }

    @Override
    public void getGroups(final Context context, final FragmentGroupBinding binding) {
        Observable.create(new ObservableOnSubscribe<List<EMGroup>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<EMGroup>> e) throws Exception {
                List<EMGroup> groups = EMClient.getInstance().groupManager().getJoinedGroupsFromServer();
                e.onNext(groups);
            }
        })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<List<EMGroup>>() {
                    @Override
                    public void accept(List<EMGroup> emGroups) throws Exception {
                        LinearLayoutManager manager = new LinearLayoutManager(context);
                        GroupRecAdapter adapter = new GroupRecAdapter(context, emGroups);
                        binding.frgGroupRv.setLayoutManager(manager);
                        binding.frgGroupRv.setAdapter(adapter);
                    }
                });
    }

    @Override
    public void handleGroupApply(final Context context, final FragmentGroupBinding binding) {

        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull final ObservableEmitter<String> e) throws Exception {
                EMClient.getInstance().groupManager().addGroupChangeListener(new EMGroupChangeListener() {
                    @Override
                    public void onInvitationReceived(String s, String s1, String s2, String s3) {
                        groupIds.add(s);
                        groupNames.add(s1);
                        inviters.add(s2);
                        reasons.add(s3);
                        e.onNext("收到群申请");
                    }

                    @Override
                    public void onRequestToJoinReceived(String s, String s1, String s2, String s3) {

                    }

                    @Override
                    public void onRequestToJoinAccepted(String s, String s1, String s2) {

                    }

                    @Override
                    public void onRequestToJoinDeclined(String s, String s1, String s2, String s3) {

                    }

                    @Override
                    public void onInvitationAccepted(String s, String s1, String s2) {

                    }

                    @Override
                    public void onInvitationDeclined(String s, String s1, String s2) {

                    }

                    @Override
                    public void onUserRemoved(String s, String s1) {

                    }

                    @Override
                    public void onGroupDestroyed(String s, String s1) {

                    }

                    @Override
                    public void onAutoAcceptInvitationFromGroup(String s, String s1, String s2) {

                    }

                    @Override
                    public void onMuteListAdded(String s, List<String> list, long l) {

                    }

                    @Override
                    public void onMuteListRemoved(String s, List<String> list) {

                    }

                    @Override
                    public void onAdminAdded(String s, String s1) {

                    }

                    @Override
                    public void onAdminRemoved(String s, String s1) {

                    }

                    @Override
                    public void onOwnerChanged(String s, String s1, String s2) {

                    }

                    @Override
                    public void onMemberJoined(String s, String s1) {

                    }

                    @Override
                    public void onMemberExited(String s, String s1) {

                    }

                    @Override
                    public void onAnnouncementChanged(String s, String s1) {

                    }

                    @Override
                    public void onSharedFileAdded(String s, EMMucSharedFile emMucSharedFile) {

                    }

                    @Override
                    public void onSharedFileDeleted(String s, String s1) {

                    }
                });

            }
        })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Toast.makeText(context,s,Toast.LENGTH_SHORT).show();
                        binding.frgGroupApplyTv.setBackgroundColor(Color.parseColor(String.valueOf(R.color.grey)));
                    }
                });

        binding.frgGroupApplyTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ApplyActivity.class);
                GroupApply groupApply = new GroupApply(groupIds,groupNames,inviters,reasons);
                intent.putExtra("group_apply",groupApply);
                intent.putExtra("is_group",true);
                context.startActivity(intent);
            }
        });
    }
}
