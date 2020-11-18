package com.huashengfu.StemCellsManager.entity.response.goods;

import java.io.Serializable;

/*
{
	"videoHeight": 1280,
	"videoWidth": 720,
	"size": "1.62MB",
	"ms": 13500,
	"url": "http://test.huashengfu.cn:7777/upload/goods/video/b3786ae679cb4101bfb118756ea59e46.mp4",
	"format": "mov"
}
 */
public class UploadVideo implements Serializable {

    private int videoHeight;
    private int videoWidth;
    private String size;
    private long ms;
    private String url;
    private String format;

    public int getVideoHeight() {
        return videoHeight;
    }

    public void setVideoHeight(int videoHeight) {
        this.videoHeight = videoHeight;
    }

    public int getVideoWidth() {
        return videoWidth;
    }

    public void setVideoWidth(int videoWidth) {
        this.videoWidth = videoWidth;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public long getMs() {
        return ms;
    }

    public void setMs(long ms) {
        this.ms = ms;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }
}
