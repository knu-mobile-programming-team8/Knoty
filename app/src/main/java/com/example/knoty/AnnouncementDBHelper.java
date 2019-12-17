package com.example.knoty;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AnnouncementDBHelper extends SQLiteOpenHelper {
    public AnnouncementDBHelper(Context context) {
        super(context, "announcementlist.db", null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
            "CREATE TABLE IF NOT EXISTS announcement (" +
            "id NUMBER, category NUMBER, num NUMBER," +
            "title TEXT, author TEXT, date TEXT, url TEXT," +
            "read NUMBER, bookmark NUMBER" +
            "PRIMARY KEY (id, category, num)" +
            ");"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS announcement");
        onCreate(db);
    }
}
