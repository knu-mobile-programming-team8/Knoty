package com.example.knoty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static MainActivityRecyclerAdapter adapter = null;
    public static Intent foregroundServiceIntent = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        checkNotificationChannelSettings();

        initService(); //2시간마다 푸쉬 알림 해줄 서비스 초기화
        initRecyclerView(); //리사이클러뷰와 어댑터 연결
        loadNotices(); //메인화면에 표시할 공지사항들을 불러와서 리사이클러뷰에 표시한다
    }

    //노티피케이션 채널의 현재 설정값을 읽어오고 사용자에게 설정하라고 채널 설정창을 열어주는 함수
    private void checkNotificationChannelSettings() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationManager manager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
            List<NotificationChannel> channels = manager.getNotificationChannels();

            if(channels.size() == 0) { //앱을 다운 받고 처음 실행이라서 아직 채널이 없음
                manager.createNotificationChannel(new NotificationChannel("1", "공지사항 알림", NotificationManager.IMPORTANCE_DEFAULT));
                manager.createNotificationChannel(new NotificationChannel("2", "이 채널은 알림을 꺼주세요", NotificationManager.IMPORTANCE_MIN));
            }
        }

        if(areNotificationsEnabled(this, "2")) { //2번 채널이 켜져있는 경우 꺼달라고 요청
            Toast.makeText(this, "이 채널에 대한 알림은 꺼주셔야합니다.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS);
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
            intent.putExtra(Settings.EXTRA_CHANNEL_ID, "2");
            startActivity(intent);
        }
    }

    //노티피케이션 채널의 현재 설정값을 읽어옴
    public boolean areNotificationsEnabled(Context context, String channelId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if(channelId != null && !channelId.isEmpty()) {
                NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                NotificationChannel channel = manager.getNotificationChannel(channelId);
                return channel.getImportance() != NotificationManager.IMPORTANCE_NONE;
            }
            return false;
        } else {
            return NotificationManagerCompat.from(context).areNotificationsEnabled();
        }
    }

    public void initService() {
        if(KnotyService.serviceIntent == null) { //처음 앱을 실행해서 KnotyService가 아직 실행되지 않은 경우
            Intent foregroundServiceIntent = new Intent(this, KnotyService.class);
            startService(foregroundServiceIntent); //KnotyService 실행
            Toast.makeText(this, "서비스 시작", Toast.LENGTH_SHORT).show();
        } else {
            foregroundServiceIntent = KnotyService.serviceIntent;
            Toast.makeText(this, "서비스 아까 시작시켰음", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(foregroundServiceIntent != null) {
            stopService(foregroundServiceIntent);
            foregroundServiceIntent = null;
        }
    }

    //리사이클러뷰에 어댑터를 연결
    public void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.mainRecyclerView);
        LinearLayoutManager manager = new LinearLayoutManager(this );
        recyclerView.setLayoutManager(manager);
        adapter = new MainActivityRecyclerAdapter();
        recyclerView.setAdapter(adapter);
    }

    //공지사항들을 불러온다
    public void loadNotices() {
        ArrayList<Announcement> list = new ArrayList<Announcement>(); //여러 학과를 선택한 경우 학과들에서 각각 모은 공지사항들을 list에 모두 합친다
        ArrayList<Announcement> temp = null; //각 학과의 공지사항들을 잠깐 여기에 저장한 후 list에 합친다
        if(KnotyPreferences.getBoolean(this, KnotyPreferences.TOGGLE_DEPARTMENT_KNU, true)) { //학교 홈페이지 공지 모드인지 확인
            MainAnnouncementScraper mainAS = new MainAnnouncementScraper(this);
            mainAS.doScrapTask(1, 1); //학교 홈페이지는 카테고리 1뿐

            temp = mainAS.getListInStorage(1, 20, false);
            for(Announcement ant : temp)  { list.add(ant); }
        }
        if(KnotyPreferences.getBoolean(this, KnotyPreferences.TOGGLE_DEPARTMENT_COMPUTER, false)) { //컴학 모드인지 확인
            CSEAnnouncementScraper cseAS = new CSEAnnouncementScraper(this);
            cseAS.doScrapTask(0, 1); //카테고리 1~6까지. 0은 1~6을 모두 doScrapTask

            temp = cseAS.getListInStorage(1, 20, false);
            for(Announcement ant : temp)  { list.add(ant); }
            temp = cseAS.getListInStorage(2, 20, false);
            for(Announcement ant : temp)  { list.add(ant); }
            temp = cseAS.getListInStorage(3, 20, false);
            for(Announcement ant : temp)  { list.add(ant); }
            temp = cseAS.getListInStorage(4, 20, false);
            for(Announcement ant : temp)  { list.add(ant); }
            temp = cseAS.getListInStorage(5, 20, false);
            for(Announcement ant : temp)  { list.add(ant); }
        }

        Collections.sort(list);

        for(Announcement ant : list) {
            adapter.addItem(ant);
        }
        adapter.notifyDataSetChanged(); //데이터 변경 되었음을 알려준다
    }

    //옵션 메뉴 등록
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    //옵션 메뉴 선택시 핸들러
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu1: //옵션 메뉴
                Intent intent = new Intent(MainActivity.this, OptionActivity.class);
                startActivity(intent); //옵션 액티비티로 전환
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
