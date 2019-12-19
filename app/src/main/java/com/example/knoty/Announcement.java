package com.example.knoty;

public class Announcement implements Comparable<Announcement> {
    int id;
    int category;
    int num;
    String title;
    String author;
    String date; // YYYY-MM-DD
    String url;

    boolean read = false;
    boolean bookmark = false;
    boolean pushed = false;

    Announcement() {

    }

    Announcement(int id, int category, int num, String title, String author, String date, String url) {
        this.id = id;
        this.category = category;
        this.num = num;
        this.title = title;
        this.author = author;
        this.date = date;
        this.url = url;
    }

    @Override
    public String toString() {
        return title;
    }

    @Override
    public int compareTo(Announcement other) {
        if(bookmark != other.bookmark) return !bookmark ? -1 : 1;
        if(read != other.read) return !read ? -1 : 1;
        if(date != other.date) return date.compareTo(other.date) > 0 ? -1 : 1;
        return 0;
    }
}

