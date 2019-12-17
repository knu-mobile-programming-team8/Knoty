package com.example.knoty;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

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



        //옵션 리스트
        ArrayList<Integer> imageList = new ArrayList<Integer>(Arrays.asList(R.drawable.whitelist, R.drawable.blacklist));
        ArrayList<String> textList = new ArrayList<String>(Arrays.asList("화이트 리스트", "블랙 리스트"));
        for(int i = 0; i < textList.size(); i++) {
            adapter.addItem(imageList.get(i), textList.get(i), R.drawable.x_button, RecyclerAdapter.VIEW_TYPE_NORMAL);
            adapter.addItem(imageList.get(i), textList.get(i), 1, RecyclerAdapter.VIEW_TYPE_TOGGLE);
            adapter.addItem(-1, null, 1, RecyclerAdapter.VIEW_TYPE_DIVIDER);
        }
        adapter.notifyDataSetChanged(); //데이터 변경 되었음을 알려준다
    }
}
