package com.example.knoty;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class KnotyPreferences {
    private static final String SHARED_PREFERENCE_FILE = "knoty_pref";

    public static final String WHITELIST_PREFERENCE = "whitelist";
    public static final String BLACKLIST_PREFERENCE = "blacklist";
    public static final String TOGGLE_WHITELIST = "whilelist_toggle";
    public static final String TOGGLE_BLACKLIST = "blacklist_toggle";

    //리스트 전체를 Arraylist로 가져온다 (없으면 텅빈 ArrayList 반환(null은 아님))
    public static ArrayList<String> getPreferences(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(SHARED_PREFERENCE_FILE, Context.MODE_PRIVATE);
        Set<String> set = sp.getStringSet(key, null);
        if(set == null) {
            return new ArrayList<String>();
        } else {
            return new ArrayList<String>(set);
        }
    }

    //리스트 전체를 덮어써버린다
    public static void setPreferences(Context context, String key, ArrayList<String> list) {
        SharedPreferences sp = context.getSharedPreferences(SHARED_PREFERENCE_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        Set<String> set = new HashSet<String>();
        set.addAll(list);
        editor.putStringSet(key, set);
        editor.commit();
    }

    //리스트에 value 하나만 추가한다
    public static void appendPreferences(Context context, String key, String value) {
        ArrayList<String> list = KnotyPreferences.getPreferences(context, key);
        if(!list.contains(value)) { //value가 기존에 없었던 경우에만 새로 추가하고 이미 있었으면 그냥 무시
            list.add(value);
            KnotyPreferences.setPreferences(context, key, list);
        }
    }

    //리스트에서 value를 삭제한다 (원래 없는 경우에는 그냥 무시)
    public static void removePreferences(Context context, String key, String value) {
        ArrayList<String> list = KnotyPreferences.getPreferences(context, key);
        list.remove(value);
        KnotyPreferences.setPreferences(context, key, list);
    }

    //리스트에 value가 있는지 확인한다
    public static boolean hasPreferences(Context context, String key, String value) {
        ArrayList<String> list = KnotyPreferences.getPreferences(context, key);
        if(list.contains(value)) {
            return true;
        } else {
            return false;
        }
    }

    //Scrapping 해서 얻어온 공지사항 title을 파라미터로 넣어주면 푸쉬 알림을 해줘야하는지 아닌지 알려준다 (블랙 리스트에 들어있는지 화이트 리스트에 들어있는지 등을 알아서 검사해줌)
    public static boolean shouldPush(Context context, String string2check) {
        boolean isWhiteListMode = getBoolean(context, TOGGLE_WHITELIST);
        boolean isBlackListMode = getBoolean(context, TOGGLE_BLACKLIST);

        if(isWhiteListMode) { //화이트 리스트 켜놨을 때
            ArrayList<String> list = getPreferences(context, WHITELIST_PREFERENCE);
            for(String str : list) {
                if(string2check.contains(str)) {
                    return true;
                }
            }
            return false;
        } else if(isBlackListMode) { //블랙 리스트 켜놨을 때
            ArrayList<String> list = getPreferences(context, BLACKLIST_PREFERENCE);
            for(String str : list) {
                if(string2check.contains(str)) {
                    return false;
                }
            }
            return true;
        } else { //둘 다 꺼놨을 때
            return true;
        }
    }

    //boolean 값 1개만 저장
    public static void setBoolean(Context context, String key, boolean value) {
        SharedPreferences sp = context.getSharedPreferences(SHARED_PREFERENCE_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    //boolean 값 1개 읽어옴
    public static boolean getBoolean(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(SHARED_PREFERENCE_FILE, Context.MODE_PRIVATE);
        return sp.getBoolean(key, false);
    }
}
