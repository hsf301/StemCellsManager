package com.huashengfu.StemCellsManager.entity.dynamic;

import java.io.Serializable;

public class DynamicDetail implements Serializable {

    private String content;
    private String url;
    private String videoUrl;
    private boolean video;
    private boolean article;
    private boolean image;

    public boolean isImage() {
        return image;
    }

    public void setImage(boolean image) {
        this.image = image;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isVideo() {
        return video;
    }

    public void setVideo(boolean video) {
        this.video = video;
    }

    public boolean isArticle() {
        return article;
    }

    public void setArticle(boolean article) {
        this.article = article;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }
}
