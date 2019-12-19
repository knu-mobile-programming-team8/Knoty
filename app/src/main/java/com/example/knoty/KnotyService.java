package com.example.knoty;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import java.util.Calendar;

public class KnotyService extends Service implements Runnable {

    public static Thread myThread = null;

    @Override
    public void onCreate() {
        //AnnouncementPush 실행
        if(myThread == null) { //앱을 두 번 실행하면 myThread가 이미 생성되어 있기 때문에 최초 1번만 생성
            myThread = new Thread(this);
            myThread.start();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        //앱이 꺼지면 myThread를 종료시키고(오레오부터 액티비티가 없는데 startService로 백그라운드 실행하면 안 됨. startForegroundService로 실행해야함) setAlarmTimer()를 통해서 다시 서비스를 foregroundService로 실행
        KnotyService.myThread = null;
        setAlarmTimer();
        Thread.currentThread().interrupt(); //쓰레드 종료
    }

    protected void setAlarmTimer() {
        final Calendar cal = Calendar.getInstance();

        //현재 시간에 1초 추가한 값을 cal에 저장
        cal.setTimeInMillis(System.currentTimeMillis());
        cal.add(Calendar.SECOND, 1);

        //intent에 AlarmReceiver 담아놓고
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(this, 0, intent, 0);

        //1초 후 intent에 일어나라고 함
        AlarmManager manager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        manager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), sender);
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
