package com.anriku.imcheck.Utils;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.anriku.imcheck.MainInterface.Model.VideoModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anriku on 2017/8/20.
 */

public class GetVideoDataUtil {

    public static List<VideoModel> getVideoData(Context context) {
        List<VideoModel> videoModels = new ArrayList<>();

        String[] thumbColumns = {MediaStore.Video.Thumbnails.DATA, MediaStore.Video.Thumbnails.VIDEO_ID};
        String[] mediaColumns = {MediaStore.Video.Media._ID, MediaStore.Video.Media.DURATION, MediaStore.Video.Media.DATA};

        Cursor cursor = context.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                mediaColumns, null, null, null);

        if (cursor == null) {
            return videoModels;
        }

        if (cursor.moveToFirst()) {
            do {
                VideoModel videoModel = new VideoModel();
                int id = cursor.getInt(cursor.getColumnIndex(MediaStore.Video.Media._ID));
                Cursor thumbCursor = context.getContentResolver().query(MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI,
                        thumbColumns, MediaStore.Video.Thumbnails.VIDEO_ID + "=" + id, null, null);
                if (thumbCursor.moveToFirst()) {
                    videoModel.setThumbnailPath(thumbCursor.getString(thumbCursor.getColumnIndex(MediaStore.Video.Thumbnails.DATA)));
                }

                videoModel.setVideoPath(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)));
                videoModel.setLength(cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)));
                videoModels.add(videoModel);
            } while (cursor.moveToNext());

        }
        return videoModels;
    }
}
