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
import com.anriku.imcheck.databinding.ChatRecItemBinding;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;

import java.text.DateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by Anriku on 2017/8/19.
 */

public class ChatRecAdapter extends RecyclerView.Adapter<ChatRecAdapter.ViewHolder> {

    private Context context;
    private List<EMConversation> emConversations;

    public ChatRecAdapter(Context context, List<EMConversation> emConversations) {
        this.context = context;
        this.emConversations = emConversations;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (context == null) {
            context = parent.getContext();
        }
        ChatRecItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.chat_rec_item
                , parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.binding.chatRecItemObj.setText(emConversations.get(position).getLastMessage().getFrom());

        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.FULL, Locale.CHINA);

        holder.binding.chatRecItemTime.setText(String.valueOf(dateFormat.format(emConversations.get(position).getLastMessage().getMsgTime())));

        if (emConversations.get(position).getLastMessage().getType() == EMMessage.Type.TXT) {
            EMTextMessageBody messageBody = (EMTextMessageBody) emConversations.get(position).getLastMessage().getBody();
            holder.binding.chatRecItemMsg.setText(messageBody.getMessage());
        } else if (emConversations.get(position).getLastMessage().getType() == EMMessage.Type.VOICE) {
            holder.binding.chatRecItemMsg.setText("[语音]");
        } else if (emConversations.get(position).getLastMessage().getType() == EMMessage.Type.IMAGE) {
            holder.binding.chatRecItemMsg.setText("[图片]");
        }

        final int pos = position;
        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MessageActivity.class);
                intent.putExtra("is_group", emConversations.get(pos).isGroup());
                intent.putExtra("obj", emConversations.get(pos).conversationId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return emConversations.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ChatRecItemBinding binding;

        public ViewHolder(ChatRecItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
