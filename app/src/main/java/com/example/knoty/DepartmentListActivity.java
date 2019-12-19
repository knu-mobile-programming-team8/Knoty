package com.example.knoty;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.CompoundButton;

public class DepartmentListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);

        //리사이클러뷰에 어댑터를 연결
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager manager = new LinearLayoutManager(this );
        recyclerView.setLayoutManager(manager);
        final RecyclerAdapter adapter = new RecyclerAdapter();
        recyclerView.setAdapter(adapter);

        //뒤에서 쓸 토글버튼 이벤트 리스너
        CompoundButton.OnCheckedChangeListener listener1 = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                KnotyPreferences.setBoolean(buttonView.getContext(), KnotyPreferences.TOGGLE_DEPARTMENT_KNU, isChecked);
                if(!isChecked) {
                    MainActivity.adapter.removeItemsById(1);
                    MainActivity.adapter.notifyDataSetChanged();
                }
            }
        };
        CompoundButton.OnCheckedChangeListener listener2 = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                KnotyPreferences.setBoolean(buttonView.getContext(), KnotyPreferences.TOGGLE_DEPARTMENT_COMPUTER, isChecked);
                if(!isChecked) {
                    MainActivity.adapter.removeItemsById(2);
                    MainActivity.adapter.notifyDataSetChanged();
                }
            }
        };

        //아이템 추가
        adapter.addItem(-1, null, -1, RecyclerAdapter.VIEW_TYPE_SPACE);
        adapter.addItem(-1, "알림 받을 공지 종류 선택", -1, RecyclerAdapter.VIEW_TYPE_HEADLINE);
        adapter.addItem(-1, "경북대학교", KnotyPreferences.getBoolean(this, KnotyPreferences.TOGGLE_DEPARTMENT_KNU, true) ? 1 : 0, RecyclerAdapter.VIEW_TYPE_TOGGLE, null, listener1);
        adapter.addItem(-1, "컴퓨터학부", KnotyPreferences.getBoolean(this, KnotyPreferences.TOGGLE_DEPARTMENT_COMPUTER, false) ? 1 : 0, RecyclerAdapter.VIEW_TYPE_TOGGLE, null, listener2);
    }
}
