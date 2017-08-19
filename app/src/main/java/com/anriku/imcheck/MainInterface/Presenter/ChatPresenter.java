package com.anriku.imcheck.MainInterface.Presenter;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;

import com.anriku.imcheck.Adapter.ChatRecAdapter;
import com.anriku.imcheck.MainInterface.Interface.IChatFrg;
import com.anriku.imcheck.MainInterface.Interface.IChatPre;
import com.anriku.imcheck.R;
import com.anriku.imcheck.databinding.ChatRecItemBinding;
import com.anriku.imcheck.databinding.FragmentChatBinding;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Anriku on 2017/8/19.
 */

public class ChatPresenter implements IChatPre {

    private IChatFrg iChatFrg;

    public ChatPresenter(IChatFrg iChatFrg) {
        this.iChatFrg = iChatFrg;
    }

    @Override
    public void getConversations(final Context context, final FragmentChatBinding binding) {
        //对会话进行获取
        Observable.create(new ObservableOnSubscribe<List<EMConversation>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<EMConversation>> e) throws Exception {
                Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();
                Collection<EMConversation> cons = conversations.values();
                Iterator<EMConversation> iterator = cons.iterator();
                List<EMConversation> lists = new ArrayList<>();
                while (iterator.hasNext()) {
                    lists.add(iterator.next());
                }
                e.onNext(lists);
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<List<EMConversation>>() {
                    @Override
                    public void accept(List<EMConversation> emConversations) throws Exception {
                        LinearLayoutManager manager = new LinearLayoutManager(context);
                        ChatRecAdapter adapter = new ChatRecAdapter(context, emConversations);
                        binding.frgChatRv.setLayoutManager(manager);
                        binding.frgChatRv.setAdapter(adapter);
                    }
                });
    }

    @Override
    public void refreshConversations(final Context context, final FragmentChatBinding binding) {
        binding.frgChatSrl.setColorSchemeResources(R.color.colorAccent);
        binding.frgChatSrl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getConversations(context,binding);
                binding.frgChatSrl.setRefreshing(false);
            }
        });
    }
}
