package com.anriku.imcheck.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anriku.imcheck.MainInterface.View.MessageActivity;
import com.anriku.imcheck.R;
import com.anriku.imcheck.Utils.PopupWindowUtil;
import com.anriku.imcheck.databinding.FriendsRecItemBinding;

import java.util.List;

/**
 * Created by Anriku on 2017/8/17.
 */

public class FriendsRecAdapter extends RecyclerView.Adapter<FriendsRecAdapter.ViewHolder> {

    private Context context;
    private List<String> names;

    public FriendsRecAdapter(Context context, List<String> names) {
        this.context = context;
        this.names = names;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (context == null) {
            context = parent.getContext();
        }
        FriendsRecItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.friends_rec_item, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.binding.friendsRecItemTv.setText(names.get(position));
        holder.binding.friendsRecItemTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MessageActivity.class);
                intent.putExtra("obj", names.get(position));
                context.startActivity(intent);
            }
        });

        holder.binding.friendsRecItemTv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                PopupWindowUtil popupWindowUtil = PopupWindowUtil.getInstance(R.layout.friends_manager,
                        R.style.Popupwindow, LayoutInflater.from(context), ((Activity) context).getWindow());
                popupWindowUtil.bottomWindow(holder.binding.friendsRecItemTv);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return names.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private FriendsRecItemBinding binding;

        public ViewHolder(FriendsRecItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
