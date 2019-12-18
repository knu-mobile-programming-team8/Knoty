package com.example.knoty;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class AddItemDialog extends AlertDialog.Builder {
    private static Context context;
    public AlertDialog.Builder builder;
    private LinearLayout container;
    private LinearLayout.LayoutParams layoutParams;
    private EditText editText;
    private int kind;

    public static final int WHITELIST_DIALOG = 0;
    public static final int BLACKLIST_DIALOG = 1;

    //kind는 화이트리스트인지 블랙리스트인지 (WHITELIST_DIALOG or BLACKLIST_DIALOG)
    public AddItemDialog(final Context context, int kind) {
        super(context);
        this.context = context;
        this.kind = kind;
        builder = new AlertDialog.Builder(context);

        builder.setTitle(kind == WHITELIST_DIALOG ? R.string.whitelist_add : R.string.black_add);

        //에딧 텍스트 설정
        container = new LinearLayout(context);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(dp2px(20), dp2px(10), dp2px(20), 0);
        editText = new EditText(context);
        editText.setHint(R.string.edittext_hint);
        container.addView(editText);
        editText.setLayoutParams(layoutParams);
        builder.setView(container);

        //긍정적 버튼
        builder.setPositiveButton(R.string.enroll, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                addListItem(editText.getText().toString());
            }
        });

        //부정적 버튼
        builder.setNegativeButton(R.string.cancel, null); //버튼만 있고 아무 기능도 안 함
    }

    public static int dp2px(float dips)
    {
        return (int) (dips * context.getResources().getDisplayMetrics().density + 0.5f);
    }

    public AlertDialog.Builder setTitle(String str) {
        return builder.setTitle(str);
    }

    public AlertDialog.Builder setMessage(String str) {
        return builder.setMessage(str);
    }

    public AlertDialog.Builder setTitle(int id) {
        return builder.setTitle(context.getResources().getString(id));
    }

    public AlertDialog.Builder setMessage(int id) {
        return builder.setTitle(context.getResources().getString(id));
    }

    public void alert() {
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    //화이트리스트나 블랙리스트에 단어를 추가 (화이트는 ADD_TO_WHITELIST, 블랙은 ADD_TO_BLACKLIST)
    public void addListItem(String str) {
        if(str == null || str.equals("")) return; //예외 처리(암것도 입력 안하고 엔터하면 ""이 들어옴)

        KnotyPreferences.appendPreferences(context, (kind == WHITELIST_DIALOG ? KnotyPreferences.WHITELIST_PREFERENCE : KnotyPreferences.BLACKLIST_PREFERENCE), str);
        Toast.makeText(context, str + "를 추가하였습니다.", Toast.LENGTH_SHORT).show();
    }
}
