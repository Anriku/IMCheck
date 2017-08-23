package com.anriku.imcheck.MainInterface.View.GroupSet;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.anriku.imcheck.Adapter.FriendsRecAdapter;
import com.anriku.imcheck.MainInterface.Interface.GroupSet.IInviteMembersAct;
import com.anriku.imcheck.MainInterface.Interface.GroupSet.IInviteMembersPre;
import com.anriku.imcheck.MainInterface.Presenter.GroupSet.InviteMembersPresenter;
import com.anriku.imcheck.R;
import com.anriku.imcheck.databinding.ActivityInviteMembersBinding;
import com.hyphenate.chat.EMClient;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class InviteMembersActivity extends AppCompatActivity implements IInviteMembersAct {

    private ActivityInviteMembersBinding binding;
    private IInviteMembersPre iInviteMembersPre;
    private String obj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_invite_members);
        iInviteMembersPre = new InviteMembersPresenter(this);

        setSupportActionBar(binding.acInviteMembersTb);

        Intent intent = getIntent();
        obj = intent.getStringExtra("id");
        iInviteMembersPre.getFriends(this,binding, obj);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.invite_members_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.invite_members_menu_finish:
                iInviteMembersPre.inviteFriends(InviteMembersActivity.this,binding,obj);
                break;
            default:
                break;
        }
        return true;
    }
}
