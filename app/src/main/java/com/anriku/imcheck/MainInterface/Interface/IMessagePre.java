package com.anriku.imcheck.MainInterface.Interface;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.Window;

import com.anriku.imcheck.MainInterface.Model.VideoModel;
import com.anriku.imcheck.databinding.ActivityLoginBinding;
import com.anriku.imcheck.databinding.ActivityMessageBinding;

/**
 * Created by Anriku on 2017/8/18.
 */

public interface IMessagePre {

    void setChatObj(Context chatObj,ActivityMessageBinding binding,String obj,boolean isGroup);

    void setMessagesRecAdapter(Context context, ActivityMessageBinding binding);

    void chat(Context context, String obj, ActivityMessageBinding binding, FragmentManager fragmentManager, boolean isGroup);

    void getHistory(Context context, String obj, ActivityMessageBinding binding);

    void setMore(ActivityMessageBinding binding);

    void setSpeak(Context context, Window window, String obj, ActivityMessageBinding binding, boolean isGroup);

    void getImageFromAlbum(Context context, String obj, ActivityMessageBinding binding);

    void sendImage(String obj, String imagePath, LayoutInflater inflater, Window window, ActivityMessageBinding binding, boolean isGroup);

    void sendVideo(Context context, Window window, ActivityMessageBinding binding, String obj);

    void getFile(Context context, ActivityMessageBinding binding);

    void sendFile(Context context, String filePath, ActivityMessageBinding binding, String obj, boolean isGroup);
}
