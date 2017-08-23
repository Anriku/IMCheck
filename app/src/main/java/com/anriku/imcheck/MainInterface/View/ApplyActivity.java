package com.anriku.imcheck.MainInterface.View;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.anriku.imcheck.Adapter.FriendsApplyRecAdapter;
import com.anriku.imcheck.Adapter.GroupsApplyRecAdapter;
import com.anriku.imcheck.MainInterface.Model.FriendApply;
import com.anriku.imcheck.MainInterface.Model.GroupApply;
import com.anriku.imcheck.R;
import com.anriku.imcheck.databinding.ActivityApplyBinding;


public class ApplyActivity extends AppCompatActivity {

    private ActivityApplyBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_apply);

        getApplys();
    }

    private void getApplys() {

        Intent intent = getIntent();
        boolean isGroup = intent.getBooleanExtra("is_group", false);
        if (isGroup) {
            GroupApply groupApply = (GroupApply) intent.getSerializableExtra("group_apply");
            LinearLayoutManager manager = new LinearLayoutManager(this);
            GroupsApplyRecAdapter adapter = new GroupsApplyRecAdapter(this, groupApply);
            binding.friendsApplyRv.setLayoutManager(manager);
            binding.friendsApplyRv.setAdapter(adapter);
        } else {
            FriendApply friendApply = (FriendApply) intent.getSerializableExtra("friend_apply");
            LinearLayoutManager manager = new LinearLayoutManager(this);
            FriendsApplyRecAdapter adapter = new FriendsApplyRecAdapter(this, friendApply);
            binding.friendsApplyRv.setLayoutManager(manager);
            binding.friendsApplyRv.setAdapter(adapter);
        }
    }


}
