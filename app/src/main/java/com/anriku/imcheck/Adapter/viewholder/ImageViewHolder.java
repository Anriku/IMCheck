package com.anriku.imcheck.Adapter.viewholder;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.anriku.imcheck.R;
import com.anriku.imcheck.Utils.ImageActivity;
import com.bumptech.glide.Glide;
import com.hyphenate.chat.EMImageMessageBody;
import com.hyphenate.chat.EMMessage;

import de.hdodenhof.circleimageview.CircleImageView;

public class ImageViewHolder extends BaseViewHolder {

    private CircleImageView circleImageView;
    private ImageView imageView;

    public ImageViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void onBindViewHolder(EMMessage emMessage) {
        if (getAccount(itemView.getContext()).equals(emMessage.getFrom())) {
            itemView.findViewById(R.id.civ_item_image_left).setVisibility(View.GONE);
            itemView.findViewById(R.id.iv_item_image_left).setVisibility(View.GONE);
            circleImageView = itemView.findViewById(R.id.civ_item_image_right);
            imageView = itemView.findViewById(R.id.iv_item_image_right);

        }else {
            itemView.findViewById(R.id.civ_item_image_right).setVisibility(View.GONE);
            itemView.findViewById(R.id.iv_item_image_right).setVisibility(View.GONE);
            circleImageView = itemView.findViewById(R.id.civ_item_image_left);
            imageView = itemView.findViewById(R.id.iv_item_image_left);
        }
        circleImageView.setVisibility(View.VISIBLE);
        imageView.setVisibility(View.VISIBLE);


        ViewGroup.LayoutParams params = imageView.getLayoutParams();
        params.width = 300;
        params.height = 300;
        imageView.setLayoutParams(params);

        final EMImageMessageBody messageBody = (EMImageMessageBody) emMessage.getBody();
        Glide.with(itemView.getContext()).load(messageBody.getRemoteUrl()).into(imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(itemView.getContext(),ImageActivity.class);
                intent.putExtra("url",messageBody.getRemoteUrl());
                itemView.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getType() {
        return BaseViewHolder.IMAGE;
    }
}
