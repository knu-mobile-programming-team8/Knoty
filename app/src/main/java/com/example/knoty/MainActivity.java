package com.example.knoty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    public static MainActivityRecyclerAdapter adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initRecyclerView(); //리사이클러뷰와 어댑터 연결
        loadNotices(); //메인화면에 표시할 공지사항들을 불러와서 리사이클러뷰에 표시한다
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
            mainAS.doScrapTask(1, 1);

            temp = mainAS.getListInStorage(1, 50, false);
            for(Announcement ant : temp)  { list.add(ant); }
        }
        if(KnotyPreferences.getBoolean(this, KnotyPreferences.TOGGLE_DEPARTMENT_COMPUTER, false)) { //컴학 모드인지 확인
            CSEAnnouncementScraper cseAS = new CSEAnnouncementScraper(this);
            cseAS.doScrapTask(0, 1);

            temp = cseAS.getListInStorage(2, 50, false);
            for(Announcement ant : temp)  { list.add(ant); }
        }

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
