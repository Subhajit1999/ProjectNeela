package com.subhajitkar.commercial.project_neela;

import android.app.Application;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.subhajitkar.commercial.project_neela.utils.PreferenceManager;
import com.subhajitkar.commercial.project_neela.utils.StaticFields;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Init extends Application {
    private static final String TAG = "Init";

    public Init(){}

    @Override
    public void onCreate() {
        super.onCreate();

        //getting pref
        PreferenceManager prefs = new PreferenceManager(getApplicationContext());
        if (prefs.getUserPrefs()!=null){
            StaticFields.userCredentials = prefs.getUserPrefs();
        }
        //get the country code
        if (StaticFields.userCredentials.getUserCountry().isEmpty()){
            TelephonyManager tm = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
            StaticFields.userCredentials.setUserCountry(tm.getNetworkCountryIso());
            Log.d(TAG, "onCreate: country code: "+StaticFields.userCredentials.getUserCountry());
        }
        //init
        StaticFields.dailyNewsList = new ArrayList<>();
        //building news query
        StaticFields.dailyNewsQuery = "";
        for (int i=0; i<StaticFields.userCredentials.getNewsPref().size(); i++){
            StaticFields.dailyNewsQuery = StaticFields.dailyNewsQuery.concat(StaticFields.userCredentials.getNewsPref().get(i)
                    .replace(" ","+").toLowerCase());
            if (i!=StaticFields.userCredentials.getNewsPref().size()-1){
                StaticFields.dailyNewsQuery = StaticFields.dailyNewsQuery.concat(" OR ");
            }
        }
        StaticFields.dailyNewsQuery = "(("+StaticFields.dailyNewsQuery+") AND "+StaticFields.userCredentials.getUserCountry()+")";
        //getting current date
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        StaticFields.currentDate = dateFormat.format(Calendar.getInstance().getTime());
        Log.d(TAG, "onCreate: current date: "+StaticFields.currentDate);
    }
}
