package com.anriku.imcheck.Adapter.viewholder;

import android.view.View;
import android.widget.TextView;

import com.anriku.imcheck.R;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;

import de.hdodenhof.circleimageview.CircleImageView;

public class TxtViewHolder extends BaseViewHolder {

    private CircleImageView circleImageView;
    private TextView textView;

    public TxtViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void onBindViewHolder(EMMessage emMessage) {
        if (getAccount(itemView.getContext()).equals(emMessage.getFrom())) {
            itemView.findViewById(R.id.civ_item_txt_left).setVisibility(View.GONE);
            itemView.findViewById(R.id.tv_item_txt_left).setVisibility(View.GONE);
            circleImageView = itemView.findViewById(R.id.civ_item_txt_right);
            textView = itemView.findViewById(R.id.tv_item_txt_right);
        } else {
            itemView.findViewById(R.id.civ_item_txt_right).setVisibility(View.GONE);
            itemView.findViewById(R.id.tv_item_txt_right).setVisibility(View.GONE);
            circleImageView = itemView.findViewById(R.id.civ_item_txt_left);
            textView = itemView.findViewById(R.id.tv_item_txt_left);
        }

        circleImageView.setVisibility(View.VISIBLE);
        textView.setVisibility(View.VISIBLE);

        EMTextMessageBody messageBody = (EMTextMessageBody) emMessage.getBody();
        textView.setText(messageBody.getMessage());
    }

    @Override
    public int getType() {
        return BaseViewHolder.TXT;
    }
}
