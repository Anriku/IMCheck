package com.anriku.imcheck.Utils;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.anriku.imcheck.R;
import com.anriku.imcheck.databinding.ActivityImageBinding;
import com.bumptech.glide.Glide;

import uk.co.senab.photoview.PhotoView;

public class ImageActivity extends AppCompatActivity {

    private ActivityImageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_image);

        initView();
    }

    private void initView() {
        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        Glide.with(this).load(url).into(binding.acImagePv);
    }
}
