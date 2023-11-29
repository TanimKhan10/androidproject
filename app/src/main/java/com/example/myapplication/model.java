package com.example.myapplication;

public class model {
    String title,url,category;

    public model() {
    }

    public model(String title, String url,String category) {
        this.title = title;
        this.url = url;
        this.category=category;


    }



    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
