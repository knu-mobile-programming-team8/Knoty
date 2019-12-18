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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //리사이클러뷰에 어댑터를 연결
        RecyclerView recyclerView = findViewById(R.id.mainRecyclerView);
        LinearLayoutManager manager = new LinearLayoutManager(this );
        recyclerView.setLayoutManager(manager);
        MainActivityRecyclerAdapter adapter = new MainActivityRecyclerAdapter();
        recyclerView.setAdapter(adapter);

        //공지사항들을 불러온다
        ArrayList<Announcement> list;
        if(true) { //학교 홈페이지 공지 모드인지 확인
            MainAnnouncementScraper mainAS = new MainAnnouncementScraper(this);
            mainAS.doScrapTask(1, 1);

            list = mainAS.getListInStorage(1, 50, false);
        } else if(true) { //컴학 모드인지 확인
            CSEAnnouncementScraper cseAS = new CSEAnnouncementScraper(this);
            cseAS.doScrapTask(0, 1);

            list = cseAS.getListInStorage(2, 50, false);
        }
        for(Announcement ant : list) {
            Log.d("===============", ant.title + " " + ant.id);
            adapter.addItem(ant);
        }
        adapter.notifyDataSetChanged(); //데이터 변경 되었음을 알려준다

        //리스트에 아이템들을 추가
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
