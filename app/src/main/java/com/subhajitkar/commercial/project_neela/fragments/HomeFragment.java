package com.subhajitkar.commercial.project_neela.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.subhajitkar.commercial.project_neela.R;
import com.subhajitkar.commercial.project_neela.adapters.HorizontalRecyclerAdapter;
import com.subhajitkar.commercial.project_neela.objects.NewsObject;
import com.subhajitkar.commercial.project_neela.utils.APIManager;
import com.subhajitkar.commercial.project_neela.utils.StaticFields;
import com.takusemba.multisnaprecyclerview.MultiSnapRecyclerView;

import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragment extends Fragment implements HorizontalRecyclerAdapter.OnItemClickListener{
    private static final String TAG = "HomeFragment";

    private GifImageView splashGif;
    private LinearLayout updatesLayout, errorLayout;
    private TextView title;
    private BroadcastReceiver mBroadcastReceiver;
    private APIManager api;
    private HorizontalRecyclerAdapter adapter;
    private MultiSnapRecyclerView dailyNewsRecycler;
    public static ProgressBar newsProgress, quoteProgress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        updatesLayout = view.findViewById(R.id.layout_updates);
        errorLayout = view.findViewById(R.id.layout_error);
        title = view.findViewById(R.id.home_title);
        quoteProgress = view.findViewById(R.id.quotes_progress);
        newsProgress = view.findViewById(R.id.news_progress);
        //setup recycler
        dailyNewsRecycler = view.findViewById(R.id.daily_news_recycler);
        dailyNewsRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        adapter = new HorizontalRecyclerAdapter(getContext(), 1);
        adapter.setOnItemClickListener(this);
        dailyNewsRecycler.setAdapter(adapter);
        //set the title with username
        String user;
        if (!StaticFields.userCredentials.getUserName().isEmpty()){
            user = StaticFields.userCredentials.getUserName();
        }else{
            user = "User";
        }
        title.setText(String.format("Welcome back,\n%s", StaticFields.capitalizeString(user)));

        setupGifImage(view);
        checkNetworkAvailability();
    }

    private void setupGifImage(View view){
        Log.d(TAG, "setupGifImage: setting up gif avatar");
        //widgets init
        splashGif = view.findViewById(R.id.splash_gif);
    }

    private void checkNetworkAvailability(){
        Log.d(TAG, "checkNetworkAvailability: log message");
        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle extras = intent.getExtras();
                NetworkInfo info = (NetworkInfo) extras.getParcelable("networkInfo");
                NetworkInfo.State state = info.getState();

                if (state == NetworkInfo.State.CONNECTED) {
                    //tasks when connection is back
                    hideError();
                    //daily news data
                    if (StaticFields.dailyNewsList.isEmpty()){
                        fetchDailyNews();
                    }else{
                        adapter.setDailyNewsData(StaticFields.dailyNewsList);
                    }
                } else {
                    //handle network error when connection is gone
                    showError();
                }
            }
        };
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        getActivity().registerReceiver(mBroadcastReceiver,intentFilter);
    }

    private void showError(){
        Log.d(TAG, "showError: showing error layout");
        updatesLayout.setVisibility(View.GONE);
        errorLayout.setVisibility(View.VISIBLE);
    }

    private void hideError(){
        Log.d(TAG, "hideError: hiding error layout");
        updatesLayout.setVisibility(View.VISIBLE);
        errorLayout.setVisibility(View.GONE);
    }

    @Override
    public void onDestroy() {
        getActivity().unregisterReceiver(mBroadcastReceiver);
        super.onDestroy();
    }

    private void initRetrofit(String url){
        //retrofit init
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(APIManager.class);
        Log.d(TAG, "initRetrofit: initialized");
    }

    private void fetchDailyNews(){
        initRetrofit(StaticFields.NEWSAPI_BASE_URL);

        if (!StaticFields.dailyNewsList.isEmpty()) {
            StaticFields.dailyNewsList.clear();
        }
        Call<JsonObject> callDailyNews = api.getDailyNews(StaticFields.dailyNewsQuery,"en","relevancy",
                StaticFields.currentDate, StaticFields.NEWSAPI_AUTH_KEY);
        Log.d(TAG, "fetchDailyNews: Request Url: "+api.getDailyNews(StaticFields.dailyNewsQuery,"en","relevancy",
                StaticFields.currentDate, StaticFields.NEWSAPI_AUTH_KEY).request().url().toString());
        callDailyNews.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.d(TAG, "onResponse: Json: "+response.body());
                if (response.isSuccessful() && response.body()!=null){
                    JsonArray JsonArr;
                    JsonObject jsonObj = response.body().getAsJsonObject();
                    JsonArr = jsonObj.getAsJsonArray("articles");

                    for (int i=0; i<JsonArr.size();i++){
                        JsonObject obj = JsonArr.get(i).getAsJsonObject();
                        String source = obj.get("source").getAsJsonObject().get("name").isJsonNull()?"":obj.get("source").getAsJsonObject().get("name").getAsString();
                        String imageUrl = obj.get("urlToImage").isJsonNull()?"":obj.get("urlToImage").getAsString();
                        String title = obj.get("title").isJsonNull()?"":obj.get("title").getAsString();
                        String url = obj.get("url").isJsonNull()?"":obj.get("url").getAsString();
                        StaticFields.dailyNewsList.add(new NewsObject(imageUrl,title,source,url));
                        Log.d(TAG, "onResponse: source: "+source+"\n imageUrl: "+imageUrl+"\ntitle: "+title
                        +"\nUrl: "+url);
                    }
                    adapter.setDailyNewsData(StaticFields.dailyNewsList);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(getContext(), "Sorry! Error getting daily news.", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onFailure: Data fetch error: "+t.getMessage());
            }
        });
    }

    @Override
    public void onItemClick(String url) {
        Log.d(TAG, "onItemClick: item clicked");
        WebDialogFragment.display(getActivity().getSupportFragmentManager(),url);
    }
}