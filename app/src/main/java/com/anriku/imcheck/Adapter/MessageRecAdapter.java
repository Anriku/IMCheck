package com.anriku.imcheck.Adapter;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.IBinder;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.anriku.imcheck.Utils.VideoActivity;
import com.anriku.imcheck.R;
import com.anriku.imcheck.Utils.DownloadService;
import com.anriku.imcheck.Utils.ImageActivity;
import com.anriku.imcheck.Utils.PermissionUtil;
import com.anriku.imcheck.Utils.VoiceUtil;
import com.anriku.imcheck.databinding.MessageRecItemBinding;
import com.bumptech.glide.Glide;
import com.hyphenate.chat.EMFileMessageBody;
import com.hyphenate.chat.EMImageMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.chat.EMVideoMessageBody;
import com.hyphenate.chat.EMVoiceMessageBody;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anriku on 2017/8/18.
 */

public class MessageRecAdapter extends RecyclerView.Adapter<MessageRecAdapter.ViewHolder> {

    private Context context;
    private List<EMMessage> emMessages;
    private String TAG = "MsgRecAdapter";
    private DownloadService.DownloadBinder binder;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            binder = (DownloadService.DownloadBinder) iBinder;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    public MessageRecAdapter(Context context, List<EMMessage> emMessages) {
        this.context = context;
        this.emMessages = emMessages;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (context == null) {
            context = parent.getContext();
        }
        MessageRecItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.message_rec_item, parent, false);
        return new ViewHolder(binding);
    }

    //RecyclerView的重用注意对可见性以及点击事件的重新设置
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        //获取登录的用户的id
        SharedPreferences pref = context.getSharedPreferences("account", Context.MODE_PRIVATE);
        String account = pref.getString("name", "");

        //进行消息来源的判断
        if (account.equals(emMessages.get(position).getFrom())) {
            holder.binding.messageRecItemLeftIv.setVisibility(View.GONE);
            holder.binding.messageRecItemLeftTv.setVisibility(View.GONE);
            holder.binding.messageRecItemLeftCiv.setVisibility(View.GONE);
            holder.binding.messageRecItemLeftMoreTv.setVisibility(View.GONE);

            holder.binding.messageRecItemRightCiv.setVisibility(View.VISIBLE);

            //进行消息类型的判断
            if (emMessages.get(position).getType() == EMMessage.Type.TXT) {

                //进一步的可见性设置
                holder.binding.messageRecItemRightIv.setVisibility(View.GONE);
                holder.binding.messageRecItemRightMoreTv.setVisibility(View.GONE);
                holder.binding.messageRecItemRightTv.setVisibility(View.VISIBLE);

                //对文字进行输出
                EMTextMessageBody messageBody = (EMTextMessageBody) emMessages.get(position).getBody();
                holder.binding.messageRecItemRightTv.setText(messageBody.getMessage());

                //防止重用点击事件
                holder.binding.messageRecItemRightIv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    }
                });

                Log.e(TAG, "onBindViewHolder:r发送文字" + String.valueOf(emMessages.get(position).getType()));

            } else if (emMessages.get(position).getType() == EMMessage.Type.VOICE) {
                holder.binding.messageRecItemRightTv.setVisibility(View.VISIBLE);
                holder.binding.messageRecItemRightMoreTv.setVisibility(View.VISIBLE);
                holder.binding.messageRecItemRightIv.setVisibility(View.VISIBLE);

                //防止重用后,文字还在
                holder.binding.messageRecItemRightTv.setText("");

                //进行语音播放
                holder.binding.messageRecItemRightTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        VoiceUtil util = new VoiceUtil();
                        util.startPlay(emMessages.get(position), holder.binding.messageRecItemRightIv);
                    }
                });
                EMVoiceMessageBody messageBody = (EMVoiceMessageBody) emMessages.get(position).getBody();
                //设置语音时间
                holder.binding.messageRecItemRightMoreTv.setText(String.valueOf(messageBody.getLength()) + "\"");

                //设置图片大小
                ViewGroup.LayoutParams params = holder.binding.messageRecItemRightIv.getLayoutParams();
                params.width = 60;
                params.height = 60;
                holder.binding.messageRecItemRightIv.setLayoutParams(params);

                holder.binding.messageRecItemRightIv.setImageResource(R.mipmap.voice_right);
                //防止重用点击事件
                holder.binding.messageRecItemRightIv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    }
                });

                Log.e(TAG, "onBindViewHolder: r发送语音" + String.valueOf(emMessages.get(position).getType()));
            } else if (emMessages.get(position).getType() == EMMessage.Type.IMAGE) {
                holder.binding.messageRecItemRightIv.setVisibility(View.VISIBLE);
                holder.binding.messageRecItemRightMoreTv.setVisibility(View.GONE);
                holder.binding.messageRecItemRightTv.setVisibility(View.GONE);


                ViewGroup.LayoutParams params = holder.binding.messageRecItemRightIv.getLayoutParams();
                params.width = 300;
                params.height = 300;
                holder.binding.messageRecItemRightIv.setLayoutParams(params);

                final EMImageMessageBody messageBody = (EMImageMessageBody) emMessages.get(position).getBody();
                Glide.with(context).load(messageBody.getRemoteUrl()).into(holder.binding.messageRecItemRightIv);

                holder.binding.messageRecItemRightIv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context,ImageActivity.class);
                        intent.putExtra("url",messageBody.getRemoteUrl());
                        context.startActivity(intent);
                    }
                });

                Log.e(TAG, "onBindViewHolder: r发送图片" + String.valueOf(emMessages.get(position).getType()));
            } else if (emMessages.get(position).getType() == EMMessage.Type.VIDEO) {
                holder.binding.messageRecItemRightIv.setVisibility(View.VISIBLE);
                holder.binding.messageRecItemRightTv.setVisibility(View.GONE);
                holder.binding.messageRecItemRightMoreTv.setVisibility(View.GONE);


                ViewGroup.LayoutParams params = holder.binding.messageRecItemRightIv.getLayoutParams();
                params.width = 300;
                params.height = 300;
                holder.binding.messageRecItemRightIv.setLayoutParams(params);

                final EMVideoMessageBody messageBody = (EMVideoMessageBody) emMessages.get(position).getBody();
                Glide.with(context).load(messageBody.getRemoteUrl()).into(holder.binding.messageRecItemRightIv);

                holder.binding.messageRecItemRightIv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    }
                });

                Log.e(TAG, "onBindViewHolder: r发送视频" + String.valueOf(emMessages.get(position).getType()));
            } else if (emMessages.get(position).getType() == EMMessage.Type.FILE) {
                holder.binding.messageRecItemRightMoreTv.setVisibility(View.GONE);
                holder.binding.messageRecItemRightTv.setVisibility(View.GONE);
                holder.binding.messageRecItemRightIv.setVisibility(View.VISIBLE);

                holder.binding.messageRecItemRightIv.setImageResource(R.mipmap.msg_file);

                final EMFileMessageBody messageBody = (EMFileMessageBody) emMessages.get(position).getBody();
                holder.binding.messageRecItemRightIv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, DownloadService.class);
                        context.bindService(intent, connection, Context.BIND_AUTO_CREATE);

                        List<String> permissions = new ArrayList<>();
                        permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                        PermissionUtil.requestPermissions(context, permissions, new PermissionUtil.PermissionsInterface() {
                            private boolean isDownload = false;

                            @Override
                            public void onDoSomething() {
                                if (!isDownload) {
                                    if (binder != null) {
                                        binder.startDownload(messageBody.getRemoteUrl());
                                        isDownload = true;
                                    }
                                } else {
                                    if (binder != null) {
                                        binder.pauseDownload();
                                        isDownload = false;
                                    }
                                }
                            }

                            @Override
                            public void onSolvePermissionDisAllow() {
                                Toast.makeText(context, "亲,只有允许权限才能下载哟", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onError() {

                            }
                        });
                    }

                });
            }
        } else {//当信息的发送者为他人的情况
            holder.binding.messageRecItemRightTv.setVisibility(View.GONE);
            holder.binding.messageRecItemRightCiv.setVisibility(View.GONE);
            holder.binding.messageRecItemRightIv.setVisibility(View.GONE);
            holder.binding.messageRecItemRightMoreTv.setVisibility(View.GONE);
            holder.binding.messageRecItemLeftCiv.setVisibility(View.VISIBLE);

            //进行消息类型的判断
            if (emMessages.get(position).getType() == EMMessage.Type.TXT) {

                holder.binding.messageRecItemLeftIv.setVisibility(View.GONE);
                holder.binding.messageRecItemLeftMoreTv.setVisibility(View.GONE);
                holder.binding.messageRecItemLeftTv.setVisibility(View.VISIBLE);

                EMTextMessageBody messageBody = (EMTextMessageBody) emMessages.get(position).getBody();
                holder.binding.messageRecItemLeftTv.setText(messageBody.getMessage());

                holder.binding.messageRecItemLeftIv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    }
                });

                Log.e(TAG, "onBindViewHolder: l发送文字" + String.valueOf(emMessages.get(position).getType()));
            } else if (emMessages.get(position).getType() == EMMessage.Type.VOICE) {
                holder.binding.messageRecItemLeftTv.setVisibility(View.VISIBLE);
                holder.binding.messageRecItemLeftMoreTv.setVisibility(View.VISIBLE);
                holder.binding.messageRecItemLeftIv.setVisibility(View.VISIBLE);

                //防止重用后,文字还在
                holder.binding.messageRecItemLeftTv.setText("");

                //进行语音播放
                holder.binding.messageRecItemLeftTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        VoiceUtil util = new VoiceUtil();
                        util.startPlay(emMessages.get(position), holder.binding.messageRecItemLeftIv);
                    }
                });

                EMVoiceMessageBody messageBody = (EMVoiceMessageBody) emMessages.get(position).getBody();
                //设置语音时间
                holder.binding.messageRecItemLeftMoreTv.setText(String.valueOf(messageBody.getLength()) + "\"");

                //设置图片大小
                ViewGroup.LayoutParams params = holder.binding.messageRecItemLeftIv.getLayoutParams();
                params.width = 60;
                params.height = 60;
                holder.binding.messageRecItemLeftIv.setLayoutParams(params);

                holder.binding.messageRecItemLeftIv.setImageResource(R.mipmap.voice_left);
                //防止重用点击事件
                holder.binding.messageRecItemLeftIv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    }
                });

                Log.e(TAG, "onBindViewHolder: l发送语音" + String.valueOf(emMessages.get(position).getType()));
            } else if (emMessages.get(position).getType() == EMMessage.Type.IMAGE) {
                holder.binding.messageRecItemLeftMoreTv.setVisibility(View.GONE);
                holder.binding.messageRecItemLeftIv.setVisibility(View.VISIBLE);
                holder.binding.messageRecItemLeftTv.setVisibility(View.GONE);

                ViewGroup.LayoutParams params = holder.binding.messageRecItemLeftIv.getLayoutParams();
                params.width = 300;
                params.height = 300;
                holder.binding.messageRecItemLeftIv.setLayoutParams(params);

                final EMImageMessageBody messageBody = (EMImageMessageBody) emMessages.get(position).getBody();
                Glide.with(context).load(messageBody.getRemoteUrl()).into(holder.binding.messageRecItemLeftIv);

                holder.binding.messageRecItemLeftIv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context,ImageActivity.class);
                        intent.putExtra("url",messageBody.getRemoteUrl());
                        context.startActivity(intent);
                    }
                });

                Log.e(TAG, "onBindViewHolder: l发送图片" + String.valueOf(emMessages.get(position).getType()));
            } else if (emMessages.get(position).getType() == EMMessage.Type.VIDEO) {
                holder.binding.messageRecItemLeftTv.setVisibility(View.GONE);
                holder.binding.messageRecItemLeftIv.setVisibility(View.VISIBLE);
                holder.binding.messageRecItemLeftMoreTv.setVisibility(View.GONE);


                ViewGroup.LayoutParams params = holder.binding.messageRecItemLeftIv.getLayoutParams();
                params.width = 300;
                params.height = 300;
                holder.binding.messageRecItemLeftIv.setLayoutParams(params);

                final EMVideoMessageBody messageBody = (EMVideoMessageBody) emMessages.get(position).getBody();
                Glide.with(context).load(messageBody.getRemoteUrl()).into(holder.binding.messageRecItemLeftIv);


                holder.binding.messageRecItemLeftIv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, VideoActivity.class);
                        intent.putExtra("video", messageBody);
                        context.startActivity(intent);
                    }
                });

                Log.e(TAG, "onBindViewHolder:l发送视频" + String.valueOf(emMessages.get(position).getType()));
            } else if (emMessages.get(position).getType() == EMMessage.Type.FILE) {
                holder.binding.messageRecItemLeftMoreTv.setVisibility(View.GONE);
                holder.binding.messageRecItemLeftTv.setVisibility(View.GONE);
                holder.binding.messageRecItemLeftIv.setVisibility(View.VISIBLE);

                holder.binding.messageRecItemLeftIv.setImageResource(R.mipmap.msg_file);
                final EMFileMessageBody messageBody = (EMFileMessageBody) emMessages.get(position).getBody();
                holder.binding.messageRecItemLeftIv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        List<String> permissions = new ArrayList<>();
                        permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                        Intent intent = new Intent(context, DownloadService.class);
                        context.bindService(intent, connection, Context.BIND_AUTO_CREATE);
                        PermissionUtil.requestPermissions(context, permissions, new PermissionUtil.PermissionsInterface() {
                            private boolean isDownload = false;

                            @Override
                            public void onDoSomething() {
                                if (!isDownload) {
                                    if (binder != null) {
                                        binder.startDownload(messageBody.getRemoteUrl());
                                        isDownload = true;
                                    }
                                } else {
                                    if (binder != null) {
                                        binder.pauseDownload();
                                        isDownload = false;
                                    }
                                }
                            }

                            @Override
                            public void onSolvePermissionDisAllow() {
                                Toast.makeText(context, "亲,只有允许权限才能下载哟", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onError() {
                            }
                        });
                    }
                });
            }
        }
    }


    @Override
    public int getItemCount() {
        return emMessages.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private MessageRecItemBinding binding;

        public ViewHolder(MessageRecItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
