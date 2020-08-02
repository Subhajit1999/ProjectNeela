package com.subhajitkar.commercial.project_neela.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.subhajitkar.commercial.project_neela.R;
import com.subhajitkar.commercial.project_neela.fragments.HomeFragment;
import com.subhajitkar.commercial.project_neela.objects.NewsObject;

import java.util.ArrayList;
import java.util.List;

public class HorizontalRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "HorizontalRecyclerAdapt";

    private static List<NewsObject> newsList;
    private Context mContext;
    private static int mAdapterId;
    private static OnItemClickListener mListener;

    public HorizontalRecyclerAdapter(Context context, int adapterId){
        mContext = context;
        mAdapterId = adapterId;
        newsList = new ArrayList<>();
    }

    public void setDailyNewsData(List<NewsObject> list){
        newsList = list;
        notifyDataSetChanged();
        HomeFragment.newsProgress.setVisibility(View.GONE);
    }

    public void setOnItemClickListener(HorizontalRecyclerAdapter.OnItemClickListener listener){
        mListener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=null;
        if (mAdapterId==1) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_news, parent, false);
        }
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        RecyclerViewHolder viewHolder = (RecyclerViewHolder) holder;
        if (mAdapterId==1){
            NewsObject newsItem = newsList.get(position);
            if (newsItem.getNewsImage().isEmpty()){
                viewHolder.newsImage.setImageResource(R.drawable.splash_avatar);
            }else {
                Picasso.get()
                        .load(newsItem.getNewsImage())
                        .placeholder(R.drawable.splash_avatar)
                        .into(viewHolder.newsImage);
            }
            viewHolder.newsTitle.setText(newsItem.getNewsTitle());
            viewHolder.newsSource.setText(String.format("• %s", newsItem.getNewsSource()));
        }
    }

    @Override
    public int getItemCount() {
        if (mAdapterId==1) {
            return newsList.size();
        }
        return 0;
    }
    public static class RecyclerViewHolder extends RecyclerView.ViewHolder{
        ImageView newsImage;
        TextView newsTitle, newsSource;
        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            //news
            if (mAdapterId==1){
                newsImage = itemView.findViewById(R.id.image_item_news);
                newsTitle = itemView.findViewById(R.id.text_item_news);
                newsSource = itemView.findViewById(R.id.tv_source_item_news);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(mListener!=null){
                            Log.d(TAG, "onClick: item clicked");
                            int position = getAdapterPosition();
                            if (position!=RecyclerView.NO_POSITION){
                                String url = newsList.get(position).getNewsUrl();
                                mListener.onItemClick(url);
                            }
                        }
                    }
                });
            }
        }
    }
    public interface OnItemClickListener{
        void onItemClick(String url);
    }
}
