package com.anriku.imcheck.MainInterface.Model;

import java.io.Serializable;

/**
 * Created by Anriku on 2017/8/20.
 */

public class VideoModel implements Serializable{
    private String videoPath;
    private String thumbnailPath;
    private int length;

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public String getThumbnailPath() {
        return thumbnailPath;
    }

    public void setThumbnailPath(String thumbnailPath) {
        this.thumbnailPath = thumbnailPath;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }
}
