package com.anriku.imcheck.Adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anriku.imcheck.MainInterface.Model.VideoModel;
import com.anriku.imcheck.R;
import com.anriku.imcheck.Utils.PopupWindowUtil;
import com.anriku.imcheck.databinding.VideoRecItemBinding;
import com.bumptech.glide.Glide;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;

import java.util.List;

/**
 * Created by Anriku on 2017/8/20.
 */

public class VideoRecAdapter extends RecyclerView.Adapter<VideoRecAdapter.ViewHolder> {

    private Context context;
    private String obj;
    private List<VideoModel> videoModels;
    private PopupWindowUtil popupWindowUtil;
    //用于更新消息
    private List<EMMessage> emMessages;
    private MessagesRecAdapter adapter;

    public VideoRecAdapter(Context context, String obj, List<VideoModel> videoModels, PopupWindowUtil popupWindowUtil
    ,List<EMMessage> emMessages,MessagesRecAdapter adapter) {
        this.context = context;
        this.obj = obj;
        this.videoModels = videoModels;
        this.popupWindowUtil = popupWindowUtil;
        this.emMessages = emMessages;
        this.adapter = adapter;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (context == null) {
            context = parent.getContext();
        }
        VideoRecItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.video_rec_item, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Glide.with(context).load(videoModels.get(position).getThumbnailPath()).into(holder.binding.videoRecItemIv);
        holder.binding.videoRecItemPathTv.setText(videoModels.get(position).getVideoPath());
        holder.binding.videoRecItemLengthTv.setText(String.valueOf(videoModels.get(position).getLength()));
        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EMMessage emMessage = EMMessage.createVideoSendMessage(videoModels.get(position).getVideoPath()
                        , videoModels.get(position).getThumbnailPath(), videoModels.get(position).getLength(), obj);
                EMClient.getInstance().chatManager().sendMessage(emMessage);
                Log.e("MessageLength",String.valueOf(emMessages.size()));
                emMessages.add(emMessage);
                adapter.notifyDataSetChanged();
                Log.e("MessageLength",String.valueOf(emMessages.size()));
                popupWindowUtil.getPopupWindow().dismiss();
            }
        });
    }

    @Override
    public int getItemCount() {
        return videoModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        VideoRecItemBinding binding;

        public ViewHolder(VideoRecItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
