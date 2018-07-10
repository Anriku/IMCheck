package com.anriku.imcheck.Adapter.viewholder;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.hyphenate.chat.EMMessage;

public abstract class BaseViewHolder extends RecyclerView.ViewHolder {

    public static final int TXT = 0;
    public static final int IMAGE = 1;
    public static final int VIDEO = 2;
    public static final int LOCATION = 3;
    public static final int VOICE = 4;
    public static final int FILE = 5;
    public static final int CMD = 6;

    public String getAccount(Context context){
        SharedPreferences pref = context.getSharedPreferences("account", Context.MODE_PRIVATE);
        String account = pref.getString("name", "");
        return account;
    }

    public abstract void onBindViewHolder(EMMessage emMessage);

    public BaseViewHolder(View itemView) {
        super(itemView);
    }

    public abstract int getType();
}
