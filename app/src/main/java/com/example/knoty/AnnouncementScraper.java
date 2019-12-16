package com.example.knoty;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class AnnouncementScraper {
    int UNIQUE_ID;
    Context ctx;

    class ScrapTask extends AsyncTask<Integer, Void, Void> {
        @Override
        protected Void doInBackground(Integer... integers) {
            int category = integers[0];
            int page = integers[1];

            try {
                ArrayList<Announcement> list = scrap(category, page);
                updateListInStorage(list);
            }
            catch(IOException e) {
                return null;
            }

            return null;
        }
    }

    AnnouncementScraper(Context ctx) {
        this.ctx = ctx;
    }

    public void doScrapTask(int category, int page, int maxNoti) {
        ScrapTask scrapTask = new ScrapTask();
        scrapTask.execute(category, page);
    }

    private boolean updateListInStorage(List<Announcement> list) {
        AnnouncementDBHelper DBHelper = new AnnouncementDBHelper(ctx);
        SQLiteDatabase DB = DBHelper.getWritableDatabase();

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

