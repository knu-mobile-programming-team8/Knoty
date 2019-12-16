package com.example.knoty;


import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;

class MainAnnouncementScraper extends AnnouncementScraper {
    int UNIQUE_ID = 1;

    // Categories
    // 1 : 학사 공지
    @Override
    public ArrayList<Announcement> scrap(int category, int page) throws IOException {
        ArrayList<Announcement> list = new ArrayList<Announcement>();

        String baseUrl = " http://knu.ac.kr/wbbs/wbbs/bbs/btin/stdList.action?popupDeco=false&btin.search_type=&btin.search_text=&menu_idx=42&btin.page=" + page;
        String articleUrl = "http://knu.ac.kr/wbbs/wbbs/bbs/btin/stdViewBtin.action?btin.appl_no=000000&btin.search_type=&btin.search_text=&popupDeco=false&btin.note_div=row&menu_idx=42&btin.doc_no=";

        Document doc = Jsoup.connect(baseUrl).get();
        Element table = doc.select("tbody").first();

        for(Element row : table.children()) {
            Elements items = row.children();

            String numStr = items.get(0).text();
            if(numStr.equals("공지")) continue;
            int num = Integer.parseInt(numStr);

            String title = items.get(1).text();
            String author = items.get(3).text();
            String date = items.get(4).text();

            // javascript:doRead('1394200', '000000', '812', 'top');">
            String articleJS = items.get(1).children().get(0).attributes().get("href");
            int articleCode = Integer.parseInt(articleJS.split("\'")[1]);

            Announcement a = new Announcement(UNIQUE_ID, category, num, title, author, date, articleUrl + articleCode);
            list.add(a);
        }

        return list;
    }
}
