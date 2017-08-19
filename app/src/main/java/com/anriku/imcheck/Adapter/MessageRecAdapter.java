package com.anriku.imcheck.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.anriku.imcheck.R;
import com.anriku.imcheck.databinding.MessageRecItemBinding;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.exceptions.HyphenateException;

import java.util.List;

/**
 * Created by Anriku on 2017/8/18.
 */

public class MessageRecAdapter extends RecyclerView.Adapter<MessageRecAdapter.ViewHolder> {

    private Context context;
    private List<EMMessage> emMessages;

    public MessageRecAdapter(Context context, List<EMMessage> emMessages) {
        this.context = context;
        this.emMessages = emMessages;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (context == null) {
            context = parent.getContext();
        }
        MessageRecItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.message_rec_item, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        SharedPreferences pref = context.getSharedPreferences("account", Context.MODE_PRIVATE);
        String account = pref.getString("name", "");

        if (account.equals(emMessages.get(position).getFrom())) {
            holder.binding.messageRecItemRightTv.setText(String.valueOf(emMessages.get(position).getBody().toString().split("\"")[1]));
            holder.binding.messageRecItemRightTv.setVisibility(View.VISIBLE);
            holder.binding.messageRecItemRightCiv.setVisibility(View.VISIBLE);
            holder.binding.messageRecItemLeftTv.setVisibility(View.GONE);
            holder.binding.messageRecItemLeftCiv.setVisibility(View.GONE);
        } else {
            holder.binding.messageRecItemRightTv.setVisibility(View.GONE);
            holder.binding.messageRecItemRightCiv.setVisibility(View.GONE);
            holder.binding.messageRecItemLeftTv.setVisibility(View.VISIBLE);
            holder.binding.messageRecItemLeftCiv.setVisibility(View.VISIBLE);

            holder.binding.messageRecItemLeftTv.setText(emMessages.get(position).getBody().toString().split("\"")[1]);
        }
    }

    @Override
    public int getItemCount() {
        return emMessages.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private MessageRecItemBinding binding;

        public ViewHolder(MessageRecItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
