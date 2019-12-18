package com.example.knoty;

import android.content.Context;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;

class CSEAnnouncementScraper extends AnnouncementScraper {
    int UNIQUE_ID = 2;

    CSEAnnouncementScraper(Context ctx) {
        super(ctx);
    }

    // Categories
    // 1 : 전체 공지
    // 2 : 학사
    // 3 : ABEEK
    // 4 : 글솝
    // 5 : 심화
    // 6 : 대학원
    @Override
    public ArrayList<Announcement> scrap(int category, int page) throws IOException {
        ArrayList<Announcement> list = new ArrayList<Announcement>();

        String categoryStr = "";
        if(2 <= category && category <= 6) categoryStr = "_" + category;

        String pageUrl = "";
        if(page >= 2) pageUrl = "?page=" + Integer.toString(page);

        String baseUrl = "http://computer.knu.ac.kr/06_sub/02_sub" + categoryStr + ".html";

        Document doc = Jsoup.connect(baseUrl + pageUrl).get();
        Element table = doc.select("tbody").first();

        for(Element row : table.children()) {
            Elements items = row.children();

            String numStr = items.get(0).text();
            if(numStr.equals("공지")) continue;
            int num = Integer.parseInt(numStr);

            String title = items.get(1).text();
            String author = items.get(2).text();
            String date = items.get(3).text();

            String articleUrl = items.get(1).children().get(0).attributes().get("href");

            Announcement a = new Announcement(UNIQUE_ID, category, num, title, author, date, baseUrl + articleUrl);
            list.add(a);
        }

        return list;
    }
}
