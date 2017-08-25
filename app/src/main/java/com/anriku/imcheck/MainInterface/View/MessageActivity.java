package com.anriku.imcheck.MainInterface.View;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;

import com.anriku.imcheck.MainInterface.Interface.IMessageAct;
import com.anriku.imcheck.MainInterface.Interface.IMessagePre;
import com.anriku.imcheck.MainInterface.Presenter.MessagePresenter;
import com.anriku.imcheck.MainInterface.View.GroupSet.GroupMoreActivity;
import com.anriku.imcheck.MainInterface.View.UserSet.UserSetActivity;
import com.anriku.imcheck.R;
import com.anriku.imcheck.Utils.ExitAndDissolveGroupCollector;
import com.anriku.imcheck.Utils.GetContentUtil;
import com.anriku.imcheck.Utils.PermissionUtil;
import com.anriku.imcheck.databinding.ActivityMessageBinding;

public class MessageActivity extends AppCompatActivity implements IMessageAct {

    private ActivityMessageBinding binding;
    private IMessagePre iMessagePre;
    private String obj;
    private boolean isGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_message);
        iMessagePre = new MessagePresenter(this);

        initView();

        iMessagePre.setChatObj(this, binding, obj, isGroup);
        iMessagePre.setMessageRecAdapter(this, binding);
        iMessagePre.chat(this, obj, binding, getSupportFragmentManager(), isGroup);
        iMessagePre.getHistory(this, obj, binding);
        iMessagePre.setMore(binding);
        iMessagePre.setSpeak(this, getWindow(), obj, binding, isGroup);
        iMessagePre.getImageFromAlbum(this, obj, binding);
        iMessagePre.sendVideo(this, getWindow(), binding, obj);
        iMessagePre.getFile(this, binding);
    }

    private void initView() {
        Intent intent = getIntent();
        obj = intent.getStringExtra("obj");
        isGroup = intent.getBooleanExtra("is_group", false);

        binding.acMessageTb.setTitle("");
        setSupportActionBar(binding.acMessageTb);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.mipmap.back);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        //用于解散或退出群
        ExitAndDissolveGroupCollector.addActivity(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionUtil.solvePermissionsResult(requestCode, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case GetContentUtil.CHOOSE_FROM_ALBUM: {
                String imagePath = GetContentUtil.getPath(data, MessageActivity.this, resultCode);
                iMessagePre.sendImage(obj, imagePath, LayoutInflater.from(MessageActivity.this), getWindow(), binding, isGroup);
                break;
            }
            case GetContentUtil.CHOOSE_FILE: {
                String filePath = GetContentUtil.getPath(data, MessageActivity.this, resultCode);
                iMessagePre.sendFile(MessageActivity.this, filePath, binding, obj, isGroup);
            }
            default:
                break;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (isGroup) {
            getMenuInflater().inflate(R.menu.message_group_menu, menu);
        } else {
            getMenuInflater().inflate(R.menu.message_friend_menu, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.message_friend_menu_more:
                startActivity(new Intent(this, UserSetActivity.class));
                break;
            case R.id.message_group_menu_more:
                Intent intent = new Intent(MessageActivity.this, GroupMoreActivity.class);
                intent.putExtra("id", obj);
                startActivity(intent);
                break;
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return true;
    }
}
