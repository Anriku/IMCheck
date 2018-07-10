package com.anriku.imcheck.MainInterface.Presenter;

import android.Manifest;
import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.anriku.imcheck.Adapter.MessagesRecAdapter;
import com.anriku.imcheck.Adapter.VideoRecAdapter;
import com.anriku.imcheck.MainInterface.Interface.IMessageAct;
import com.anriku.imcheck.MainInterface.Interface.IMessagePre;
import com.anriku.imcheck.MainInterface.Model.VideoModel;
import com.anriku.imcheck.R;
import com.anriku.imcheck.Utils.AudioRecorderUtils;
import com.anriku.imcheck.Utils.GetContentUtil;
import com.anriku.imcheck.Utils.GetVideoDataUtil;
import com.anriku.imcheck.Utils.PermissionUtil;
import com.anriku.imcheck.Utils.PopupWindowUtil;
import com.anriku.imcheck.databinding.ActivityMessageBinding;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMMessage;

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
    private List<EMMessage> emMessages;
    private MessagesRecAdapter MessagesRecAdapter;
    private boolean isFromDB = false;

    public MessagePresenter(IMessageAct iMessageAct) {
        this.iMessageAct = iMessageAct;
    }

    @Override
    public void setChatObj(final Context context, final ActivityMessageBinding binding, final String obj, boolean isGroup) {
        if (isGroup) {
            Observable.create(new ObservableOnSubscribe<EMGroup>() {
                @Override
                public void subscribe(@NonNull ObservableEmitter<EMGroup> e) throws Exception {
                    try {
                        EMGroup emGroup = EMClient.getInstance().groupManager().getGroupFromServer(obj);
                        e.onNext(emGroup);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Consumer<EMGroup>() {
                        @Override
                        public void accept(EMGroup emGroup) throws Exception {
                            binding.acMessageObjTv.setText(emGroup.getGroupName());
                        }
                    });
        } else {
            binding.acMessageObjTv.setText(obj);
        }
    }

    @Override
    public void setMessagesRecAdapter(Context context, ActivityMessageBinding binding) {
        LinearLayoutManager manager = new LinearLayoutManager(context);
        emMessages = new ArrayList<>();
        binding.acMessageRv.setLayoutManager(manager);
        MessagesRecAdapter = new MessagesRecAdapter(context, emMessages);
        binding.acMessageRv.setAdapter(MessagesRecAdapter);
    }

    @Override
    public void chat(final Context context, final String obj, final ActivityMessageBinding binding, final FragmentManager fragmentManager, final boolean isGroup) {
        binding.acMessageEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //如果功能菜单打开进行关闭
                if (binding.acMessageFirstLl.getVisibility() == View.VISIBLE) {
                    binding.acMessageFirstLl.setVisibility(View.GONE);
                    binding.acMessageMoreIv.setImageResource(R.mipmap.add);
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //如果有字完成出现没得就消失
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
                MessagesRecAdapter.notifyDataSetChanged();
                binding.acMessageRv.scrollToPosition(emMessages.size() - 1);

                //进行群聊的判断
                if (isGroup) {
                    message.setChatType(EMMessage.ChatType.GroupChat);
                }

                EMClient.getInstance().chatManager().sendMessage(message);
                binding.acMessageEt.setText("");
            }
        });

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
                        MessagesRecAdapter.notifyDataSetChanged();
                        binding.acMessageRv.scrollToPosition(emMessages.size() - 1);
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

    @Override
    public void setMore(final ActivityMessageBinding binding) {
        binding.acMessageMoreIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.acMessageFirstLl.getVisibility() == View.GONE) {
                    binding.acMessageFirstLl.setVisibility(View.VISIBLE);
                    binding.acMessageMoreIv.setImageResource(R.mipmap.functions_back);
                } else {
                    binding.acMessageFirstLl.setVisibility(View.GONE);
                    binding.acMessageMoreIv.setImageResource(R.mipmap.add);
                }
            }
        });
    }

    @Override
    public void setSpeak(final Context context, Window window, final String obj, final ActivityMessageBinding binding, final boolean isGroup) {
        //话筒的显示
        final PopupWindowUtil voicePop = PopupWindowUtil.getInstance(R.layout.speak_popup_window, R.style.Popupwindow, LayoutInflater.from(context),
                window, PopupWindowUtil.CENTER);
        final ImageView imageVie = voicePop.getFrameLayout().findViewById(R.id.speak_popup_window_iv);
        final TextView textView = voicePop.getFrameLayout().findViewById(R.id.speak_popup_window_tv);

        final AudioRecorderUtils utils = new AudioRecorderUtils();
        utils.setOnAudioStatusUpdateListener(new AudioRecorderUtils.OnAudioStatusUpdateListener() {
            @Override
            public void onUpdate(double db, String time) {
                imageVie.setImageLevel((int) db);
                textView.setText(time);
            }

            @Override
            public void onStop(String filePath) {
                Toast.makeText(context, "录音完成" + filePath, Toast.LENGTH_SHORT).show();
            }
        });


        //进行控件的可见变换
        binding.acMessageSpeakIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.acMessagePressSpeakBt.getVisibility() == View.GONE) {
                    binding.acMessageSpeakIv.setImageResource(R.mipmap.key_board);
                    binding.acMessagePressSpeakBt.setVisibility(View.VISIBLE);
                    binding.acMessageEt.setVisibility(View.GONE);

                    //权限设置，坑，坑，坑,一定要记住权限
                    List<String> permissions = new ArrayList<>();
                    permissions.add(Manifest.permission.RECORD_AUDIO);
                    permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    PermissionUtil.requestPermissions(context, permissions, new PermissionUtil.PermissionsInterface() {
                        @Override
                        public void onDoSomething() {
                            //按住不放进行说话，放开完成，进行设置
                            binding.acMessagePressSpeakBt.setOnTouchListener(new View.OnTouchListener() {
                                @Override
                                public boolean onTouch(final View view, MotionEvent motionEvent) {
                                    switch (motionEvent.getAction()) {
                                        case MotionEvent.ACTION_DOWN:
                                            voicePop.bottomWindow(view);
                                            binding.acMessagePressSpeakBt.setText("松开保存");
                                            utils.startRecord();
                                            break;

                                        case MotionEvent.ACTION_UP:
                                            utils.stopRecord();
                                            voicePop.getPopupWindow().dismiss();
                                            binding.acMessagePressSpeakBt.setText("按住说话");
                                            EMMessage message = EMMessage.createVoiceSendMessage(utils.getFilePath(), utils.getLength(), obj);
                                            emMessages.add(message);
                                            MessagesRecAdapter.notifyDataSetChanged();

                                            if (isGroup) {
                                                message.setChatType(EMMessage.ChatType.GroupChat);
                                            }

                                            EMClient.getInstance().chatManager().sendMessage(message);
                                            binding.acMessageRv.scrollToPosition(emMessages.size() - 1);
                                            break;
                                    }
                                    return true;
                                }
                            });
                        }

                        @Override
                        public void onSolvePermissionDisAllow() {
                            Toast.makeText(context, "只有允许权限，才能发语音哟", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError() {
                        }
                    });

                } else {
                    binding.acMessageSpeakIv.setImageResource(R.mipmap.speak);
                    binding.acMessagePressSpeakBt.setVisibility(View.GONE);
                    binding.acMessageEt.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    @Override
    public void getImageFromAlbum(final Context context, String obj, ActivityMessageBinding binding) {
        binding.acMessageImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> permissions = new ArrayList<>();
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                PermissionUtil.requestPermissions(context, permissions, new PermissionUtil.PermissionsInterface() {
                    @Override
                    public void onDoSomething() {
                        GetContentUtil.getContent(context, true);
                    }

                    @Override
                    public void onSolvePermissionDisAllow() {
                        Toast.makeText(context, "亲，要进行权限才能发图片哟", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError() {

                    }
                });
            }
        });
    }

    @Override
    public void sendImage(final String obj, final String imagePath, LayoutInflater inflater, Window window, final ActivityMessageBinding binding, final boolean isGroup) {
        final PopupWindowUtil popupWindowUtil = PopupWindowUtil.getInstance(R.layout.is_send_origin_image,
                R.style.Popupwindow, inflater, window, PopupWindowUtil.BOTTOM);
        popupWindowUtil.bottomWindow(window.getDecorView());
        FrameLayout linearLayout = popupWindowUtil.getFrameLayout();
        Button yesBt = linearLayout.findViewById(R.id.is_send_origin_image_yes_bt);
        Button noBt = linearLayout.findViewById(R.id.is_send_origin_image_no_bt);

        yesBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EMMessage emMessage = EMMessage.createImageSendMessage(imagePath, false, obj);
                emMessages.add(emMessage);
                MessagesRecAdapter.notifyDataSetChanged();

                if (isGroup) {
                    emMessage.setChatType(EMMessage.ChatType.GroupChat);
                }

                EMClient.getInstance().chatManager().sendMessage(emMessage);
                binding.acMessageRv.scrollToPosition(emMessages.size() - 1);
                popupWindowUtil.getPopupWindow().dismiss();
            }
        });

        noBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EMMessage emMessage = EMMessage.createImageSendMessage(imagePath, true, obj);
                emMessages.add(emMessage);
                MessagesRecAdapter.notifyDataSetChanged();

                if (isGroup) {
                    emMessage.setChatType(EMMessage.ChatType.GroupChat);
                }

                EMClient.getInstance().chatManager().sendMessage(emMessage);
                binding.acMessageRv.scrollToPosition(emMessages.size() - 1);
                popupWindowUtil.getPopupWindow().dismiss();
            }
        });
    }

    @Override
    public void sendVideo(final Context context, final Window window, ActivityMessageBinding binding, final String obj) {
        binding.acMessageVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<VideoModel> videoModels = GetVideoDataUtil.getVideoData(context);

                PopupWindowUtil videoPop = PopupWindowUtil.getInstance(R.layout.video_popup_window, R.style.Popupwindow, LayoutInflater.from(context)
                        , window, PopupWindowUtil.CENTER);

                RecyclerView rec = videoPop.getFrameLayout().findViewById(R.id.video_popup_window_rv);
                videoPop.bottomWindow(view);
                LinearLayoutManager manager = new LinearLayoutManager(context);
                VideoRecAdapter adapter = new VideoRecAdapter(context, obj, videoModels, videoPop, emMessages, MessagesRecAdapter);
                rec.setLayoutManager(manager);
                rec.setAdapter(adapter);
            }
        });
    }

    @Override
    public void getFile(final Context context, ActivityMessageBinding binding) {
        binding.acMessageFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> permissions = new ArrayList<>();
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                PermissionUtil.requestPermissions(context, permissions, new PermissionUtil.PermissionsInterface() {
                    @Override
                    public void onDoSomething() {
                        GetContentUtil.getContent(context, false);
                    }

                    @Override
                    public void onSolvePermissionDisAllow() {
                        Toast.makeText(context, "亲，要允许权限才能发送文件哟", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError() {
                    }

                });
            }
        });
    }

    @Override
    public void sendFile(Context context, String filePath, ActivityMessageBinding binding, String obj, boolean isGroup) {
        EMMessage message = EMMessage.createFileSendMessage(filePath, obj);

        if (isGroup) {
            message.setChatType(EMMessage.ChatType.GroupChat);
        }

        EMClient.getInstance().chatManager().sendMessage(message);
        emMessages.add(message);
        MessagesRecAdapter.notifyDataSetChanged();
        binding.acMessageRv.scrollToPosition(emMessages.size() - 1);
    }


    private void requestMoreNews(ActivityMessageBinding binding, String obj) {
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(obj);
        List<EMMessage> messages = new ArrayList<>();
        if (conversation != null) {
            //判断是不是该从数据库里拿消息了
            if (!isFromDB) {
                messages = conversation.getAllMessages();
                isFromDB = true;
            } else {
                messages = conversation.loadMoreMsgFromDB(emMessages.get(0).getMsgId(), 4);
            }
            emMessages.addAll(0, messages);
            Log.e("requestMoreNews: ", String.valueOf(messages.size()));
            MessagesRecAdapter.notifyDataSetChanged();
        }
        binding.acMessageSrl.setRefreshing(false);
    }

}
