package com.anriku.imcheck.Adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anriku.imcheck.R;
import com.anriku.imcheck.databinding.ActivityFileBinding;
import com.anriku.imcheck.databinding.FileRecItemBinding;
import com.hyphenate.chat.EMMucSharedFile;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anriku on 2017/8/23.
 */

public class FileRecAdapter extends RecyclerView.Adapter<FileRecAdapter.ViewHolder> {

    private Context context;
    private List<EMMucSharedFile> files = new ArrayList<>();//初始化避免为空的情况

    public FileRecAdapter(Context context, List<EMMucSharedFile> files) {
        this.context = context;
        this.files = files;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (context == null) {
            context = parent.getContext();
        }
        FileRecItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.file_rec_item
                , parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.binding.fileRecItemNameTv.setText(files.get(position).getFileName());
        holder.binding.fileRecItemOwnerTv.setText(files.get(position).getFileOwner());
        holder.binding.fileRecItemSizeTv.setText(String.valueOf(files.get(position).getFileSize()));
        holder.binding.fileRecItemUpdateTimeTv.setText(String.valueOf(files.get(position).getFileUpdateTime()));
    }

    @Override
    public int getItemCount() {
        return files.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private FileRecItemBinding binding;

        public ViewHolder(FileRecItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
