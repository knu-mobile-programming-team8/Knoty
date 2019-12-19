package com.example.knoty;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class RestartService extends Service {
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("==============", "restartservice 실행됨. knoty service를 시작하겠음");
        //무조건 이상한 notification으로 표시되게 돼있음 그러나 RestartService는 짧은 시간에 KnotyService만 실행시키고 바로 꺼버리기에 notification 안 나오게 함
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "default");
        builder.setSmallIcon(R.mipmap.ic_launcher);

        Notification notification = builder.build();
        startForeground(9, notification);

        Intent in = new Intent(this, KnotyService.class); //KnotyService 시작
        startService(in);

        stopForeground(true);
        stopSelf();

        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
