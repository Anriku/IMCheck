package com.anriku.imcheck.Adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.anriku.imcheck.R;
import com.anriku.imcheck.databinding.SpinnerItemBinding;

/**
 * Created by Anriku on 2017/8/22.
 */

public class SpinnerAdapter extends BaseAdapter {

    private Context context;
    private String[] types;

    public SpinnerAdapter(Context context, String[] types) {
        this.context = context;
        this.types = types;
    }

    @Override
    public int getCount() {
        return types.length;
    }

    @Override
    public Object getItem(int i) {
        return types[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        SpinnerItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.spinner_item, viewGroup, false);
        view = binding.getRoot();
        binding.spinnerItemTv.setText(types[i]);
        return view;
    }
}
