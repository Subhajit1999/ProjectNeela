package com.subhajitkar.commercial.project_neela.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.wifi.hotspot2.pps.Credential;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.google.android.material.chip.Chip;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.subhajitkar.commercial.project_neela.R;
import com.subhajitkar.commercial.project_neela.objects.NewsObject;
import com.subhajitkar.commercial.project_neela.objects.User;

import java.util.List;

public class StaticFields {
    private static final String TAG = "StaticFields";

    //static fields
    public static User userCredentials = new User();
    public static KProgressHUD progressHUD;
    public static String NEWSAPI_BASE_URL = "http://newsapi.org/v2/";
    public static String NEWSAPI_AUTH_KEY = "094b87442ca64bcb9e84b5b139e463c4";
    public static List<NewsObject> dailyNewsList;
    public static String dailyNewsQuery;
    public static String currentDate;

    //static functions
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void setStatusBarBackground(Activity activity, int back) {
        Log.d(TAG, "setStatusBarBackground: customizing status bar");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            Drawable background = activity.getResources().getDrawable(back);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(activity.getResources().getColor(android.R.color.transparent));
//            window.setNavigationBarColor(activity.getResources().getColor(android.R.color.background_dark));
            window.setBackgroundDrawable(background);
        }
    }

    public static Chip addChip(Context _context, String text){
        Log.d(TAG, "addChip: adding chip view");
        Chip chip = new Chip(_context,null, R.attr.CustomChipStyle);
        chip.setText(text);
        chip.setTextColor(Color.BLACK);
        chip.setCheckable(true);
        chip.setCheckedIconVisible(false);
        chip.setRippleColorResource(R.color.blue_2);
        chip.setChipIconVisible(false);
        return chip;
    }

    public static String capitalizeString(String text){
        Log.d(TAG, "capitalizeString: capitalizing text");
        String[] words = text.split(" ");
        StringBuilder builder = new StringBuilder();
        for (String s : words) {
            String cap = s.substring(0, 1).toUpperCase() + s.substring(1);
            builder.append(cap).append(" ");
        }
        return builder.toString();
    }

    public static void showLoadingDialog(Context context, String msg1, String msg2, boolean cancelable){
        Log.d(TAG, "showLoadingDialog: showing custom loading bar");
        progressHUD = new KProgressHUD(context);
        progressHUD.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel(msg1)
                .setDetailsLabel(msg2)
                .setCancellable(cancelable)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();
    }

    public static void hideLoadingDialog(){
        Log.d(TAG, "hideLoadingDialog: hiding loading dialog");
        progressHUD.dismiss();
    }
}
