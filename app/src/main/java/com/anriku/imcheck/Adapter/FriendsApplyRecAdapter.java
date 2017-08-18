package com.anriku.imcheck.Adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.anriku.imcheck.R;
import com.anriku.imcheck.databinding.FriendsApplyRecItemBinding;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.util.List;

/**
 * Created by Anriku on 2017/8/18.
 */

public class FriendsApplyRecAdapter extends RecyclerView.Adapter<FriendsApplyRecAdapter.ViewHolder> {

    private Context context;
    private List<String> names;
    private List<String> reasons;

    public FriendsApplyRecAdapter(Context context, List<String> names, List<String> reasons) {
        this.context = context;
        this.names = names;
        this.reasons = reasons;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (context == null) {
            context = parent.getContext();
        }
        FriendsApplyRecItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.friends_apply_rec_item, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.binding.friendsApplyTv.setText("好友申请:" + names.get(position) + "\n" + "申请备注:" + reasons.get(position));
        holder.binding.friendsApplyAgree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    EMClient.getInstance().contactManager().acceptInvitation(names.get(position));
                } catch (HyphenateException e) {
                    Toast.makeText(context, "同意好友失败", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });

        holder.binding.friendsApplyRefuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    EMClient.getInstance().contactManager().declineInvitation(names.get(position));
                } catch (HyphenateException e) {
                    Toast.makeText(context, "拒绝好友失败", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return names.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private FriendsApplyRecItemBinding binding;

        public ViewHolder(FriendsApplyRecItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
