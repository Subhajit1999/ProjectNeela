package com.subhajitkar.commercial.project_neela.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.subhajitkar.commercial.project_neela.objects.User;

import java.lang.reflect.Type;

public class PreferenceManager {
    private static final String TAG = "PreferenceManager";
    private Context mContext;
    private SharedPreferences preference;
    private final String KEY_USER_PREFERENCE = "UserPrefsKey";
    private final String KEY_PREFERENCES = "com.subhajitkar.commercial.project_neela";

    public PreferenceManager(Context context){
        mContext = context;
    }

    public void setUserPrefs(User user){
        Log.d(TAG, "setUserPrefs: setting user prefs data");
        preference = mContext.getSharedPreferences(KEY_PREFERENCES, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = gson.toJson(user);
        SharedPreferences.Editor editor = preference.edit();
        editor.putString(KEY_USER_PREFERENCE, json );
        editor.apply();
    }

    public User getUserPrefs(){
        preference = mContext.getSharedPreferences(KEY_PREFERENCES, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = preference.getString(KEY_USER_PREFERENCE, "");

        User user = null;
        if (!json.isEmpty()){
            Type type = new TypeToken<User>() {}.getType();
            user = gson.fromJson(json, type);
        }
        return user;
    }
}
