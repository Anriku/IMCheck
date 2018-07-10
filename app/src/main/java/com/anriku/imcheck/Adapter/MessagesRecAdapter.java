package com.anriku.imcheck.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anriku.imcheck.Adapter.viewholder.BaseViewHolder;
import com.anriku.imcheck.Adapter.viewholder.FileViewHolder;
import com.anriku.imcheck.Adapter.viewholder.ImageViewHolder;
import com.anriku.imcheck.Adapter.viewholder.TxtViewHolder;
import com.anriku.imcheck.Adapter.viewholder.VideoViewHolder;
import com.anriku.imcheck.Adapter.viewholder.VoiceViewHolder;
import com.anriku.imcheck.R;
import com.hyphenate.chat.EMMessage;

import java.util.List;

public class MessagesRecAdapter extends RecyclerView.Adapter<BaseViewHolder> {


    private Context context;
    private List<EMMessage> emMessages;

    public MessagesRecAdapter(Context context, List<EMMessage> emMessages) {
        this.context = context;
        this.emMessages = emMessages;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        SharedPreferences pref = context.getSharedPreferences("account", Context.MODE_PRIVATE);
        String account = pref.getString("name", "");

        switch (viewType) {
            case BaseViewHolder.CMD:
                break;
            case BaseViewHolder.FILE:
                view = LayoutInflater.from(context).inflate(R.layout.item_file, parent, false);
                return new FileViewHolder(view);
            case BaseViewHolder.IMAGE:
                view = LayoutInflater.from(context).inflate(R.layout.item_image, parent, false);
                return new ImageViewHolder(view);
            case BaseViewHolder.LOCATION:
                break;
            case BaseViewHolder.TXT:
                view = LayoutInflater.from(context).inflate(R.layout.item_txt, parent, false);
                return new TxtViewHolder(view);
            case BaseViewHolder.VIDEO:
                view = LayoutInflater.from(context).inflate(R.layout.item_video, parent, false);
                return new VideoViewHolder(view);
            case BaseViewHolder.VOICE:
                view = LayoutInflater.from(context).inflate(R.layout.item_voice,parent,false);
                return new VoiceViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.onBindViewHolder(emMessages.get(position));
    }

    @Override
    public int getItemCount() {
        return emMessages.size();
    }


    @Override
    public int getItemViewType(int position) {
        switch (emMessages.get(position).getType()) {
            case CMD:
                return BaseViewHolder.CMD;
            case TXT:
                return BaseViewHolder.TXT;
            case FILE:
                return BaseViewHolder.FILE;
            case IMAGE:
                return BaseViewHolder.IMAGE;
            case VIDEO:
                return BaseViewHolder.VIDEO;
            case VOICE:
                return BaseViewHolder.VOICE;
            case LOCATION:
                return BaseViewHolder.LOCATION;
        }
        return 0;
    }
}
