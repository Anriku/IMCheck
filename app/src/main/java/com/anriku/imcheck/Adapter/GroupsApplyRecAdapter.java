package com.anriku.imcheck.Adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anriku.imcheck.MainInterface.Model.GroupApply;
import com.anriku.imcheck.R;
import com.anriku.imcheck.databinding.GroupsApplyRecItemBinding;

/**
 * Created by Anriku on 2017/8/23.
 */

public class GroupsApplyRecAdapter extends RecyclerView.Adapter<GroupsApplyRecAdapter.ViewHolder> {

    private Context context;
    private GroupApply groupApply;

    public GroupsApplyRecAdapter(Context context, GroupApply groupApply) {
        this.context = context;
        this.groupApply = groupApply;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (context == null) {
            context = parent.getContext();
        }

        GroupsApplyRecItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.groups_apply_rec_item, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.binding.groupsApplyTv.setText(groupApply.getGroupNames().get(position) + "\n" + groupApply.getReasons().get(position));
    }

    @Override
    public int getItemCount() {
        return groupApply.getGroupIds().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private GroupsApplyRecItemBinding binding;

        public ViewHolder(GroupsApplyRecItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
