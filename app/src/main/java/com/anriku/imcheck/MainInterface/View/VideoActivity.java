package com.anriku.imcheck.MainInterface.View;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.anriku.imcheck.R;
import com.anriku.imcheck.Utils.VideoUtil;
import com.anriku.imcheck.databinding.ActivityVideoBinding;
import com.hyphenate.chat.EMVideoMessageBody;
import com.hyphenate.chat.adapter.message.EMAVideoMessageBody;

public class VideoActivity extends AppCompatActivity {

    private ActivityVideoBinding binding;
    private VideoUtil videoUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_video);

        initView();
    }

    private void initView() {
        Intent intent = getIntent();
        EMVideoMessageBody messageBody = intent.getParcelableExtra("video");
        String url = messageBody.getLocalUrl();

        if (TextUtils.isEmpty(url)){
            url = messageBody.getRemoteUrl();
        }

        videoUtil = new VideoUtil(binding.acVideoSv,url);
        videoUtil.play();
        binding.acVideoSv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        videoUtil.recycle();
    }
}
