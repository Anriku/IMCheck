package com.anriku.imcheck.MainInterface.View;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.anriku.imcheck.MainInterface.Interface.ISearchFriendAct;
import com.anriku.imcheck.MainInterface.Interface.ISearchFriendPre;
import com.anriku.imcheck.MainInterface.Presenter.SearchFriendPresenter;
import com.anriku.imcheck.R;
import com.anriku.imcheck.databinding.ActivitySearchFriendBinding;

public class SearchFriendActivity extends AppCompatActivity implements ISearchFriendAct {

    private ActivitySearchFriendBinding binding;
    private ISearchFriendPre iSearchFriendPre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_search_friend);
        iSearchFriendPre = new SearchFriendPresenter(this);

        iSearchFriendPre.searchFriend(this,binding);
    }

}
