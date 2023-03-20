package com.example.a5dayappchat2.Activity.Preferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.a5dayappchat2.Activity.constant.Constant;

public class PreferencesFriendsId {

    private final SharedPreferences sharedPreferences;


    public PreferencesFriendsId(Context context){
        sharedPreferences = context.getSharedPreferences(Constant.FRIENDS_PREFER,Context.MODE_PRIVATE);
    }

    public void putString(String key,String value){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getString(String key){
        return sharedPreferences.getString(key,null);
    }

    public void clear(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

}
