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

import java.util.Calendar;

public class KnotyService extends Service implements Runnable {
    public static Intent serviceIntent = null;

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
                    try {
                        Log.d("==========", "3초마다 실행되는 쓰레드");
                        Thread.sleep(1000 * 3);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();

        return START_STICKY;
    }

    private void initializeNotification() {
        //노티피케이션 만들기
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "2"); //2는 채널 아이디
        builder.setSmallIcon(R.mipmap.ic_launcher);

        NotificationCompat.BigTextStyle style = new NotificationCompat.BigTextStyle();
        style.bigText("내가 설정한 텍스트");
        style.setBigContentTitle(null);
        style.setSummaryText("요약 텍스트는 뭐지");

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
