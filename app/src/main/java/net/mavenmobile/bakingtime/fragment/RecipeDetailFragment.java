package net.mavenmobile.bakingtime.fragment;


import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import net.mavenmobile.bakingtime.R;
import net.mavenmobile.bakingtime.model.Step;
import net.mavenmobile.bakingtime.rest.ApiClient;
import net.mavenmobile.bakingtime.rest.ApiInterface;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeDetailFragment extends Fragment {


    @BindView(R.id.exo_player)
    SimpleExoPlayerView mExoPlayer;
    @BindView(R.id.tv_step_desc)
    TextView mTvStepDesc;
    Unbinder unbinder;
    @BindView(R.id.button_prev)
    ImageButton mButtonPrev;
    @BindView(R.id.button_next)
    ImageButton mButtonNext;

    private Context mContext;
    private SimpleExoPlayer mPlayer;
    private Step step;
    private ApiInterface apiService;
    private String TAG = "RecipeDetailFragment: ";
    private int position;

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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt("position");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recipe_detail, container, false);
        unbinder = ButterKnife.bind(this, view);

        apiService = ApiClient.getClient().create(ApiInterface.class);

        Bundle args = getArguments();
        if (args != null) {
            step = args.getParcelable("step");
        }
        initView(step);
        return view;
    }

    private void initView(Step step) {
        mTvStepDesc.setText(step.getDescription());
        String videoUrl = step.getVideoURL();
        if (!videoUrl.isEmpty()) {
            initPlayer(videoUrl);
        }

        mButtonPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), " PREV", Toast.LENGTH_SHORT).show();
            }
        });
        mButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "NEXT", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void initPlayer(String url) {
        mExoPlayer.setVisibility(View.VISIBLE);

        TrackSelector trackSelector = new DefaultTrackSelector();
        LoadControl loadControl = new DefaultLoadControl();
        SimpleExoPlayer player = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
        mExoPlayer.setPlayer(player);

        String userAgent = Util.getUserAgent(getContext(), getContext().getString(R.string.app_name));
        MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(url), new DefaultDataSourceFactory
                (getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
        player.prepare(mediaSource);
        player.setPlayWhenReady(false);
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
