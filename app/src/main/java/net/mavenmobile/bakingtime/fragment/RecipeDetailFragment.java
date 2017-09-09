package net.mavenmobile.bakingtime.fragment;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import net.mavenmobile.bakingtime.R;
import net.mavenmobile.bakingtime.model.Step;
import net.mavenmobile.bakingtime.rest.ApiClient;
import net.mavenmobile.bakingtime.rest.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeDetailFragment extends Fragment {


    @BindView(R.id.player_view)
    SimpleExoPlayerView mPlayerView;
    @BindView(R.id.tv_step_desc)
    TextView mTvStepDesc;
    Unbinder unbinder;
    @BindView(R.id.button_prev)
    ImageButton mButtonPrev;
    @BindView(R.id.button_next)
    ImageButton mButtonNext;
    @BindView(R.id.iv_thumbnail)
    ImageView mIvThumbnail;

    private Context mContext;
    private SimpleExoPlayer mPlayer;
    private List<Step> mStepList;
    private Step step;
    private ApiInterface apiService;
    private String TAG = "RecipeDetailFragment: ";
    private int position;
    private boolean playWhenReady = true;
    private int currentWindow;
    private long playbackPosition;
    private String videoUrl;

    public RecipeDetailFragment() {
        // Required empty public constructor
    }

    public static RecipeDetailFragment newInstance(String videoURL, String stepDesc) {
        Bundle args = new Bundle();
        args.putString("videoURL", videoURL);
        args.putString("desc", stepDesc);
        RecipeDetailFragment fragment = new RecipeDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("playWhenReady", playWhenReady);
        outState.putInt("currentWindow", currentWindow);
        outState.putLong("playbackPosition", playbackPosition);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt("position");
        ArrayList<Step> stepArray = getArguments().getParcelableArrayList("stepList");
        mStepList = new ArrayList<>();
        mStepList.addAll(stepArray);

        step = mStepList.get(position);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recipe_detail, container, false);
        unbinder = ButterKnife.bind(this, view);

        apiService = ApiClient.getClient().create(ApiInterface.class);

        if (savedInstanceState != null) {
            playWhenReady = savedInstanceState.getBoolean("playWhenReady");
            currentWindow = savedInstanceState.getInt("currentWindow");
            playbackPosition = savedInstanceState.getLong("playbackPosition");
        }

        buttonVisibilityCheck();
        initView(step);
        initButton();
        return view;
    }

    private void initButton() {
        mButtonPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (position > 0) {
                    position -= 1;
                    Step step = mStepList.get(position);
                    releasePlayer();
                    initView(step);
                    initializePlayer(step);
                }
                buttonVisibilityCheck();
            }
        });
        mButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (position < mStepList.size()) {
                    position += 1;
                    Step step = mStepList.get(position);
                    releasePlayer();
                    initView(step);
                    initializePlayer(step);
                }
                buttonVisibilityCheck();
            }
        });
    }

    private void buttonVisibilityCheck() {
        mButtonPrev.setVisibility(View.VISIBLE);
        mButtonNext.setVisibility(View.VISIBLE);

        if (position == 0) {
            mButtonPrev.setVisibility(View.INVISIBLE);
        } else if (position == mStepList.size() - 1) {
            mButtonNext.setVisibility(View.INVISIBLE);
        }
    }

    private void initView(Step step) {
        Glide.with(getContext())
                .load(step.getThumbnailURL())
                .placeholder(R.drawable.ic_insert_photo)
                .error(R.drawable.ic_broken_image)
                .into(mIvThumbnail);
        mTvStepDesc.setText(step.getDescription());
    }

    private void initializePlayer(Step step) {
        mPlayer = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(getContext()),
                new DefaultTrackSelector(), new DefaultLoadControl());

        mPlayerView.setPlayer(mPlayer);

        mIvThumbnail.setVisibility(View.GONE);
        mPlayer.setPlayWhenReady(playWhenReady);
        mPlayer.seekTo(currentWindow, playbackPosition);

        videoUrl = step.getVideoURL();
        Uri uri = Uri.parse(videoUrl);
        MediaSource mediaSource = buildMediaSource(uri);
        mPlayer.prepare(mediaSource, true, false);
    }

    private MediaSource buildMediaSource(Uri uri) {
        return new ExtractorMediaSource(uri,
                new DefaultHttpDataSourceFactory("ua"),
                new DefaultExtractorsFactory(), null, null);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializePlayer(step);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            hideSystemUi();
        }
        if ((Util.SDK_INT <= 23 || mPlayer == null)) {
            initializePlayer(step);
        }
    }

    @SuppressLint("InlinedApi")
    private void hideSystemUi() {
        mPlayerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    private void releasePlayer() {
        if (mPlayer != null) {
            playbackPosition = mPlayer.getCurrentPosition();
            currentWindow = mPlayer.getCurrentWindowIndex();
            playWhenReady = mPlayer.getPlayWhenReady();
            mPlayer.release();
            mPlayer = null;
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
