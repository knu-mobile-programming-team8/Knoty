package com.example.knoty;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

public class RebootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            Log.d("==========", "부팅 완료");
            Toast.makeText(context, "부팅 완료", Toast.LENGTH_SHORT).show();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { //오레오 버전 이상 (액티비티 없을땐 foregroundService로 시작해야함)
                Intent in = new Intent(context, KnotyService.class);
                context.startForegroundService(in);
            } else { //오레오 버전 이하 (액티비티 없어도 backgroundService로 실행 가능)
                Intent in = new Intent(context, KnotyService.class);
                context.startService(in);
            }
        } else {
            Log.d("==========리부트 리시버 else문", intent.getAction().toString());
        }
    }
}
