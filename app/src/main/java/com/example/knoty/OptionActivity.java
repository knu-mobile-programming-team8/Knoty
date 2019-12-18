package com.example.knoty;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class OptionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);

        //리사이클러뷰에 어댑터를 연결
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager manager = new LinearLayoutManager(this );
        recyclerView.setLayoutManager(manager);
        RecyclerAdapter adapter = new RecyclerAdapter();
        recyclerView.setAdapter(adapter);


        View.OnClickListener listener1 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OptionActivity.this, WhiteListActivity.class);
                startActivity(intent);
            }
        };
        View.OnClickListener listener2 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OptionActivity.this, BlackListActivity.class);
                startActivity(intent);
            }
        };

        //옵션 리스트
        adapter.addItem(-1, null, -1, RecyclerAdapter.VIEW_TYPE_SPACE); //빈공백 (메뉴가 너무 위에 딱 붙어있어서)
        adapter.addItem(R.drawable.whitelist, "화이트 리스트", -1, RecyclerAdapter.VIEW_TYPE_NORMAL, listener1);
        adapter.addItem(R.drawable.blacklist, "블랙 리스트", -1, RecyclerAdapter.VIEW_TYPE_NORMAL, listener2);

        adapter.notifyDataSetChanged(); //데이터 변경 되었음을 알려준다

        Log.d("======", "사과: " + (KnotyPreferences.shouldPush(this, "사과")));
        Log.d("======", "치킨: " + (KnotyPreferences.shouldPush(this, "치킨")));
        Log.d("======", "피자: " + (KnotyPreferences.shouldPush(this, "피자")));
        Log.d("======", "달걀: " + (KnotyPreferences.shouldPush(this, "달걀")));
        Log.d("======", "치킨: " + (KnotyPreferences.shouldPush(this, "단팥빵")));
        Log.d("======", "당근: " + (KnotyPreferences.shouldPush(this, "당근")));
        Log.d("======", "파프리카: " + (KnotyPreferences.shouldPush(this, "파프리카")));
    }
}
