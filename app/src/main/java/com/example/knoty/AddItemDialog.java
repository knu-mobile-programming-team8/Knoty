package com.example.knoty;

import android.app.AlertDialog;
import android.content.Context;
import android.widget.EditText;

public class AddItemDialog extends AlertDialog.Builder {
    public AlertDialog.Builder builder;

    public AddItemDialog(Context context) {
        super(context);
        builder = new AlertDialog.Builder(context);
        builder.setTitle("추가");
        builder.setMessage("다이얼로그 입니다");

        EditText editText = new EditText(context);
        builder.setView(editText);
    }

    public AlertDialog.Builder setTitle(String str) {
        return builder.setTitle(str);
    }

    public AlertDialog.Builder setMessage(String str) {
        return builder.setMessage(str);
    }

    public void alert() {
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
