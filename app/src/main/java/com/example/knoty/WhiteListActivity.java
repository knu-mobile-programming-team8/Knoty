package com.example.knoty;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import java.util.ArrayList;

public class WhiteListActivity extends AppCompatActivity {

    boolean flag = true; //true일 땐 dimiss만 호출된 경우이다 == 정상적으로 입력하고 확인 버튼을 누른 경우이다

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option); //OptionActivity와 동일한 레이아웃 사용

        //리사이클러뷰에 어댑터를 연결
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager manager = new LinearLayoutManager(this );
        recyclerView.setLayoutManager(manager);
        final RecyclerAdapter adapter = new RecyclerAdapter();
        recyclerView.setAdapter(adapter);

        //옵션 리스트
        adapter.addItem(-1, null, -1, RecyclerAdapter.VIEW_TYPE_SPACE);
        adapter.addItem(R.drawable.whitelist, "화이트 리스트", KnotyPreferences.getBoolean(this, KnotyPreferences.TOGGLE_WHITELIST) ? 1 : 0, RecyclerAdapter.VIEW_TYPE_TOGGLE, null, new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    KnotyPreferences.setBoolean(WhiteListActivity.this, KnotyPreferences.TOGGLE_WHITELIST, true);
                    KnotyPreferences.setBoolean(WhiteListActivity.this, KnotyPreferences.TOGGLE_BLACKLIST, false);
                    Toast.makeText(WhiteListActivity.this, "화이트 리스트와 블랙 리스트 중 하나만 활성화 시킬 수 있습니다. 블랙 리스트를 끕니다.", Toast.LENGTH_LONG).show();
                } else {
                    KnotyPreferences.setBoolean(WhiteListActivity.this, KnotyPreferences.TOGGLE_WHITELIST, false);
                }
            }
        });
        adapter.addItem(-1, null, -1, RecyclerAdapter.VIEW_TYPE_DIVIDER);

        ArrayList<String> list = KnotyPreferences.getStringSet(this, KnotyPreferences.WHITELIST_PREFERENCE);
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
                //onCancel 호출: 뒤로가기 키 눌렀을 때, 취소 버튼 눌렀을 때, 아무것도 입력 안 하고 확인버튼 눌렀을 때, 다이얼로그 바깥 눌렀을 때 (앞의 경우들 모두 dismiss도 호출되는데 cancel이 더 빨리 호출 된 후 dismiss가 호출됨)
                dialog.builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        flag = false;
                    }
                });
                //onDismiss 호출: 정상적으로 입력 후 확인 버튼을 눌렀을 때 (이 땐 cancel은 안 나오고 dismiss만 나옴)
                dialog.builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        if(flag) { //목록 추가 다이얼로그가 성공적으로 종료되면 Item을 추가시키고 새로고침
                            ArrayList<String> newList = KnotyPreferences.getStringSet(WhiteListActivity.this,  KnotyPreferences.WHITELIST_PREFERENCE);
                            adapter.addItem(-1, newList.get(newList.size() - 1), R.drawable.x_button, RecyclerAdapter.VIEW_TYPE_NORMAL, KnotyPreferences.WHITELIST_PREFERENCE);
                            adapter.notifyDataSetChanged();
                        }
                        flag = true;
                    }
                });
                dialog.alert();
            }
        });
    }
}
