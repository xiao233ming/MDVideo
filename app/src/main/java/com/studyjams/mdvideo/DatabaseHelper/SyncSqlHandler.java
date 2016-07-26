package com.studyjams.mdvideo.DatabaseHelper;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.studyjams.mdvideo.R;
import com.studyjams.mdvideo.Util.Tools;

/**
 * Created by syamiadmin on 2016/7/26.
 */
public class SyncSqlHandler extends AsyncQueryHandler{

    private static final String TAG = "SyncSqlHandler";

    //查询后进行插入操作
    public static final int Query_Insert = 0;
    //查询后进行更新操作
    public static final int Query_Update = 1;

    private ContentResolver mContentResolver;
    private Uri mUri;
    public SyncSqlHandler(ContentResolver cr,Uri uri) {
        super(cr);
        mContentResolver = cr;
        mUri = uri;
    }

    @Override
    protected void onDeleteComplete(int token, Object cookie, int result) {
        super.onDeleteComplete(token, cookie, result);
        Log.d(TAG, "onDeleteComplete: ");
        mContentResolver.notifyChange(mUri, null);
    }

    @Override
    protected void onInsertComplete(int token, Object cookie, Uri uri) {
        super.onInsertComplete(token, cookie, uri);
        Log.d(TAG, "onInsertComplete: ");
        mContentResolver.notifyChange(mUri, null);
    }

    @Override
    protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
        super.onQueryComplete(token, cookie, cursor);
        Log.d(TAG, "onQueryComplete: ");
        switch (token){
            case Query_Insert:
                ContentValues values = new ContentValues();
                values.put(Tables.Video_title, "测试");
                values.put(Tables.Video_album, "111111");
                values.put(Tables.Video_artist, "222222");
                values.put(Tables.Video_displayName, "33333");
                values.put(Tables.Video_mimeType, "44444");
                values.put(Tables.Video_path, R.mipmap.empty_photo);
                values.put(Tables.Video_size, 1024*1024*10);
                values.put(Tables.Video_duration, 42423);
                values.put(Tables.Video_playDuration, 14213);
                values.put(Tables.Video_createdDate, Tools.getCurrentTimeMillis());

                for (int i = 0; i < 10; i++) {
                    startInsert(token, null, (Uri) cookie, values);
                }

                break;
            case Query_Update:

                break;
            default:break;
        }

        mContentResolver.notifyChange(mUri, null);

    }

    @Override
    protected void onUpdateComplete(int token, Object cookie, int result) {
        super.onUpdateComplete(token, cookie, result);
        Log.d(TAG, "onUpdateComplete: ");
        mContentResolver.notifyChange(mUri, null);
    }
}
