package com.anriku.imcheck.Adapter.viewholder;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.anriku.imcheck.R;
import com.anriku.imcheck.Utils.VoiceUtil;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMVoiceMessageBody;

import de.hdodenhof.circleimageview.CircleImageView;

public class VoiceViewHolder extends BaseViewHolder {

    private CircleImageView circleImageView;
    private TextView textView;
    private ImageView imageView;


    public VoiceViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void onBindViewHolder(final EMMessage emMessage) {
        if (getAccount(itemView.getContext()).equals(emMessage.getFrom())) {
            itemView.findViewById(R.id.civ_item_voice_left).setVisibility(View.GONE);
            itemView.findViewById(R.id.iv_item_voice_left).setVisibility(View.GONE);
            itemView.findViewById(R.id.tv_item_voice_left).setVisibility(View.GONE);
            circleImageView = itemView.findViewById(R.id.civ_item_voice_right);
            imageView = itemView.findViewById(R.id.iv_item_voice_right);
            textView = itemView.findViewById(R.id.tv_item_voice_right);
        } else {
            itemView.findViewById(R.id.civ_item_voice_right).setVisibility(View.GONE);
            itemView.findViewById(R.id.iv_item_voice_right).setVisibility(View.GONE);
            itemView.findViewById(R.id.tv_item_voice_right).setVisibility(View.GONE);
            circleImageView = itemView.findViewById(R.id.civ_item_voice_left);
            imageView = itemView.findViewById(R.id.iv_item_voice_left);
            textView = itemView.findViewById(R.id.tv_item_voice_left);
        }

        circleImageView.setVisibility(View.VISIBLE);
        textView.setVisibility(View.VISIBLE);
        imageView.setVisibility(View.VISIBLE);


        //防止重用后,文字还在
        textView.setText("");

        //进行语音播放
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VoiceUtil util = new VoiceUtil();
                util.startPlay(emMessage, imageView);
            }
        });
        EMVoiceMessageBody messageBody = (EMVoiceMessageBody) emMessage.getBody();
        //设置语音时间
        textView.setText(String.valueOf(messageBody.getLength()) + "\"");

        //设置图片大小
        ViewGroup.LayoutParams params = imageView.getLayoutParams();
        params.width = 60;
        params.height = 60;
        imageView.setLayoutParams(params);

        //防止重用点击事件
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }

    @Override
    public int getType() {
        return 0;
    }
}
