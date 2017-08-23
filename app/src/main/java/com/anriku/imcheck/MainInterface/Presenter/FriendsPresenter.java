package com.anriku.imcheck.MainInterface.Presenter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;

import com.anriku.imcheck.Adapter.FriendsRecAdapter;
import com.anriku.imcheck.MainInterface.Interface.IFriendsFrg;
import com.anriku.imcheck.MainInterface.Interface.IFriendsPre;
import com.anriku.imcheck.MainInterface.Model.FriendApply;
import com.anriku.imcheck.MainInterface.View.ApplyActivity;
import com.anriku.imcheck.R;
import com.anriku.imcheck.databinding.FragmentFriendsBinding;
import com.hyphenate.EMContactListener;
import com.hyphenate.chat.EMClient;
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
 * Created by Anriku on 2017/8/17.
 */

public class FriendsPresenter implements IFriendsPre {

    private IFriendsFrg iFriendsFrg;

    public FriendsPresenter(IFriendsFrg iFriendsFrg) {
        this.iFriendsFrg = iFriendsFrg;
    }

    @Override
    public void getFriends(final Context context, final FragmentFriendsBinding binding) {
        //获取好友,异步加载
        Observable.create(new ObservableOnSubscribe<List<String>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<String>> e) throws Exception {
                List<String> names = new ArrayList<>();
                try {
                    names = EMClient.getInstance().contactManager().getAllContactsFromServer();
                    names.removeAll(EMClient.getInstance().contactManager().getBlackListFromServer());
                } catch (HyphenateException e1) {
                    e1.printStackTrace();
                }
                e.onNext(names);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<String>>() {
                    @Override
                    public void accept(List<String> strings) throws Exception {
                        FriendsRecAdapter adapter = new FriendsRecAdapter(context, strings,FriendsRecAdapter.FRIEND);
                        LinearLayoutManager manager = new LinearLayoutManager(context);
                        binding.frgFriendsRv.setAdapter(adapter);
                        binding.frgFriendsRv.setLayoutManager(manager);
                    }
                });
    }


    @Override
    public void handleApply(final Context context, final List<String> names, final List<String> reasons, final FragmentFriendsBinding binding) {

        EMClient.getInstance().contactManager().setContactListener(new EMContactListener() {

            @Override
            public void onContactAdded(String s) {
                getFriends(context, binding);
            }

            @Override
            public void onContactDeleted(String s) {
                getFriends(context, binding);
            }

            @Override
            public void onContactInvited(final String s, final String s1) {
                Log.e("Friends", "收到");
                //消息到来进行的操作
                Observable.create(new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                        names.add(s);
                        reasons.add(s1);
                        e.onNext("收到");
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<String>() {
                            @Override
                            public void accept(String s) throws Exception {
                                binding.frgFriendsTv.setBackgroundColor(Color.parseColor("#333333"));
                            }
                        });
            }

            @Override
            public void onFriendRequestAccepted(String s) {

            }

            @Override
            public void onFriendRequestDeclined(String s) {

            }
        });

        binding.frgFriendsTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, ApplyActivity.class);
                FriendApply friendApply = new FriendApply(names,reasons);
                intent.putExtra("friend_apply",friendApply);
                intent.putExtra("is_group",false);
                context.startActivity(intent);
                binding.frgFriendsTv.setBackgroundColor(Color.parseColor("#ffffff"));
            }
        });
    }

    @Override
    public void refreshFriends(final Context context, final FragmentFriendsBinding binding) {
        binding.frgFriendsSrl.setColorSchemeResources(R.color.colorAccent);
        binding.frgFriendsSrl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getFriends(context, binding);
                binding.frgFriendsSrl.setRefreshing(false);
            }
        });
    }
}
