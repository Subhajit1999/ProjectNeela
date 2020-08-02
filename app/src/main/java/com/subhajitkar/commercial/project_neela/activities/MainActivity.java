package com.subhajitkar.commercial.project_neela.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.infideap.drawerbehavior.AdvanceDrawerLayout;
import com.subhajitkar.commercial.project_neela.R;
import com.subhajitkar.commercial.project_neela.fragments.ConversationsFragment;
import com.subhajitkar.commercial.project_neela.fragments.HomeFragment;
import com.subhajitkar.commercial.project_neela.objects.NewsObject;
import com.subhajitkar.commercial.project_neela.utils.APIManager;
import com.subhajitkar.commercial.project_neela.utils.StaticFields;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private static final String TAG = "MainActivity";

    private Toolbar toolbar;
    private AdvanceDrawerLayout drawer;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //widgets init
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //function callback
        setupDrawer();
        //default fragment
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
        navigationView.getMenu().getItem(0).setChecked(true);
        //showing tips dialog
        StaticFields.showLoadingDialog(this,"Loading...","",false);
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                StaticFields.hideLoadingDialog();
                setupTipsDialog();
            }
        }, 2000);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void setupDrawer(){
        Log.d(TAG, "setupDrawer: Setting up the custom navigation drawer");
        drawer = (AdvanceDrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){};
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        drawer.setViewScale(Gravity.START, 0.9f);
        drawer.setRadius(Gravity.START, 35);
        drawer.setViewElevation(Gravity.START, 20);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        Fragment fragment = new HomeFragment();
        switch(item.getItemId()){
            case R.id.main_home:
                fragment = new HomeFragment();
                break;
            case R.id.main_conversation:
                fragment = new ConversationsFragment();
                break;
        }
        navigationView.setCheckedItem(item.getItemId());
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setupTipsDialog(){
        Log.d(TAG, "setupTipsDialog: setting up tips dialog");
        //getting random tips
        String tips = getTipsCommands(getResources().getStringArray(R.array.commands_tips));
        //preparing dialog
        final Dialog dialog = new Dialog(this, R.style.DialogTheme);
        dialog.setContentView(R.layout.layout_dialog_tips);
        dialog.setTitle("");
        dialog.setCancelable(false);
        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //widgets init & set data
        TextView title = dialog.findViewById(R.id.tv_dialog_title);
        TextView subTitle = dialog.findViewById(R.id.tv_dialog_subtitle);
        TextView message = dialog.findViewById(R.id.tv_dialog_msg);
        message.setText(tips);
        //cancel button
        ImageView cancel = dialog.findViewById(R.id.iv_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private String getTipsCommands(String[] tips){
        Log.d(TAG, "getTipsCommands: getting random tips");
        Collections.shuffle(Arrays.asList(tips));
        String string="";
        for(int i=0; i<4; i++){
            string = string.concat(tips[i]);
            if (i!=3){
                string = string.concat("\n");
            }
        }
        return string;
    }
}