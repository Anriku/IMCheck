package com.anriku.imcheck.MainInterface.Presenter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.anriku.imcheck.Adapter.MessageRecAdapter;
import com.anriku.imcheck.MainInterface.Interface.IMessageAct;
import com.anriku.imcheck.MainInterface.Interface.IMessagePre;
import com.anriku.imcheck.R;
import com.anriku.imcheck.databinding.ActivityMessageBinding;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
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
 * Created by Anriku on 2017/8/18.
 */

public class MessagePresenter implements IMessagePre {

    private IMessageAct iMessageAct;
    private List<EMMessage> emMessages = new ArrayList<>();
    private MessageRecAdapter adapter;
    private boolean isFromDB = false;

    public MessagePresenter(IMessageAct iMessageAct) {
        this.iMessageAct = iMessageAct;
    }

    @Override
    public void chat(final Context context, final String obj, final ActivityMessageBinding binding) {
        binding.acMessageEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (TextUtils.isEmpty(binding.acMessageEt.getText().toString())) {
                    binding.acMessageMoreIv.setVisibility(View.VISIBLE);
                    binding.acMessageBt.setVisibility(View.GONE);
                } else {
                    binding.acMessageMoreIv.setVisibility(View.GONE);
                    binding.acMessageBt.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.acMessageBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EMMessage message = EMMessage.createTxtSendMessage(binding.acMessageEt.getText().toString(), obj);
                emMessages.add(message);
                //输入信息后进行跳转
                adapter.notifyDataSetChanged();
                binding.acMessageRv.scrollToPosition(emMessages.size() - 1);
                EMClient.getInstance().chatManager().sendMessage(message);
                binding.acMessageEt.setText("");

            }
        });
        adapter = new MessageRecAdapter(context, emMessages);
        LinearLayoutManager manager = new LinearLayoutManager(context);
        binding.acMessageRv.setLayoutManager(manager);
        binding.acMessageRv.setAdapter(adapter);

        //聊天进行实时更新
        Observable.create(new ObservableOnSubscribe<List<EMMessage>>() {
            @Override
            public void subscribe(@NonNull final ObservableEmitter<List<EMMessage>> e) throws Exception {
                EMClient.getInstance().chatManager().addMessageListener(new EMMessageListener() {
                    @Override
                    public void onMessageReceived(List<EMMessage> list) {
                        e.onNext(list);
                    }

                    @Override
                    public void onCmdMessageReceived(List<EMMessage> list) {

                    }

                    @Override
                    public void onMessageRead(List<EMMessage> list) {

                    }

                    @Override
                    public void onMessageDelivered(List<EMMessage> list) {

                    }

                    @Override
                    public void onMessageRecalled(List<EMMessage> list) {

                    }

                    @Override
                    public void onMessageChanged(EMMessage emMessage, Object o) {

                    }
                });
            }
        })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<EMMessage>>() {
                    @Override
                    public void accept(List<EMMessage> list) throws Exception {
                        emMessages.addAll(list);
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    @Override
    public void getHistory(Context context, final String obj, final ActivityMessageBinding binding) {
        binding.acMessageSrl.setColorSchemeResources(R.color.colorAccent);
        binding.acMessageSrl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestMoreNews(binding, obj);
            }
        });
    }


    private void requestMoreNews(ActivityMessageBinding binding, String obj) {
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(obj);
        List<EMMessage> messages = new ArrayList<>();
        //判断是不是该从数据库里拿消息了
        if (!isFromDB) {
            messages = conversation.getAllMessages();
            isFromDB = true;
        } else {
            messages = conversation.loadMoreMsgFromDB(emMessages.get(0).getMsgId(), 5);
        }
        emMessages.addAll(0, messages);
        Log.e("Message", emMessages.toString());
        adapter.notifyDataSetChanged();
        binding.acMessageSrl.setRefreshing(false);
    }

}
