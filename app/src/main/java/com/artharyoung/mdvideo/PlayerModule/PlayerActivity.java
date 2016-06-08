package com.artharyoung.mdvideo.PlayerModule;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.artharyoung.mdvideo.ApiConstant.Api;
import com.artharyoung.mdvideo.R;
import com.pili.pldroid.player.widget.PLVideoTextureView;

public class PlayerActivity extends AppCompatActivity {
    private static final String POSITION = "position";
    private PLVideoTextureView mVideoView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        int position = intent.getIntExtra(POSITION,-1);

        mVideoView = (PLVideoTextureView) findViewById(R.id.PLVideoTextureView);

        if(position != -1){
            getSupportActionBar().setTitle(Api.VIDEO_STREAM[position][0]);
            mVideoView.setVideoPath(Api.VIDEO_STREAM[position][1]);
            mVideoView.start();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mVideoView.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mVideoView.stopPlayback();
    }
}
