package com.anriku.imcheck.Adapter;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anriku.imcheck.MainInterface.View.MessageActivity;
import com.anriku.imcheck.R;
import com.anriku.imcheck.databinding.GroupRecItemBinding;
import com.hyphenate.chat.EMGroup;

import java.util.List;

/**
 * Created by Anriku on 2017/8/22.
 */

public class GroupRecAdapter extends RecyclerView.Adapter<GroupRecAdapter.ViewHolder> {

    private Context context;
    private List<EMGroup> groups;

    public GroupRecAdapter(Context context, List<EMGroup> groups) {
        this.context = context;
        this.groups = groups;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (context == null) {
            context = parent.getContext();
        }
        GroupRecItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.group_rec_item, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.binding.groupRecItemTv.setText(groups.get(position).getGroupName());
        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MessageActivity.class);
                intent.putExtra("obj", groups.get(position).getGroupId());
                intent.putExtra("is_group", true);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return groups.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private GroupRecItemBinding binding;

        public ViewHolder(GroupRecItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
