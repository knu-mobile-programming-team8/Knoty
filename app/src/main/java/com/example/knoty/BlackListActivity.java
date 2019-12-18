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
        adapter.addItem(-1, null, -1, RecyclerAdapter.VIEW_TYPE_SPACE);
        adapter.addItem(R.drawable.whitelist, "블랙 리스트", 0, RecyclerAdapter.VIEW_TYPE_TOGGLE);
        adapter.addItem(-1, null, -1, RecyclerAdapter.VIEW_TYPE_DIVIDER);

        adapter.addItem(-1, "튜터", R.drawable.x_button, RecyclerAdapter.VIEW_TYPE_NORMAL, listener1);
        adapter.addItem(-1, "대회", R.drawable.x_button, RecyclerAdapter.VIEW_TYPE_NORMAL, listener2);
        adapter.addItem(-1, "튜터", R.drawable.x_button, RecyclerAdapter.VIEW_TYPE_NORMAL, listener1);
        adapter.addItem(-1, "대회", R.drawable.x_button, RecyclerAdapter.VIEW_TYPE_NORMAL, listener2);
        adapter.addItem(-1, "튜터", R.drawable.x_button, RecyclerAdapter.VIEW_TYPE_NORMAL, listener1);
        adapter.addItem(-1, "대회", R.drawable.x_button, RecyclerAdapter.VIEW_TYPE_NORMAL, listener2);
        adapter.addItem(-1, "튜터", R.drawable.x_button, RecyclerAdapter.VIEW_TYPE_NORMAL, listener1);
        adapter.addItem(-1, "대회", R.drawable.x_button, RecyclerAdapter.VIEW_TYPE_NORMAL, listener2);

        adapter.notifyDataSetChanged(); //데이터 변경 되었음을 알려준다

        //footer 보이게
        View footer = (View)findViewById(R.id.footer);
        footer.setVisibility(View.VISIBLE);
        footer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddItemDialog dialog = new AddItemDialog(view.getContext());
                dialog.alert();
            }
        });
    }
}
