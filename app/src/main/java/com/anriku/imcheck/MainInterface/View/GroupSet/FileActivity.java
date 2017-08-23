package com.anriku.imcheck.MainInterface.View.GroupSet;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.anriku.imcheck.MainInterface.Interface.GroupSet.IFileAct;
import com.anriku.imcheck.MainInterface.Interface.GroupSet.IFilePre;
import com.anriku.imcheck.MainInterface.Presenter.GroupSet.FilePresenter;
import com.anriku.imcheck.R;
import com.anriku.imcheck.databinding.ActivityFileBinding;

public class FileActivity extends AppCompatActivity implements IFileAct {

    private IFilePre iFilePre;
    private ActivityFileBinding binding;
    private String obj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_file);
        iFilePre = new FilePresenter(this);
        Intent intent = getIntent();
        obj = intent.getStringExtra("id");

        binding.acFileTb.setTitle("");
        setSupportActionBar(binding.acFileTb);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setHomeAsUpIndicator(R.mipmap.back);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        iFilePre.getFiles(this,binding,obj);
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
