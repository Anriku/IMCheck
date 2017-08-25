package com.anriku.imcheck.Adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.anriku.imcheck.R;
import com.anriku.imcheck.databinding.FriendsSearchRecItemBinding;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.exceptions.HyphenateException;
import com.wilddog.client.DataSnapshot;
import com.wilddog.client.SyncError;
import com.wilddog.client.SyncReference;
import com.wilddog.client.ValueEventListener;
import com.wilddog.client.WilddogSync;

import java.util.ArrayList;
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

public class FriendsSearchRecAdapter extends RecyclerView.Adapter<FriendsSearchRecAdapter.ViewHolder> {

    private Context context;
    private List<String> names;
    private int type;
    private List<Boolean> isMemberOnly = new ArrayList<>();
    public static final int FRIEND = 0;
    public static final int GROUP = 1;

    public FriendsSearchRecAdapter(Context context, List<String> names, int type) {
        this.context = context;
        this.names = names;
        this.type = type;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (context == null) {
            context = parent.getContext();
        }
        FriendsSearchRecItemBinding binding = DataBindingUtil.inflate(LayoutInflater
                .from(context), R.layout.friends_search_rec_item, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {


        holder.binding.friendsSearchRecItemTv.setText(names.get(position));
        if (type == FRIEND) {
            holder.binding.friendsSearchRecItemBt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (holder.binding.friendsSearchRecItemEt.getVisibility() == View.GONE) {
                        holder.binding.friendsSearchRecItemEt.setVisibility(View.VISIBLE);
                    } else {
                        try {
                            EMClient.getInstance().contactManager().addContact(names.get(position), holder.binding.friendsSearchRecItemEt.getText().toString());
                            Toast.makeText(context, "发送请求成功", Toast.LENGTH_SHORT).show();
                        } catch (HyphenateException e) {
                            Toast.makeText(context, "添加好友失败,你们以及是好友啦", Toast.LENGTH_SHORT);
                            e.printStackTrace();
                        }
                    }

                }
            });
        } else if (type == GROUP) {

            SyncReference reference = WilddogSync.getInstance().getReference("groups");
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Iterator iterator = dataSnapshot.getChildren().iterator();
                    while (iterator.hasNext()) {
                        DataSnapshot snapshot = (DataSnapshot) iterator.next();
                        isMemberOnly.add((Boolean) snapshot.getValue());
                    }
                }

                @Override
                public void onCancelled(SyncError syncError) {

                }
            });

            //群的申请加入
            holder.binding.friendsSearchRecItemBt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (!isMemberOnly.get(position)) {
                        holder.binding.friendsSearchRecItemEt.setVisibility(View.GONE);

                        Observable.create(new ObservableOnSubscribe<String>() {
                            @Override
                            public void subscribe(@NonNull final ObservableEmitter<String> e) throws Exception {
                                try {
                                    EMClient.getInstance().groupManager().joinGroup(names.get(position));
                                    e.onNext("加入成功");
                                } catch (HyphenateException e1) {
                                    e1.printStackTrace();
                                    e.onNext("加入失败");
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
                    } else {
                        if (holder.binding.friendsSearchRecItemEt.getVisibility() == View.GONE) {
                            holder.binding.friendsSearchRecItemEt.setVisibility(View.VISIBLE);
                        } else {

                            Observable.create(new ObservableOnSubscribe<String>() {
                                @Override
                                public void subscribe(@NonNull final ObservableEmitter<String> e) throws Exception {
                                    try {
                                        EMClient.getInstance().groupManager().applyJoinToGroup(names.get(position)
                                                , holder.binding.friendsSearchRecItemEt.getText().toString());
                                        e.onNext("加入成功");
                                    } catch (HyphenateException e1) {
                                        e1.printStackTrace();
                                        e.onNext("加入失败");
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
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return names.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private FriendsSearchRecItemBinding binding;

        public ViewHolder(FriendsSearchRecItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
