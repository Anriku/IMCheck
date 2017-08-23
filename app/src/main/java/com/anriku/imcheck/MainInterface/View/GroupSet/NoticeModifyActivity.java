package com.anriku.imcheck.MainInterface.View.GroupSet;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.anriku.imcheck.MainInterface.Interface.GroupSet.INoticeModifyAct;
import com.anriku.imcheck.MainInterface.Interface.GroupSet.INoticeModifyPre;
import com.anriku.imcheck.MainInterface.Presenter.GroupSet.NoticeModifyPresenter;
import com.anriku.imcheck.R;
import com.anriku.imcheck.databinding.ActivityNoticeModifyBinding;

public class NoticeModifyActivity extends AppCompatActivity implements INoticeModifyAct {

    private INoticeModifyPre iNoticeModifyPre;
    private ActivityNoticeModifyBinding binding;
    private String obj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_notice_modify);
        iNoticeModifyPre = new NoticeModifyPresenter(this);

        binding.acNoticeModifyTb.setTitle("");
        setSupportActionBar(binding.acNoticeModifyTb);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setHomeAsUpIndicator(R.mipmap.back);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();
        obj = intent.getStringExtra("id");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.notice_modify_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.notice_modify_finish:
                iNoticeModifyPre.finishWrite(this,binding,obj);
                break;
            case android.R.id.home:
                finish();
            default:
                break;
        }
        return true;
    }
}
