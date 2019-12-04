package com.example.knoty;

import java.io.IOException;
import java.util.ArrayList;

public abstract class AnnouncementScraper {
    AnnouncementScraper() {

    }

    public void pushNotification() {
        // TODO: Implement
    }

    public ArrayList<Announcement> getArrayListFromStorage(int category, int maxItems, boolean onlyUnread) {
        ArrayList<Announcement> list = new ArrayList<Announcement>();

        return list;
    }

    public abstract ArrayList<Announcement> scrap(int category, int page) throws IOException;
}

