package com.anriku.imcheck.MainInterface.View.GroupSet;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.anriku.imcheck.MainInterface.Interface.GroupSet.INoticeAct;
import com.anriku.imcheck.MainInterface.Interface.GroupSet.INoticePre;
import com.anriku.imcheck.MainInterface.Presenter.GroupSet.NoticePresenter;
import com.anriku.imcheck.R;
import com.anriku.imcheck.databinding.ActivityNoticeBinding;

public class NoticeActivity extends AppCompatActivity implements INoticeAct{

    private INoticePre iNoticePre;
    private ActivityNoticeBinding binding;
    private String obj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_notice);
        iNoticePre = new NoticePresenter(this);

        binding.acNoticeTb.setTitle("");
        setSupportActionBar(binding.acNoticeTb);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setHomeAsUpIndicator(R.mipmap.back);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();
        obj = intent.getStringExtra("id");

        iNoticePre.getNotice(this,binding,obj);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.notice_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.notice_menu_modify:
                Intent intent = new Intent(this,NoticeModifyActivity.class);
                intent.putExtra("id",obj);
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
