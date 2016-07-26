package com.studyjams.mdvideo.Fragment;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.studyjams.mdvideo.Adapter.VideoLocalCursorAdapter;
import com.studyjams.mdvideo.PlayerModule.PlayerActivity;
import com.studyjams.mdvideo.R;
import com.studyjams.mdvideo.View.ProRecyclerView.RecyclerViewItemClickListener;

/**
 * Created by zwx on 2016/7/9.
 */

public class VideoLocalListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>,
        RecyclerViewItemClickListener.OnItemClickListener{
    private static final String TAG = "LocalVideoListFragment";
    private static final String ARG_PARAM = "param";

    private String mParam;

    private RecyclerView mRecyclerView;

    //本地视频的loader编号
    private static final int LOCAL_VIDEO_LOADER = 0;
    private VideoLocalCursorAdapter mLocalVideoCursorAdapter;

    public VideoLocalListFragment() {
        // Required empty public constructor
    }

    public static VideoLocalListFragment newInstance(String param) {
        VideoLocalListFragment fragment = new VideoLocalListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM, param);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam = getArguments().getString(ARG_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View parent = inflater.inflate(R.layout.fragment_video_local_list, container, false);
        mRecyclerView = (RecyclerView) parent.findViewById(R.id.local_video_list_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        //设置布局管理器
        mRecyclerView.setLayoutManager(layoutManager);
        //设置为垂直布局，这也是默认的
        layoutManager.setOrientation(OrientationHelper.VERTICAL);

        mLocalVideoCursorAdapter = new VideoLocalCursorAdapter(getActivity());
        mRecyclerView.setAdapter(mLocalVideoCursorAdapter);
        mRecyclerView.addOnItemTouchListener(new RecyclerViewItemClickListener(getActivity(), this));
        return parent;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().getSupportLoaderManager().initLoader(LOCAL_VIDEO_LOADER, null, this);
    }

    @Override
    public void onItemClick(View view, int position) {

            Intent intent = new Intent(getActivity(), PlayerActivity.class)
                    .setData(Uri.parse(mLocalVideoCursorAdapter.getItemData(position).getPath()))
                    .putExtra(PlayerActivity.CONTENT_ID_EXTRA, String.valueOf(mLocalVideoCursorAdapter.getItemData(position).getId()))
                    .putExtra(PlayerActivity.CONTENT_TYPE_EXTRA, mLocalVideoCursorAdapter.getItemData(position).getMimeType());
            getActivity().startActivity(intent);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch(id) {
            case LOCAL_VIDEO_LOADER:
                return new CursorLoader(
                        getActivity(),
                        VideoLocalCursorAdapter.LOCAL_VIDEO_URI,
                        null,
                        null,
                        null,
                        null
                );
            default:
                throw new UnsupportedOperationException("Unknown loader id: " + id);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        switch(loader.getId()) {
            case LOCAL_VIDEO_LOADER:
                mLocalVideoCursorAdapter.swapCursor(null);
                break;
            default:
                throw new UnsupportedOperationException("Unknown loader id: " + loader.getId());
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch(loader.getId()) {
            case LOCAL_VIDEO_LOADER:
                mLocalVideoCursorAdapter.swapCursor(data);
                break;
            default:
                throw new UnsupportedOperationException("Unknown loader id: " + loader.getId());
        }

    }
}