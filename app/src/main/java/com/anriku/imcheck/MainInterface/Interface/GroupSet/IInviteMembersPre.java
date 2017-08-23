package com.anriku.imcheck.MainInterface.Interface.GroupSet;

import android.content.Context;

import com.anriku.imcheck.Adapter.FriendsRecAdapter;
import com.anriku.imcheck.databinding.ActivityInviteMembersBinding;

/**
 * Created by Anriku on 2017/8/23.
 */

public interface IInviteMembersPre {
    void getFriends(Context context, ActivityInviteMembersBinding binding, String obj);
    void inviteFriends(Context context,ActivityInviteMembersBinding binding,String obj);
}
