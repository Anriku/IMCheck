package com.anriku.imcheck.Adapter.viewholder;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.anriku.imcheck.R;
import com.anriku.imcheck.Utils.DownloadService;
import com.anriku.imcheck.Utils.PermissionUtil;
import com.hyphenate.chat.EMFileMessageBody;
import com.hyphenate.chat.EMMessage;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FileViewHolder extends BaseViewHolder {

    private CircleImageView circleImageView;
    private ImageView imageView;

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


    @Override
    public void onBindViewHolder(EMMessage emMessage) {
        if (getAccount(itemView.getContext()).equals(emMessage.getFrom())) {
            itemView.findViewById(R.id.civ_item_file_left).setVisibility(View.GONE);
            itemView.findViewById(R.id.iv_item_file_left).setVisibility(View.GONE);
            circleImageView = itemView.findViewById(R.id.civ_item_file_right);
            imageView = itemView.findViewById(R.id.iv_item_file_right);

        }else {
            itemView.findViewById(R.id.civ_item_file_right).setVisibility(View.GONE);
            itemView.findViewById(R.id.iv_item_file_right).setVisibility(View.GONE);
            circleImageView = itemView.findViewById(R.id.civ_item_file_left);
            imageView = itemView.findViewById(R.id.iv_item_file_left);
        }
        circleImageView.setVisibility(View.VISIBLE);
        imageView.setVisibility(View.VISIBLE);

        //进行下载操作
        final EMFileMessageBody messageBody = (EMFileMessageBody) emMessage.getBody();
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(itemView.getContext(), DownloadService.class);
                itemView.getContext().bindService(intent, connection, Context.BIND_AUTO_CREATE);

                List<String> permissions = new ArrayList<>();
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                PermissionUtil.requestPermissions(itemView.getContext(), permissions, new PermissionUtil.PermissionsInterface() {
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
                        Toast.makeText(itemView.getContext(), "亲,只有允许权限才能下载哟", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError() {

                    }
                });
            }

        });
    }

    public FileViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public int getType() {
        return BaseViewHolder.FILE;
    }
}
