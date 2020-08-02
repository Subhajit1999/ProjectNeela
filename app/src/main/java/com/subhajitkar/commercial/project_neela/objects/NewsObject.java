package com.subhajitkar.commercial.project_neela.objects;

public class NewsObject {

    private String newsImage;
    private String newsTitle;
    private String newsUrl;
    private String newsSource;

    public NewsObject(String image, String title, String source, String url){
        newsImage = image;
        newsTitle = title;
        newsSource = source;
        newsUrl = url;
    }

    public String getNewsImage() {
        return newsImage;
    }

    public String getNewsSource() {
        return newsSource;
    }

    public String getNewsTitle() {
        return newsTitle;
    }

    public String getNewsUrl() {
        return newsUrl;
    }
}
