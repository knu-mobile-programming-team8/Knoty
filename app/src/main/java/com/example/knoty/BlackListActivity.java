package com.example.knoty;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class BlackListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option); //OptionActivity와 동일한 레이아웃 사용

        //리사이클러뷰에 어댑터를 연결
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager manager = new LinearLayoutManager(this );
        recyclerView.setLayoutManager(manager);
        RecyclerAdapter adapter = new RecyclerAdapter();
        recyclerView.setAdapter(adapter);


        View.OnClickListener listener1 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "아이템 클릭", Toast.LENGTH_SHORT).show();
            }
        };
        View.OnClickListener listener2 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "아이템 클릭", Toast.LENGTH_SHORT).show();
            }
        };

        //옵션 리스트
        adapter.addItem(-1, "튜터", R.drawable.x_button, RecyclerAdapter.VIEW_TYPE_NORMAL, listener1);
        adapter.addItem(-1, "대회", R.drawable.x_button, RecyclerAdapter.VIEW_TYPE_NORMAL, listener2);
        adapter.addItem(-1, "튜터", R.drawable.x_button, RecyclerAdapter.VIEW_TYPE_NORMAL, listener1);
        adapter.addItem(-1, "대회", R.drawable.x_button, RecyclerAdapter.VIEW_TYPE_NORMAL, listener2);
        adapter.addItem(-1, "튜터", R.drawable.x_button, RecyclerAdapter.VIEW_TYPE_NORMAL, listener1);
        adapter.addItem(-1, "대회", R.drawable.x_button, RecyclerAdapter.VIEW_TYPE_NORMAL, listener2);
        adapter.addItem(-1, "튜터", R.drawable.x_button, RecyclerAdapter.VIEW_TYPE_NORMAL, listener1);
        adapter.addItem(-1, "대회", R.drawable.x_button, RecyclerAdapter.VIEW_TYPE_NORMAL, listener2);

        adapter.notifyDataSetChanged(); //데이터 변경 되었음을 알려준다
    }
}
