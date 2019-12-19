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
        View.OnClickListener listener3 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OptionActivity.this, DepartmentListActivity.class);
                startActivity(intent);
            }
        };

        //옵션 리스트
        adapter.addItem(-1, null, -1, RecyclerAdapter.VIEW_TYPE_SPACE); //빈공백 (메뉴가 너무 위에 딱 붙어있어서)
        adapter.addItem(-1, "화이트/블랙 리스트", -1, RecyclerAdapter.VIEW_TYPE_HEADLINE);
        adapter.addItem(R.drawable.whitelist, "화이트 리스트", -1, RecyclerAdapter.VIEW_TYPE_NORMAL, listener1);
        adapter.addItem(R.drawable.blacklist, "블랙 리스트", -1, RecyclerAdapter.VIEW_TYPE_NORMAL, listener2);
        adapter.addItem(-1, null, -1, RecyclerAdapter.VIEW_TYPE_DIVIDER);
        adapter.addItem(-1, "알림 받을 학과 선택", -1, RecyclerAdapter.VIEW_TYPE_HEADLINE);
        adapter.addItem(R.drawable.selector, "학과 설정", -1, RecyclerAdapter.VIEW_TYPE_NORMAL, listener3);

        adapter.notifyDataSetChanged(); //데이터 변경 되었음을 알려준다
    }
}
