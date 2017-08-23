package com.anriku.imcheck.Adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.anriku.imcheck.MainInterface.Model.FriendApply;
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
    private FriendApply friendApply;

    public FriendsApplyRecAdapter(Context context, FriendApply friendApply) {
        this.context = context;
        this.friendApply = friendApply;
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
    public void onBindViewHolder(final ViewHolder holder, final int position) {


        holder.binding.friendsApplyTv.setText("好友申请:" + friendApply.getNames().get(position) + "\n" + "申请备注:" + friendApply.getReasons().get(position));

        holder.binding.friendsApplyAgree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    EMClient.getInstance().contactManager().acceptInvitation(friendApply.getNames().get(position));
                    friendApply.getNames().remove(position);
                    notifyItemRemoved(position);
                    notifyDataSetChanged();
                    Toast.makeText(context, "已同意", Toast.LENGTH_SHORT).show();
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
                    EMClient.getInstance().contactManager().declineInvitation(friendApply.getNames().get(position));
                    friendApply.getNames().remove(position);
                    notifyItemRemoved(position);
                    notifyDataSetChanged();
                    Toast.makeText(context, "已拒绝", Toast.LENGTH_SHORT).show();
                } catch (HyphenateException e) {
                    Toast.makeText(context, "拒绝好友失败", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return friendApply.getNames().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private FriendsApplyRecItemBinding binding;

        public ViewHolder(FriendsApplyRecItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
