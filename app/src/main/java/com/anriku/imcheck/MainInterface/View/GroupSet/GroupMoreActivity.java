package com.anriku.imcheck.MainInterface.View.GroupSet;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.anriku.imcheck.MainInterface.Interface.IGroupMoreAct;
import com.anriku.imcheck.MainInterface.Interface.IGroupMorePre;
import com.anriku.imcheck.MainInterface.Presenter.GroupSet.GroupMorePresenter;
import com.anriku.imcheck.R;
import com.anriku.imcheck.Utils.ExitAndDissolveGroupCollector;
import com.anriku.imcheck.databinding.ActivityGroupMoreBinding;
import com.hyphenate.chat.EMClient;

public class GroupMoreActivity extends AppCompatActivity implements IGroupMoreAct {

    private IGroupMorePre iGroupMorePre;
    private ActivityGroupMoreBinding binding;
    private String obj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        iGroupMorePre = new GroupMorePresenter(this);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_group_more);

        //用于解散和退出群
        ExitAndDissolveGroupCollector.addActivity(this);

        binding.acGroupMoreTb.setTitle("");
        setSupportActionBar(binding.acGroupMoreTb);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setHomeAsUpIndicator(R.mipmap.back);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();
        obj = intent.getStringExtra("id");
        iGroupMorePre.adminsSet(this,binding,obj);
        iGroupMorePre.exitOrDissolveGroup(this,binding,obj);
        iGroupMorePre.modifyGroup(this,binding,obj);
        iGroupMorePre.lookNotice(this,binding,obj);
        iGroupMorePre.inviteNewMember(this,binding,obj);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return true;
    }
}
