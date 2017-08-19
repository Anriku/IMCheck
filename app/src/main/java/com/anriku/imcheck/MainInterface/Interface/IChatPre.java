package com.anriku.imcheck.MainInterface.Interface;

import android.content.Context;

import com.anriku.imcheck.databinding.ChatRecItemBinding;
import com.anriku.imcheck.databinding.FragmentChatBinding;

/**
 * Created by Anriku on 2017/8/19.
 */

public interface IChatPre {
    void getConversations(Context context,FragmentChatBinding binding);
    void refreshConversations(Context context,FragmentChatBinding binding);
}
