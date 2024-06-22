package com.example.youtube.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.youtube.R;

@Entity
public class Video {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String author;

    private int video;

    private String content;
    private int likes;
    private int pic;
    private String duration;
    private String views;

    public int getVideo() {
        return video;
    }

    public void setVideo(int video) {
        this.video = video;
    }

    private String uploadDate;

    public Video(String author, String content, String duration , String views, String uploadDate, int pic, int video) {
        this.author = author;
        this.content = content;
        this.pic = pic;
        this.duration = duration;
        this.views = views;
        this.uploadDate = uploadDate;
        this.video = video;
    }
    public Video() {
        this.pic = R.drawable.osher;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getViews() {
        return views;
    }

    public void setViews(String views) {
        this.views = views;
    }

    public String getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(String uploadDate) {
        this.uploadDate = uploadDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getPic() {
        return pic;
    }

    public void setPic(int pic) {
        this.pic = pic;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }
}