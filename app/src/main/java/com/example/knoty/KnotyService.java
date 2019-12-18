package com.example.knoty;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

public class KnotyService extends Service implements Runnable {

    @Override
    public void onCreate() {
        //AnnouncementPush 실행
        Thread myThread = new Thread(this);
        myThread.start();
    }

    public void run() {
        while (true) {
            try {
                Thread.sleep(1000 * 60 * 60 * 2);
            } catch (Exception ex) {
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
