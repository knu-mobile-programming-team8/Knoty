package com.example.knoty;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

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

    public void doScrapTask(int category, int page) {
        ScrapTask scrapTask = new ScrapTask();
        scrapTask.execute(category, page);
    }

    private boolean updateListInStorage(List<Announcement> list) {
        AnnouncementDBHelper DBHelper = new AnnouncementDBHelper(ctx);
        SQLiteDatabase DB = DBHelper.getWritableDatabase();

        for(Announcement a : list) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("id", UNIQUE_ID);;
            contentValues.put("category", a.category);
            contentValues.put("num", a.num);
            contentValues.put("title", a.title);
            contentValues.put("author", a.author);
            contentValues.put("url", a.url);
            contentValues.put("date", a.date);

            DB.replace("announcement", null, contentValues);
        }

        return false;
    }

    public ArrayList<Announcement> getListInStorage(int category, int maxItems, boolean onlyUnread) {
        // TODO: maxItems, onlyUread

        ArrayList<Announcement> list = new ArrayList<Announcement>();

        AnnouncementDBHelper DBHelper = new AnnouncementDBHelper(ctx);
        SQLiteDatabase DB = DBHelper.getReadableDatabase();

        Cursor cursor = DB.query("announcement",
                null,
                "id = ? AND category = ?",
                new String[]{Integer.toString(UNIQUE_ID), Integer.toString(category)},
                null,
                null,
                null);

        if(cursor == null) return list;

        Log.d("DB", Integer.toString(cursor.getCount()));

        if(cursor.moveToFirst()) {
            do {
                Announcement a = new Announcement();
                a.id = UNIQUE_ID;
                a.category = cursor.getInt(cursor.getColumnIndex("category"));
                a.num = cursor.getInt(cursor.getColumnIndex("num"));

                a.title = cursor.getString(cursor.getColumnIndex("title"));
                a.date = cursor.getString(cursor.getColumnIndex("date"));
                a.author = cursor.getString(cursor.getColumnIndex("author"));
                a.url = cursor.getString(cursor.getColumnIndex("url"));

                list.add(a);
            } while(cursor.moveToNext());
        }

        cursor.close();

        return list;
    }

    public abstract ArrayList<Announcement> scrap(int category, int page) throws IOException;
}

