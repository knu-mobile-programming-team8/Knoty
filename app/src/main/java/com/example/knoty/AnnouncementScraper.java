package com.example.knoty;

import android.os.AsyncTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class AnnouncementScraper {
    int UNIQUE_ID;

    class AnnouncementDBHelper {
        AnnouncementDBHelper() {}

        void create(Announcement a) {

        }

    }


    class ScrapTask extends AsyncTask<Integer, Void, Void> {
        @Override
        protected Void doInBackground(Integer... integers) {
            int category = integers[0];
            int page = integers[1];
            int maxNoti = integers[2];

            try {
                ArrayList<Announcement> list = scrap(category, page);
                updateListInStorage(list, maxNoti);
            }
            catch(IOException e) {
                return null;
            }

            return null;
        }
    }

    AnnouncementScraper() {

    }

    public void doScrapTask(int category, int page) {
        ScrapTask scrapTask = new ScrapTask();
        scrapTask.execute(category, page);
    }

    private boolean updateListInStorage(List<Announcement> list, int maxNoti) {
        for(Announcement a : list) {
        }

        return false;
    }

    public ArrayList<Announcement> getListInStorage(int category, int maxItems, boolean onlyUnread) {
        ArrayList<Announcement> list = new ArrayList<Announcement>();

        return list;
    }

    public abstract ArrayList<Announcement> scrap(int category, int page) throws IOException;
}

