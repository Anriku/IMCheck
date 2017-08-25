package com.anriku.imcheck.MainInterface.Interface;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.RecyclerView;

import com.anriku.imcheck.databinding.FragmentFriendsBinding;

import java.util.List;

/**
 * Created by Anriku on 2017/8/17.
 */

public interface IFriendsPre {
    void getFriends(Context context, FragmentFriendsBinding binding);
    void handleApply(Context context, List<String> names,List<String> reasons,FragmentFriendsBinding binding);
    void refreshFriends(Context context,FragmentFriendsBinding binding);
}
