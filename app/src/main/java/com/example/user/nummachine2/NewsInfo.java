package com.example.user.nummachine2;

/**
 * Created by StevenLiao on 2017/12/29.
 */

public class NewsInfo {
    private String news;
    private String newsPhoto;

    public NewsInfo(String news, String newsPhoto){
        this.news = news;
        this.newsPhoto = newsPhoto;
    }

    public String getnews() {
        return news;
    }

    public String getNewsPhoto() {
        return newsPhoto;
    }

    public void setNews(String news) {
        this.news = news;
    }

    public void setNewsPhoto(String newsPhoto) {
        this.newsPhoto = newsPhoto;
    }
}
