package com.anriku.imcheck.MainInterface.View;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.anriku.imcheck.MainInterface.Interface.IMessageAct;
import com.anriku.imcheck.MainInterface.Interface.IMessagePre;
import com.anriku.imcheck.MainInterface.Presenter.MessagePresenter;
import com.anriku.imcheck.R;
import com.anriku.imcheck.databinding.ActivityMessageBinding;

public class MessageActivity extends AppCompatActivity implements IMessageAct{

    private ActivityMessageBinding binding;
    private IMessagePre iMessagePre;
    private String obj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_message);
        iMessagePre = new MessagePresenter(this);

        initView();

        iMessagePre.chat(this,obj,binding);
        iMessagePre.getHistory(this,obj,binding);
    }

    private void initView() {
        Intent intent = getIntent();
        obj = intent.getStringExtra("obj");
        binding.acMessageObjTv.setText(obj);
        setSupportActionBar(binding.acMessageTb);
    }
}
