package com.example.knoty;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class AnnouncementPush {

    String title;
    String msg;
    Context ctx;
    String url;

    //title은 푸쉬알람 제목, msg는 푸쉬 알람 내용
    public AnnouncementPush(Context ctx, String title, String msg, String url) {
        this.title = title;
        this.msg = msg;
        this.ctx = ctx;
        this.url = url;
    }

    public AnnouncementPush(Context ctx, Announcement anno) {
        this(ctx, anno.title, anno.date, anno.url);
    }

    //푸쉬 알림을 실행시킨다
    public void alert() {
        //알림을 누르면 실행시킬 팬딩 인텐트 생성
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        PendingIntent pending = PendingIntent.getActivity(ctx, 0, intent, 0);

        //알림 설정
        NotificationCompat.Builder builder = new NotificationCompat.Builder(ctx, "1");
        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(msg)
                .setAutoCancel(true)
                .setContentIntent(pending);


        //푸쉬 알림 울리기
        NotificationManager manager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(1, builder.build());
    }
}
