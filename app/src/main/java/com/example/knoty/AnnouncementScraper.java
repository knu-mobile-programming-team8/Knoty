package com.example.knoty;

import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

public abstract class AnnouncementScraper {
    int UNIQUE_ID = -1;
    Context ctx;

    class ScrapTask extends AsyncTask<Integer, Void, ArrayList<Announcement>> {
        @Override
        protected ArrayList<Announcement> doInBackground(Integer... integers) {
            int category = integers[0];
            int page = integers[1];

            try {
                ArrayList<Announcement> list = scrap(category, page);
                return updateListInStorage(list);
            }
            catch(IOException e) {
                //
            }

            return null;
        }
    }

    AnnouncementScraper(Context ctx) {
        this.ctx = ctx;
    }

    // 백그라운드 작업 실행
    // 실행 후 DB에 없던 레코드들 반환
    // NOTE : Not Async
    public ArrayList<Announcement> doScrapTask(int category, int page) {
        ScrapTask scrapTask = new ScrapTask();
        try {
            return scrapTask.execute(category, page).get();
        }
        catch(Exception e) {
            return null;
        }
    }

    // DB에 레코드 업데이트
    // DB에 새로 들어온 레코드 들을 반환
    private ArrayList<Announcement> updateListInStorage(List<Announcement> list) {
        AnnouncementDBHelper DBHelper = new AnnouncementDBHelper(ctx);
        SQLiteDatabase DB = DBHelper.getWritableDatabase();

        ArrayList<Announcement> newAnnouncementList = new ArrayList<>();

        for(Announcement a : list) {
            boolean doesExist = DB.query("announcement",
                    null,
                    "id = ? AND category = ? AND num = ?",
                    new String[]{Integer.toString(UNIQUE_ID), Integer.toString(a.category), Integer.toString(a.num)},
                    null,
                    null,
                    null).getCount() > 0;

            if(!doesExist) {
                newAnnouncementList.add(a);

                ContentValues contentValues = new ContentValues();
                contentValues.put("id", UNIQUE_ID);;
                contentValues.put("category", a.category);
                contentValues.put("num", a.num);
                contentValues.put("title", a.title);
                contentValues.put("author", a.author);
                contentValues.put("url", a.url);
                contentValues.put("date", a.date);

                contentValues.put("pushed", 0);
                contentValues.put("read", 0);
                contentValues.put("bookmark", 0);

                DB.insert("announcement", null, contentValues);
            }
        }

        return newAnnouncementList;
    }

    // DB에 있는 레코드들을 반환
    public ArrayList<Announcement> getListInStorage(int category, int maxItems, boolean onlyUnpushed) {
        ArrayList<Announcement> list = new ArrayList<Announcement>();

        AnnouncementDBHelper DBHelper = new AnnouncementDBHelper(ctx);
        SQLiteDatabase DB = DBHelper.getReadableDatabase();

        Cursor cursor = DB.query("announcement",
                null,
                "id = ? AND category = ? " + (onlyUnpushed ? "AND pushed = 0" : ""),
                 new String[]{Integer.toString(UNIQUE_ID), Integer.toString(category)},
                null,
                null,
                "date DESC");

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

                a.read = cursor.getInt(cursor.getColumnIndex("read")) > 0;
                a.bookmark = cursor.getInt(cursor.getColumnIndex("bookmark")) > 0;
                a.pushed = cursor.getInt(cursor.getColumnIndex("pushed")) > 0;

                list.add(a);
                if(list.size() >= maxItems) break;
            } while(cursor.moveToNext());
        }

        cursor.close();
        DB.close();

        return list;
    }

    public void markAsPushed(List<Announcement> list) {
        AnnouncementDBHelper DBHelper = new AnnouncementDBHelper(ctx);
        SQLiteDatabase DB = DBHelper.getWritableDatabase();

        for(Announcement a : list) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("pushed", true);

            DB.update(
                "announcement", contentValues,
                "id = ? AND category = ? AND num = ?",
                new String[]{ Integer.toString(a.id), Integer.toString(a.category), Integer.toString(a.num) }
            );
        }

        DB.close();
    }

    public void markAsPushed(Announcement a) {
        ArrayList<Announcement> list = new ArrayList<Announcement>();
        list.add(a);
        markAsPushed(list);
    }

    // DB에 있는 레코드 지움
    public void deleteRecords(List<Announcement> list) {
        AnnouncementDBHelper DBHelper = new AnnouncementDBHelper(ctx);
        SQLiteDatabase DB = DBHelper.getWritableDatabase();

        int deleted = 0;

        for(Announcement a : list) {
            deleted += DB.delete(
                "announcement",
                "id = ? AND category = ? AND num = ?",
                new String[]{ Integer.toString(a.id), Integer.toString(a.category), Integer.toString(a.num) }
            );
        }

        Log.d("DBdel", Integer.toString(deleted));

        DB.close();
    }

    public void deleteRecord(Announcement a) {
        ArrayList<Announcement> list = new ArrayList<Announcement>();
        list.add(a);
        deleteRecords(list);
    }

    public abstract ArrayList<Announcement> scrap(int category, int page) throws IOException;
}

