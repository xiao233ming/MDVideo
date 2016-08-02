package com.studyjams.mdvideo;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.exoplayer.util.Util;
import com.studyjams.mdvideo.Adapter.MainPagerAdapter;
import com.studyjams.mdvideo.DatabaseHelper.SyncSqlHandler;
import com.studyjams.mdvideo.DatabaseHelper.Tables;
import com.studyjams.mdvideo.DatabaseHelper.VideoProvider;
import com.studyjams.mdvideo.Fragment.VideoLocalListFragment;
import com.studyjams.mdvideo.PlayerModule.PlayerActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,VideoLocalListFragment.OnVideoRefreshListener {

    private static final String TAG = "MainActivity";
    private static final int REQUEST_CODE = 1;
    private List<String> mData;
    private MainPagerAdapter mainPagerAdapter;

    //查询本地媒体库
    public static final Uri MEDIA_VIDEO_URI = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
    private SyncSqlHandler mSyncSqlHandler;

    public static final String PLAY_HISTORY_ACTION = "com.studyjams.mdvideo.HISTORY";
    private MyReceiver mMyReceiver;
    private IntentFilter mIntentFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        initView();
    }

    private void initData(){
        mData = new ArrayList<>();
        mData.add(getResources().getString(R.string.menu_video_local));
        mData.add(getResources().getString(R.string.menu_video_history));
//        mData.add(getResources().getString(R.string.menu_video_info));

        mMyReceiver = new MyReceiver();
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(PLAY_HISTORY_ACTION);
        registerReceiver(mMyReceiver,mIntentFilter);

        mSyncSqlHandler = new SyncSqlHandler(getContentResolver());
        refreshData();
    }

    @Override
    public void onVideoRefresh() {
        refreshData();
    }

    public void refreshData(){
        /**
         * token:一个令牌，主要用来标识查询,保证唯一即可.需要跟onXXXComplete方法传入的一致。
         * （当然你也可以不一致，同样在数据库的操作结束后会调用对应的onXXXComplete方法 ）
         * cookie:你想传给onXXXComplete方法使用的一个对象。(没有的话传递null即可)
         * Uri :uri（进行查询的通用资源标志符）:
         * projection: 查询的列
         * selection:  限制条件
         * selectionArgs: 查询参数
         * orderBy: 排序条件
         */
        mSyncSqlHandler.startQuery(SyncSqlHandler.MEDIA_QUERY_INSERT, null, MEDIA_VIDEO_URI, null, null, null,null);
        mSyncSqlHandler.startQuery(SyncSqlHandler.LOCAL_QUERY_DELETE,null,
                VideoProvider.VIDEO_PLAY_HISTORY_URI,
                new String[]{Tables.Video_path},
                null,null,null);
    }

    private void initView(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.main_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fileChooser();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        TabLayout tabLayout = (TabLayout)findViewById(R.id.main_view_table);
        ViewPager mViewpager = (ViewPager)findViewById(R.id.main_view_pager);
        mainPagerAdapter = new MainPagerAdapter(getSupportFragmentManager(),mData);
        mViewpager.setAdapter(mainPagerAdapter);
        tabLayout.setupWithViewPager(mViewpager);
        mViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                /**切换页面时显示文件打开**/
                fab.show();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private class MyReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {

            if(intent.getAction().equals(PLAY_HISTORY_ACTION)) {

                ContentValues values = new ContentValues();
                values.put(Tables.Video_playDuration, intent.getStringExtra(Tables.Video_playDuration));
                values.put(Tables.Video_createdDate, intent.getStringExtra(Tables.Video_createdDate));
                Log.d(TAG, "============onReceive: " + intent.getStringExtra(Tables.Video_playDuration));
                /**更新播放时长**/
                Uri updateUri = ContentUris.withAppendedId(VideoProvider.VIDEO_PLAY_HISTORY_URI,
                        Long.valueOf(intent.getStringExtra(Tables.Video_id)));
                mSyncSqlHandler.startUpdate(SyncSqlHandler.LOCAL_UPDATE, null,
                        updateUri, values, null,
                        new String[]{Tables.Video_playDuration, Tables.Video_createdDate});
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mMyReceiver);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

//        MenuItem searchViewMenuItem = menu.findItem(R.id.action_search);
//        SearchView searchView = (SearchView) searchViewMenuItem.getActionView();

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        switch(item.getItemId()){

            case R.id.menu_video_share:
                shareWithFriends();
                break;
            case R.id.menu_video_send:

                sendEmail();
                break;
            default:

                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK) {

            switch (requestCode){
                case REQUEST_CODE:
                    Intent intent = new Intent(this, PlayerActivity.class)
                            .setData(data.getData())
                            .putExtra(PlayerActivity.CONTENT_ID_EXTRA, REQUEST_CODE)
                            .putExtra(PlayerActivity.CONTENT_TYPE_EXTRA, Util.TYPE_OTHER)
                            .putExtra(PlayerActivity.PROVIDER_EXTRA, "0");
                    startActivity(intent);
                    break;
                default:break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Share with friends
     **/
    private void shareWithFriends() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.send_share_url));
        intent.setType("text/plain");
        startActivity(Intent.createChooser(intent, getString(R.string.send_share_title)));
    }

    /**
     * Feedback to the developers
     */
    private void sendEmail() {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse(getString(R.string.send_share_email)));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private void fileChooser(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("video/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(Intent.createChooser(intent, getString(R.string.menu_folder_title)), REQUEST_CODE);
        }else{
            // Potentially direct the user to the Market with a Toast
            Toast.makeText(this, getString(R.string.menu_folder_desc), Toast.LENGTH_SHORT).show();
        }
    }
}
