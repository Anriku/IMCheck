package com.anriku.imcheck.MainInterface.Presenter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import com.anriku.imcheck.Adapter.FriendsSearchRecAdapter;
import com.anriku.imcheck.MainInterface.Interface.ISearchFriendAct;
import com.anriku.imcheck.MainInterface.Interface.ISearchFriendPre;
import com.anriku.imcheck.databinding.ActivitySearchFriendBinding;
import com.wilddog.client.DataSnapshot;
import com.wilddog.client.SyncError;
import com.wilddog.client.SyncReference;
import com.wilddog.client.ValueEventListener;
import com.wilddog.client.WilddogSync;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Anriku on 2017/8/18.
 */

public class SearchFriendPresenter implements ISearchFriendPre {

    private ISearchFriendAct iSearchFriendAct;
    private ImageView searchIv;

    public SearchFriendPresenter(ISearchFriendAct iSearchFriendAct) {
        this.iSearchFriendAct = iSearchFriendAct;
    }

    @Override
    public void searchFriend(final Context context, final ActivitySearchFriendBinding binding) {

        //进行查询并列表显示
        binding.acAddFriendIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(binding.acAddFriendIv.getWindowToken(), 0);
                SyncReference reference = WilddogSync.getInstance().getReference("accounts");
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List<String> names = new ArrayList<>();
                        Iterator iterator = dataSnapshot.getChildren().iterator();
                        while (iterator.hasNext()) {
                            DataSnapshot snapshot = (DataSnapshot) iterator.next();
                            if (String.valueOf(snapshot.getValue()).contains(binding.acAddFriendEt.getText().toString())) {
                                names.add(String.valueOf(snapshot.getValue()));
                            }
                        }
                        LinearLayoutManager manager = new LinearLayoutManager(context);
                        FriendsSearchRecAdapter adapter = new FriendsSearchRecAdapter(context,names);
                        binding.acAddFriendRv.setLayoutManager(manager);
                        binding.acAddFriendRv.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(SyncError syncError) {

                    }
                });
            }
        });

        //设置查询的EditView
        binding.acAddFriendEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                binding.acAddFriendTv.setVisibility(View.VISIBLE);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //设置取消键
        binding.acAddFriendTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(binding.acAddFriendTv.getWindowToken(), 0);
                binding.acAddFriendEt.setText("");
                binding.acAddFriendTv.setVisibility(View.GONE);
            }
        });
    }
}
