package com.anriku.imcheck.Utils;

import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.util.TimeUtils;
import android.util.Log;

import java.io.File;
import java.io.IOException;

/**
 * Created by Anriku on 2017/8/19.
 */

public class AudioRecorderUtils {

    //文件路径
    private String filePath;
    //文件夹路径
    private String folderPath;
    private MediaRecorder mediaRecorder;
    //最大录音时间
    private static final int MAX_LENGTH = 1000 * 60 * 10;

    private OnAudioStatusUpdateListener audioStatusUpdateListener;

    private long startTime;

    private long endTime;

    private final Handler handler = new Handler();
    private Runnable upDateMicMicStatusTimer = new Runnable() {
        @Override
        public void run() {
            updateMicStatus();
        }
    };

    private int BASE = 1;
    private int SPACE = 100;

    public AudioRecorderUtils() {
        this(Environment.getExternalStorageDirectory() + "/record/");
    }

    public AudioRecorderUtils(String folderPath) {
        File path = new File(folderPath);
        if (!path.exists())
            path.mkdirs();

        this.folderPath = folderPath;
    }


    public void startRecord() {
        if (mediaRecorder == null) {
            //1.实例化MediaRecorder
            mediaRecorder = new MediaRecorder();
            try {
                //setAudioSource/setVideoSource
                mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                //2.设置音频文件的编码:AAC/AMR_NB/AMR_MB/Default
                mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
                //2.设置输出文件的格式
                mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                filePath = folderPath + System.currentTimeMillis() + ".amr";
                //3.准备
                mediaRecorder.setOutputFile(filePath);
                mediaRecorder.setMaxDuration(MAX_LENGTH);
                mediaRecorder.prepare();
                //4.开始啦
                mediaRecorder.start();
                //获取开始滴时间
                startTime = System.currentTimeMillis();
                Log.d("startTime", String.valueOf(startTime));
                updateMicStatus();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public long stopRecord() {
        if (mediaRecorder == null)
            return 0L;
        endTime = System.currentTimeMillis();

        //在5.0以上调用stop会报错,这里进行一下异常的捕获
        try {
            mediaRecorder.stop();
            mediaRecorder.reset();
            mediaRecorder.release();
            mediaRecorder = null;

            audioStatusUpdateListener.onStop(filePath);

        } catch (RuntimeException e) {
            mediaRecorder.reset();
            mediaRecorder.release();
            mediaRecorder = null;
            audioStatusUpdateListener.onStop(filePath);

            File file = new File(filePath);
            if (file.exists())
                file.delete();

        }
        return endTime - startTime;
    }

    public void cancelRecord() {
        try {
            mediaRecorder.stop();
            mediaRecorder.reset();
            mediaRecorder.release();
            mediaRecorder = null;

        } catch (RuntimeException e) {
            mediaRecorder.reset();
            mediaRecorder.release();
            mediaRecorder = null;
        }
        File file = new File(filePath);
        if (file.exists())
            file.delete();

    }

    public void setOnAudioStatusUpdateListener(OnAudioStatusUpdateListener audioStatusUpdateListener) {
        this.audioStatusUpdateListener = audioStatusUpdateListener;
    }

    private void updateMicStatus() {
        if (mediaRecorder != null) {
            double ratio = mediaRecorder.getMaxAmplitude() / BASE;
            double db = 0;
            if (ratio > 1) {
                db = 20 * Math.log10(ratio);
                if (null != audioStatusUpdateListener) {
                    audioStatusUpdateListener.onUpdate(db * 100, getTime(System.currentTimeMillis() - startTime));
                }
            }
            handler.postDelayed(upDateMicMicStatusTimer, SPACE);
        }
    }

    public interface OnAudioStatusUpdateListener {
        void onUpdate(double db, String time);

        void onStop(String filePath);
    }


    public String getFilePath() {
        return filePath;
    }

    //进行时间的换算
    private String getTime(long time){
        long sec = time / 1000;
        long min = sec / 60;
        sec = sec % 60;
        return String.valueOf(min) + ":" + String.valueOf(sec);
    }


    public int getLength(){
        return (int) ((endTime - startTime) / 1000);
    }

}
