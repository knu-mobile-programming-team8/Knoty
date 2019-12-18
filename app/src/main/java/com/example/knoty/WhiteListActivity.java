package com.example.knoty;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.Toast;

import java.util.ArrayList;

public class WhiteListActivity extends AppCompatActivity {
    public static RecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option); //OptionActivity와 동일한 레이아웃 사용

        //리사이클러뷰에 어댑터를 연결
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager manager = new LinearLayoutManager(this );
        recyclerView.setLayoutManager(manager);
        adapter = new RecyclerAdapter();
        recyclerView.setAdapter(adapter);

        //옵션 리스트
        adapter.addItem(-1, null, -1, RecyclerAdapter.VIEW_TYPE_SPACE);
        adapter.addItem(R.drawable.whitelist, "화이트 리스트", KnotyPreferences.getBoolean(this, KnotyPreferences.TOGGLE_WHITELIST) ? 1 : 0, RecyclerAdapter.VIEW_TYPE_TOGGLE, null, new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    KnotyPreferences.setBoolean(WhiteListActivity.this, KnotyPreferences.TOGGLE_WHITELIST, true);
                    KnotyPreferences.setBoolean(WhiteListActivity.this, KnotyPreferences.TOGGLE_BLACKLIST, false);
                } else {
                    KnotyPreferences.setBoolean(WhiteListActivity.this, KnotyPreferences.TOGGLE_WHITELIST, false);
                }
            }
        });
        adapter.addItem(-1, null, -1, RecyclerAdapter.VIEW_TYPE_DIVIDER);

        ArrayList<String> list = KnotyPreferences.getPreferences(this, KnotyPreferences.WHITELIST_PREFERENCE);
        for(String str : list) {
            adapter.addItem(-1, str, R.drawable.x_button, RecyclerAdapter.VIEW_TYPE_NORMAL, KnotyPreferences.WHITELIST_PREFERENCE);
        }

        adapter.notifyDataSetChanged(); //데이터 변경 되었음을 알려준다

        //footer 보이게
        final View footer = (View)findViewById(R.id.footer);
        footer.setVisibility(View.VISIBLE);
        footer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddItemDialog dialog = new AddItemDialog(view.getContext(), AddItemDialog.WHITELIST_DIALOG);
                dialog.alert();
            }
        });
    }
}
