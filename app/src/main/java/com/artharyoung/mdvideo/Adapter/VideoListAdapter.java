package com.artharyoung.mdvideo.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.artharyoung.mdvideo.ApiConstant.Api;
import com.artharyoung.mdvideo.PlayerModule.PlayerActivity;
import com.artharyoung.mdvideo.R;
import com.artharyoung.mdvideo.Util.ImageLoader;

/**
 * Created by syamiadmin on 2016/6/8.
 */
public class VideoListAdapter extends RecyclerView.Adapter{

    private static final String TAG = "VideoListAdapter";
    private static final String POSITION = "position";

    private Context mContext;
    private String[][] mData;
    private LayoutInflater mLayoutInflater;
    public VideoListAdapter(Context context) {
        mContext = context;
        mData = Api.VIDEO_STREAM;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemCount() {
        return mData.length;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        VideoViewHolder videoViewHolder = (VideoViewHolder)holder;
        ImageLoader.LoadNormalImage(mContext,mData[position][1],videoViewHolder.imageView);
        videoViewHolder.textView.setText(mData[position][0]);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VideoViewHolder(mLayoutInflater.inflate(R.layout.video_list_item,parent,false));
    }

    class VideoViewHolder extends RecyclerView.ViewHolder {

        public TextView textView;
        public ImageView imageView;

        public VideoViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.video_item_preview);
            textView = (TextView) view.findViewById(R.id.video_item_title);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(mContext, PlayerActivity.class);
                    intent.putExtra(POSITION,getAdapterPosition());
                    mContext.startActivity(intent);
                }
            });
        }
    }
}
