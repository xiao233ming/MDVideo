package com.studyjams.mdvideo.Fragment;


import android.database.Cursor;
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

import com.studyjams.mdvideo.Adapter.VideoPlayHistoryCursorAdapter;
import com.studyjams.mdvideo.DatabaseHelper.VideoProvider;
import com.studyjams.mdvideo.R;
import com.studyjams.mdvideo.View.ProRecyclerView.RecyclerViewItemClickListener;

public class VideoPlayHistoryFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>,
        RecyclerViewItemClickListener.OnItemClickListener{
    private static final String TAG = "HistoryFragment";
    private static final String ARG_PARAM = "param";

    private String mParam;

    private RecyclerView mRecyclerView;
    private VideoPlayHistoryCursorAdapter mVideoPlayHistoryCursorAdapter;

    /**历史记录Loader的编号**/
    private static final int VIDEO_PLAY_HISTORY_LOADER = 1;
    public VideoPlayHistoryFragment() {
        // Required empty public constructor
    }

    public static VideoPlayHistoryFragment newInstance(String param) {
        VideoPlayHistoryFragment fragment = new VideoPlayHistoryFragment();
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
        View parent = inflater.inflate(R.layout.fragment_video_play_history, container, false);
        mRecyclerView = (RecyclerView) parent.findViewById(R.id.video_play_history_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);

        mVideoPlayHistoryCursorAdapter = new VideoPlayHistoryCursorAdapter(getActivity());
        mRecyclerView.setAdapter(mVideoPlayHistoryCursorAdapter);
        mRecyclerView.addOnItemTouchListener(new RecyclerViewItemClickListener(getActivity(), this));
        return parent;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().getSupportLoaderManager().initLoader(VIDEO_PLAY_HISTORY_LOADER, null, this);
    }

    @Override
    public void onItemClick(View view, int position) {

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch(id) {
            case VIDEO_PLAY_HISTORY_LOADER:
                return new CursorLoader(
                        getActivity(),
                        VideoProvider.VIDEO_PLAY_HISTORY_URI,
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
            case VIDEO_PLAY_HISTORY_LOADER:
                mVideoPlayHistoryCursorAdapter.swapCursor(null);
                break;
            default:
                throw new UnsupportedOperationException("Unknown loader id: " + loader.getId());
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch(loader.getId()) {
            case VIDEO_PLAY_HISTORY_LOADER:
                mVideoPlayHistoryCursorAdapter.swapCursor(data);
                break;
            default:
                throw new UnsupportedOperationException("Unknown loader id: " + loader.getId());
        }

    }
}
