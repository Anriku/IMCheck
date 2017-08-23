package com.anriku.imcheck.MainInterface.Presenter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.anriku.imcheck.Adapter.FriendsSearchRecAdapter;
import com.anriku.imcheck.MainInterface.Interface.ISearchFriendAct;
import com.anriku.imcheck.MainInterface.Interface.ISearchFriendPre;
import com.anriku.imcheck.databinding.ActivitySearchFriendBinding;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.exceptions.HyphenateException;
import com.wilddog.client.DataSnapshot;
import com.wilddog.client.SyncError;
import com.wilddog.client.SyncReference;
import com.wilddog.client.ValueEventListener;
import com.wilddog.client.WilddogSync;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Anriku on 2017/8/18.
 */

public class SearchFriendPresenter implements ISearchFriendPre {

    private ISearchFriendAct iSearchFriendAct;

    public SearchFriendPresenter(ISearchFriendAct iSearchFriendAct) {
        this.iSearchFriendAct = iSearchFriendAct;
    }

    @Override
    public void search(final Context context, final ActivitySearchFriendBinding binding) {

        binding.acSearchFriendCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    searchGroup(context,binding);
                }else {
                    searchFriends(context,binding);
                }
            }
        });

        //设置查询的EditView
        binding.acAddFriendEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                binding.acAddFriendTv.setVisibility(View.VISIBLE);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //设置取消键
        binding.acAddFriendTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(binding.acAddFriendTv.getWindowToken(), 0);
                binding.acAddFriendEt.setText("");
                binding.acAddFriendTv.setVisibility(View.GONE);
            }
        });
    }

    private void searchGroup(final Context context, final ActivitySearchFriendBinding binding) {
        binding.acAddFriendIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //对软键盘回收的设置
                InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(binding.acAddFriendIv.getWindowToken(), 0);

                final SyncReference reference = WilddogSync.getInstance().getReference("groups");

                Observable.create(new ObservableOnSubscribe<List<String>>() {
                    @Override
                    public void subscribe(@NonNull final ObservableEmitter<List<String>> e) throws Exception {
                        reference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                List<EMGroup> myGroups = new ArrayList<>();
                                myGroups = EMClient.getInstance().groupManager().getAllGroups();

                                Iterator groups = dataSnapshot.getChildren().iterator();
                                List<String> list = new ArrayList<>();
                                while (groups.hasNext()) {
                                    DataSnapshot snapshot = (DataSnapshot) groups.next();
                                    if (!myGroups.contains(EMClient.getInstance().groupManager().getGroup( snapshot.getKey()))){
                                        list.add(snapshot.getKey());
                                    }
                                }
                                e.onNext(list);
                            }

                            @Override
                            public void onCancelled(SyncError syncError) {

                            }
                        });
                    }
                })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<List<String>>() {
                            @Override
                            public void accept(List<String> strings) throws Exception {
                                LinearLayoutManager manager = new LinearLayoutManager(context);
                                FriendsSearchRecAdapter adapter = new FriendsSearchRecAdapter(context, strings, FriendsSearchRecAdapter.GROUP);
                                binding.acAddFriendRv.setLayoutManager(manager);
                                binding.acAddFriendRv.setAdapter(adapter);
                            }
                        });
            }
        });
    }

    private void searchFriends(final Context context, final ActivitySearchFriendBinding binding) {


        Observable.create(new ObservableOnSubscribe<List<String>>() {
            @Override
            public void subscribe(@NonNull final ObservableEmitter<List<String>> e) throws Exception {
                //进行好友查询并列表显示
                binding.acAddFriendIv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(binding.acAddFriendIv.getWindowToken(), 0);
                        SyncReference reference = WilddogSync.getInstance().getReference("accounts");

                        reference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(final DataSnapshot dataSnapshot) {

                                //获取已有的好友
                                List<String> friends = new ArrayList<>();
                                try {
                                    friends = EMClient.getInstance().contactManager().getAllContactsFromServer();
                                } catch (HyphenateException e1) {
                                    e1.printStackTrace();
                                }
                                SharedPreferences pref = context.getSharedPreferences("account", Context.MODE_PRIVATE);
                                String account = pref.getString("name", "");
                                friends.add(account);

                                //进行判断添加
                                List<String> names = new ArrayList<>();
                                Iterator iterator = dataSnapshot.getChildren().iterator();
                                while (iterator.hasNext()) {
                                    DataSnapshot snapshot = (DataSnapshot) iterator.next();
                                    if (String.valueOf(snapshot.getValue()).contains(binding.acAddFriendEt.getText().toString())
                                            && !friends.contains(String.valueOf(snapshot.getValue()))) {
                                        names.add(String.valueOf(snapshot.getValue()));
                                    }
                                }

                                e.onNext(names);
                            }

                            @Override
                            public void onCancelled(SyncError syncError) {

                            }
                        });
                    }
                });

            }
        })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<List<String>>() {
                    @Override
                    public void accept(List<String> strings) throws Exception {
                        LinearLayoutManager manager = new LinearLayoutManager(context);
                        FriendsSearchRecAdapter adapter = new FriendsSearchRecAdapter(context, strings, FriendsSearchRecAdapter.FRIEND);
                        binding.acAddFriendRv.setLayoutManager(manager);
                        binding.acAddFriendRv.setAdapter(adapter);
                    }
                });


    }

}
