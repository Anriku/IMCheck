package com.anriku.imcheck.MainInterface.View;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anriku.imcheck.MainInterface.Interface.IChatFrg;
import com.anriku.imcheck.MainInterface.Interface.IChatPre;
import com.anriku.imcheck.MainInterface.Presenter.ChatPresenter;
import com.anriku.imcheck.R;
import com.anriku.imcheck.databinding.FragmentChatBinding;

/**
 * Created by Anriku on 2017/8/16.
 */

public class ChatFragment extends Fragment implements IChatFrg {

    private FragmentChatBinding binding;
    private IChatPre iChatPre;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_chat,container,false);
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        iChatPre.getConversations(getContext(),binding);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();

        iChatPre.getConversations(getContext(),binding);
        iChatPre.refreshConversations(getContext(),binding);
    }

    private void initView() {
        iChatPre = new ChatPresenter(this);
    }

}
