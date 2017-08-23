package com.anriku.imcheck.Utils;

import android.media.MediaPlayer;
import android.widget.ImageView;
import android.widget.Toast;

import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMVoiceMessageBody;

import java.io.File;
import java.io.IOException;

/**
 * Created by Anriku on 2017/8/20.
 */

public class VoiceUtil {

    private MediaPlayer mediaPlayer;
    private boolean isPlaying;
    private ImageView playIv;

    public void startPlay(EMMessage message, ImageView imageView){
        EMVoiceMessageBody voiceMessageBody = (EMVoiceMessageBody) message.getBody();
        if (message.direct() == EMMessage.Direct.SEND){
            playVoice(voiceMessageBody.getLocalUrl(),message,imageView);
        }else {
            if (message.status() == EMMessage.Status.SUCCESS){
             playVoice(voiceMessageBody.getLocalUrl(),message,imageView);
            }else if (message.status() == EMMessage.Status.INPROGRESS){
                Toast.makeText(IMApplication.getContext(),"语音正在发送中", Toast.LENGTH_SHORT).show();
            }else if (message.status() == EMMessage.Status.FAIL){
                Toast.makeText(IMApplication.getContext(),"接收失败",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void playVoice(String localUrl, final EMMessage message, ImageView imageView) {
        File file = new File(localUrl);
        if (!file.exists() || !file.isFile()){
            Toast.makeText(IMApplication.getContext(),"语音文件不存在",Toast.LENGTH_SHORT).show();
            return;
        }

        String playMsgId = message.getMsgId();
        mediaPlayer = new MediaPlayer();

        try {
            mediaPlayer.setDataSource(localUrl);
            mediaPlayer.prepare();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    //因播放其它语音是已经停止,所以不需要再次调用停止
                    if (mediaPlayer == null){
                        return;
                    }
                    stopPlayVoice(message);
                }
            });
            isPlaying = true;
            mediaPlayer.start();
            playIv = imageView;
            showAnimation(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAnimation(EMMessage message) {

    }

    private void stopPlayVoice(EMMessage message) {
        if (mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        isPlaying = false;
    }
}
