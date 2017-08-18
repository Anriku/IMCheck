package com.anriku.imcheck.MainInterface.Presenter;

import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.anriku.imcheck.Adapter.FriendsRecAdapter;
import com.anriku.imcheck.MainInterface.Interface.IFriendsFrg;
import com.anriku.imcheck.MainInterface.Interface.IFriendsPre;
import com.anriku.imcheck.MainInterface.View.FriendsApplyActivity;
import com.anriku.imcheck.databinding.FragmentFriendsBinding;
import com.hyphenate.EMContactListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.io.Serializable;
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
                        FriendsRecAdapter adapter = new FriendsRecAdapter(context, strings);
                        LinearLayoutManager manager = new LinearLayoutManager(context);
                        binding.frgFriendsRv.setAdapter(adapter);
                        binding.frgFriendsRv.setLayoutManager(manager);
                    }
                });

    }


    @Override
    public void handleApply(final Context context, final List<String> names, final List<String> reasons, FragmentFriendsBinding binding) {
        binding.frgFriendsTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, FriendsApplyActivity.class);

                EMClient.getInstance().contactManager().setContactListener(new EMContactListener() {

                    @Override
                    public void onContactAdded(String s) {

                    }

                    @Override
                    public void onContactDeleted(String s) {

                    }

                    @Override
                    public void onContactInvited(String s, String s1) {
                        Log.e("Friends", "收到");
                        names.add(s);
                        reasons.add(s1);
                    }

                    @Override
                    public void onFriendRequestAccepted(String s) {

                    }

                    @Override
                    public void onFriendRequestDeclined(String s) {

                    }
                });

                intent.putStringArrayListExtra("names", (ArrayList<String>) names);
                intent.putStringArrayListExtra("reasons", (ArrayList<String>) reasons);
                context.startActivity(intent);
            }
        });
    }
}