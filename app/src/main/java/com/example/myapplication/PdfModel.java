package com.example.myapplication;

public class PdfModel {
    private String title;
    private String url;

    // Default constructor required for calls to DataSnapshot.getValue(PdfModel.class)
    public PdfModel() {}

    public PdfModel(String title, String url) {
        this.title = title;
        this.url = url;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }
}
