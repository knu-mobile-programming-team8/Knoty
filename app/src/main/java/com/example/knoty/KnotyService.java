package com.example.knoty;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import java.util.ArrayList;
import java.util.Calendar;

public class KnotyService extends Service implements Runnable {
    public static Intent serviceIntent = null;
    public static final int REFRESH_TERM_MILLISECONDS = 1000 * 60 * 60 * 2; //공지사항 파싱하는 주기 (2시간)
    private static final int MAX_ITEM = 3; //각 공지마다 받아올 최대 아이템 개수
    private static final int TRY_MAX_COUNT = 5; //공지 안 불러져왔을 때 최대 시도 개수(잘 불러왔는데 이미 다 기존에 저장되있는 거 일 수도 있음)
    private static final int TRY_TERM = 500; //재시도 간격 밀리초

    @Override
    public void onCreate() { //서비스 최초 생성시 1번만 실행 됨
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) { //서비스 시작시마다 실행 됨
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            initializeNotification();
        }

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    getNotice();
                    try {
                        Thread.sleep(REFRESH_TERM_MILLISECONDS);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();

        return START_STICKY;
    }

    //2시간마다 홈페이지에서 공지사항 가져오는 거
    private void getNotice() {
        ArrayList<Announcement> tempList;

        //옵션에서 학교 홈페이지 공지 받아온다고 설정 해놨을 떄
        if(KnotyPreferences.getBoolean(this, KnotyPreferences.TOGGLE_DEPARTMENT_KNU, true)) {
            MainAnnouncementScraper mainAS = new MainAnnouncementScraper(this);
            mainAS.doScrapTask(1, 1); //학교 홈페이지는 카테고리 1뿐
            Log.d("==========", "학교 홈페이지 공지사항 긁어오는 중...");

            //500밀리초 간격으로 최대 5번 시도한다
            for(int i = 0; (tempList = mainAS.getListInStorage(1, MAX_ITEM, true)).size() == 0 && i < TRY_MAX_COUNT; i++) {
                try {
                    Thread.sleep(TRY_TERM);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            //푸쉬 알림
            for(Announcement ant : tempList) {
                if(!KnotyPreferences.shouldPush(this, ant)) continue; //화이트리스트 블랙리스트 검사해서 걸리면 continue로 넘김

                AnnouncementPush announcementPush = new AnnouncementPush(this, ant);
                announcementPush.alert();
                Log.d("================", ant.title + "를 푸시합니다");
            }
        }

        //컴학
        if(KnotyPreferences.getBoolean(this, KnotyPreferences.TOGGLE_DEPARTMENT_COMPUTER, false)) {
            CSEAnnouncementScraper cseAS = new CSEAnnouncementScraper(this);
            cseAS.doScrapTask(0, 1); //카테고리 1~6까지. 0은 1~6을 모두 doScrapTask
            Log.d("==========", "컴학 홈페이지 공지사항 긁어오는 중...");

            //500밀리초 간격으로 최대 5번 시도한다
            for(int i = 0; (tempList = cseAS.getListInStorage(1, MAX_ITEM, true)).size() == 0 && i < TRY_MAX_COUNT; i++) {
                try {
                    Thread.sleep(TRY_TERM);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            //푸쉬 알림
            for(Announcement ant : tempList) {
                if(!KnotyPreferences.shouldPush(this, ant)) continue; //화이트리스트 블랙리스트 검사해서 걸리면 continue로 넘김

                AnnouncementPush announcementPush = new AnnouncementPush(this, ant);
                announcementPush.alert();
            }

            //500밀리초 간격으로 최대 5번 시도한다
            for(int i = 0; (tempList = cseAS.getListInStorage(2, MAX_ITEM, true)).size() == 0 && i < TRY_MAX_COUNT; i++) {
                try {
                    Thread.sleep(TRY_TERM);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            //푸쉬 알림
            for(Announcement ant : tempList) {
                if(!KnotyPreferences.shouldPush(this, ant)) continue; //화이트리스트 블랙리스트 검사해서 걸리면 continue로 넘김

                AnnouncementPush announcementPush = new AnnouncementPush(this, ant);
                announcementPush.alert();
            }

            //500밀리초 간격으로 최대 5번 시도한다
            for(int i = 0; (tempList = cseAS.getListInStorage(3, MAX_ITEM, true)).size() == 0 && i < TRY_MAX_COUNT; i++) {
                try {
                    Thread.sleep(TRY_TERM);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            //푸쉬 알림
            for(Announcement ant : tempList) {
                if(!KnotyPreferences.shouldPush(this, ant)) continue; //화이트리스트 블랙리스트 검사해서 걸리면 continue로 넘김

                AnnouncementPush announcementPush = new AnnouncementPush(this, ant);
                announcementPush.alert();
            }

            //500밀리초 간격으로 최대 5번 시도한다
            for(int i = 0; (tempList = cseAS.getListInStorage(4, MAX_ITEM, true)).size() == 0 && i < TRY_MAX_COUNT; i++) {
                try {
                    Thread.sleep(TRY_TERM);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            //푸쉬 알림
            for(Announcement ant : tempList) {
                if(!KnotyPreferences.shouldPush(this, ant)) continue; //화이트리스트 블랙리스트 검사해서 걸리면 continue로 넘김

                AnnouncementPush announcementPush = new AnnouncementPush(this, ant);
                announcementPush.alert();
            }

            //500밀리초 간격으로 최대 5번 시도한다
            for(int i = 0; (tempList = cseAS.getListInStorage(5, MAX_ITEM, true)).size() == 0 && i < TRY_MAX_COUNT; i++) {
                try {
                    Thread.sleep(TRY_TERM);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            //푸쉬 알림
            for(Announcement ant : tempList) {
                if(!KnotyPreferences.shouldPush(this, ant)) continue; //화이트리스트 블랙리스트 검사해서 걸리면 continue로 넘김

                AnnouncementPush announcementPush = new AnnouncementPush(this, ant);
                announcementPush.alert();
            }

            //500밀리초 간격으로 최대 5번 시도한다
            for(int i = 0; (tempList = cseAS.getListInStorage(6, MAX_ITEM, true)).size() == 0 && i < TRY_MAX_COUNT; i++) {
                try {
                    Thread.sleep(TRY_TERM);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            //푸쉬 알림
            for(Announcement ant : tempList) {
                if(!KnotyPreferences.shouldPush(this, ant)) continue; //화이트리스트 블랙리스트 검사해서 걸리면 continue로 넘김

                AnnouncementPush announcementPush = new AnnouncementPush(this, ant);
                announcementPush.alert();
            }
        }
    }

    private void initializeNotification() {
        //노티피케이션 만들기
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "2"); //2는 채널 아이디
        builder.setSmallIcon(R.mipmap.ic_launcher);

        NotificationCompat.BigTextStyle style = new NotificationCompat.BigTextStyle();
        style.bigText("앱을 열면 나오는 옵션창에서 해당 채널 알림을 꺼주세요");
        style.setBigContentTitle(null);
        style.setSummaryText("");

        builder.setContentText(null);
        builder.setContentTitle(null);
        builder.setOngoing(true);

        builder.setStyle(style);
        builder.setWhen(0);
        builder.setShowWhen(false);

        Intent notificationIntent = new Intent(this, MainActivity.class); //notification이라는게 우리 앱에서 공지 띄울 때 쓰는 게 아니라 오레오 정책상 어쩔 수 없이 쓰는 foreground 돌아감을 표시해주는 notification임
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0); //0은 각각 requestCode와 flags
        builder.setContentIntent(pendingIntent);

        NotificationManager manager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        Notification notification = builder.build();
        startForeground(2, notification);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        setAlarm();
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);

        setAlarm();
    }

    //3초 후 AlarmReceiver에 신호를 보내어 깨운다
    private void setAlarm() {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.SECOND, 3); //현재 시간에 3초 더한 값을 캘린더에 저장
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(this, 0, intent, 0);

        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void run() {

    }
}
