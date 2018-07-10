package com.anriku.imcheck.Adapter.viewholder;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.anriku.imcheck.R;
import com.anriku.imcheck.Utils.VideoActivity;
import com.bumptech.glide.Glide;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMVideoMessageBody;

import de.hdodenhof.circleimageview.CircleImageView;

public class VideoViewHolder extends BaseViewHolder {

    private CircleImageView circleImageView;
    private ImageView imageView;

    public VideoViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void onBindViewHolder(EMMessage emMessage) {
        if (getAccount(itemView.getContext()).equals(emMessage.getFrom())) {
            itemView.findViewById(R.id.civ_item_video_left).setVisibility(View.GONE);
            itemView.findViewById(R.id.iv_item_video_left).setVisibility(View.GONE);
            circleImageView = itemView.findViewById(R.id.civ_item_video_right);
            imageView = itemView.findViewById(R.id.iv_item_video_right);

        } else {
            itemView.findViewById(R.id.civ_item_video_right).setVisibility(View.GONE);
            itemView.findViewById(R.id.iv_item_video_right).setVisibility(View.GONE);
            circleImageView = itemView.findViewById(R.id.civ_item_video_left);
            imageView = itemView.findViewById(R.id.iv_item_video_left);
        }
        circleImageView.setVisibility(View.VISIBLE);
        imageView.setVisibility(View.VISIBLE);


        ViewGroup.LayoutParams params = imageView.getLayoutParams();
        params.width = 300;
        params.height = 300;
        imageView.setLayoutParams(params);

        final EMVideoMessageBody messageBody = (EMVideoMessageBody) emMessage.getBody();
        Glide.with(itemView.getContext()).load(messageBody.getRemoteUrl()).into(imageView);


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(itemView.getContext(), VideoActivity.class);
                intent.putExtra("video", messageBody);
                itemView.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getType() {
        return 0;
    }
}
