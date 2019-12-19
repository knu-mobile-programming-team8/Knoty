package com.example.knoty;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class KnotyPreferences {
    private static final String SHARED_PREFERENCE_FILE = "knoty_pref";

    public static final String WHITELIST_PREFERENCE = "whitelist";
    public static final String BLACKLIST_PREFERENCE = "blacklist";
    public static final String TOGGLE_WHITELIST = "whilelist_toggle";
    public static final String TOGGLE_BLACKLIST = "blacklist_toggle";
    public static final String TOGGLE_DEPARTMENT_KNU = "knu_toggle";
    public static final String TOGGLE_DEPARTMENT_COMPUTER = "computer_toggle";

    //리스트 전체를 Arraylist로 가져온다 (없으면 텅빈 ArrayList 반환(null은 아님))
    public static ArrayList<String> getStringSet(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(SHARED_PREFERENCE_FILE, Context.MODE_PRIVATE);
        //초기 모델
//        Set<String> set = sp.getStringSet(key, null);
//        if(set == null) {
//            return new ArrayList<String>();
//        } else {
//            return new ArrayList<String>(set);
//        }

        //개선 모델
        String str = sp.getString(key, "");
        if(str.length() <= 2) { //데이터가 없는 경우. 사실 == 0만 해도 되는데 혹시나 싶어서
            return new ArrayList<String>();
        } else {
            return new ArrayList<String>(Arrays.asList(str.substring(1, str.length() - 1).split(", ")));
        }
    }

    //리스트 전체를 덮어써버린다
    public static void setStringSet(Context context, String key, ArrayList<String> list) {
        SharedPreferences sp = context.getSharedPreferences(SHARED_PREFERENCE_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        //초기 모델 - hashSet에 저장하면서 기존 ArrayList에 들어있던 순서대로 저장 안 되고 뒤죽박죽으로 저장되는 문제가 존재해서 StringSet으로 저장하는 게 아니라 사실 "[a, b, c]" 이렇게 되있는 arrayList.toString() 즉 문자열 하나로 저장
//        Set<String> set = new HashSet<String>();
//        set.addAll(list);
//        editor.putStringSet(key, set);

        //개선 모델
        editor.putString(key, list.toString()); // [aaa, bbb, ccc] 이렇게 저장됨. 괄호 포함.

        editor.commit();
    }

    //String Set 리스트에 value 하나만 추가한다
    public static void appendStringToStringSet(Context context, String key, String value) {
        ArrayList<String> list = KnotyPreferences.getStringSet(context, key);
        if(!list.contains(value)) { //value가 기존에 없었던 경우에만 새로 추가하고 이미 있었으면 그냥 무시
            list.add(value);
            KnotyPreferences.setStringSet(context, key, list);
        }
    }

    //리스트에서 value를 삭제한다 (원래 없는 경우에는 그냥 무시)
    public static void removeStringAtStringSet(Context context, String key, String value) {
        ArrayList<String> list = KnotyPreferences.getStringSet(context, key);
        list.remove(value);
        KnotyPreferences.setStringSet(context, key, list);
    }

    //리스트에 value가 있는지 확인한다
    public static boolean hasStringAtStringSet(Context context, String key, String value) {
        ArrayList<String> list = KnotyPreferences.getStringSet(context, key);
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
            ArrayList<String> list = getStringSet(context, WHITELIST_PREFERENCE);
            for(String str : list) {
                if(string2check.contains(str)) {
                    return true;
                }
            }
            return false;
        } else if(isBlackListMode) { //블랙 리스트 켜놨을 때
            ArrayList<String> list = getStringSet(context, BLACKLIST_PREFERENCE);
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

    //boolean 값 1개 읽어옴 (기본값 false)
    public static boolean getBoolean(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(SHARED_PREFERENCE_FILE, Context.MODE_PRIVATE);
        return sp.getBoolean(key, false);
    }

    //boolean 값 1개 읽어옴
    public static boolean getBoolean(Context context, String key, boolean defaultVal) {
        SharedPreferences sp = context.getSharedPreferences(SHARED_PREFERENCE_FILE, Context.MODE_PRIVATE);
        return sp.getBoolean(key, defaultVal);
    }
}
