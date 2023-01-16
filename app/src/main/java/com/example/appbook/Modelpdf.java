package com.example.appbook;

public class Modelpdf {
    String uid, id,titel,description,categoryId,url;
    long timetamp,viewsCount,downloadsCount;

    public Modelpdf() {
    }

    public Modelpdf(String uid, String id, String titel, String description, String categoryId, String url, long timetamp, long viewsCount, long downloadsCount) {
        this.uid = uid;
        this.id = id;
        this.titel = titel;
        this.description = description;
        this.categoryId = categoryId;
        this.url = url;
        this.timetamp = timetamp;
        this.viewsCount = viewsCount;
        this.downloadsCount = downloadsCount;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitel() {
        return titel;
    }

    public void setTitel(String titel) {
        this.titel = titel;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getTimetamp() {
        return timetamp;
    }

    public void setTimetamp(long timetamp) {
        this.timetamp = timetamp;
    }

    public long getViewsCount() {
        return viewsCount;
    }

    public void setViewsCount(long viewsCount) {
        this.viewsCount = viewsCount;
    }

    public long getDownloadsCount() {
        return downloadsCount;
    }

    public void setDownloadsCount(long downloadsCount) {
        this.downloadsCount = downloadsCount;
    }
}



