package com.anriku.imcheck.MainInterface.View;

import android.databinding.DataBindingUtil;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.anriku.imcheck.MainInterface.Interface.ISearchAct;
import com.anriku.imcheck.MainInterface.Interface.ISearchPre;
import com.anriku.imcheck.MainInterface.Presenter.SearchPresenter;
import com.anriku.imcheck.R;
import com.anriku.imcheck.databinding.ActivitySearchBinding;

public class SearchActivity extends AppCompatActivity implements ISearchAct {

    private ActivitySearchBinding binding;
    private ISearchPre iSearchPre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_search);
        iSearchPre = new SearchPresenter(this);

        binding.acSearchTb.setTitle("");
        setSupportActionBar(binding.acSearchTb);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setHomeAsUpIndicator(R.mipmap.back);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        iSearchPre.search(this,binding);
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
