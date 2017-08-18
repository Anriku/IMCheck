package com.anriku.imcheck.Adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.anriku.imcheck.R;
import com.anriku.imcheck.databinding.FriendsSearchRecItemBinding;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.util.List;

/**
 * Created by Anriku on 2017/8/18.
 */

public class FriendsSearchRecAdapter extends RecyclerView.Adapter<FriendsSearchRecAdapter.ViewHolder> {

    private Context context;
    private List<String> names;
    private int lastOpen = -1;

    public FriendsSearchRecAdapter(Context context, List<String> names) {
        this.context = context;
        this.names = names;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (context == null) {
            context = parent.getContext();
        }
        FriendsSearchRecItemBinding binding = DataBindingUtil.inflate(LayoutInflater
                .from(context), R.layout.friends_search_rec_item, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.binding.friendsSearchRecItemTv.setText(names.get(position));
        holder.binding.friendsSearchRecItemBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.binding.friendsSearchRecItemEt.getVisibility() == View.GONE){
                    holder.binding.friendsSearchRecItemEt.setVisibility(View.VISIBLE);
                }else {
                    try {
                        EMClient.getInstance().contactManager().addContact(names.get(position),holder.binding.friendsSearchRecItemEt.getText().toString());
                        Toast.makeText(context,"发送请求成功",Toast.LENGTH_SHORT).show();
                    } catch (HyphenateException e) {
                        Toast.makeText(context,"添加好友失败,你们以及是好友啦",Toast.LENGTH_SHORT);
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return names.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private FriendsSearchRecItemBinding binding;

        public ViewHolder(FriendsSearchRecItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
