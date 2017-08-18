package com.anriku.imcheck.MainInterface.View;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.Toast;

import com.anriku.imcheck.Adapter.FriendsApplyRecAdapter;
import com.anriku.imcheck.R;
import com.anriku.imcheck.databinding.ActivityFriendsApplyBinding;

import java.util.List;

public class FriendsApplyActivity extends AppCompatActivity {

    private ActivityFriendsApplyBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_friends_apply);

        getApplys();
    }

    private void getApplys() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        Intent intent = getIntent();
        List<String> names = intent.getStringArrayListExtra("names");
        List<String> reasons = intent.getStringArrayListExtra("reasons");
        Toast.makeText(this,String.valueOf(names.size()),Toast.LENGTH_SHORT).show();
        FriendsApplyRecAdapter adapter = new FriendsApplyRecAdapter(this, names, reasons);
        binding.friendsApplyRv.setLayoutManager(manager);
        binding.friendsApplyRv.setAdapter(adapter);
    }
}
