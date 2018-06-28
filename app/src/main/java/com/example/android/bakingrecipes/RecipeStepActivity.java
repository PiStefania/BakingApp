package com.example.android.bakingrecipes;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.bakingrecipes.Objects.DetailRecipe;
import com.example.android.bakingrecipes.Utils.VariousMethods;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeStepActivity extends AppCompatActivity implements View.OnClickListener, ExoPlayer.EventListener {

    @BindView(R.id.instructions_step_recipe)
    TextView instructions;
    @BindView(R.id.no_video_found)
    ImageView noVideoImageView;
    @BindView(R.id.previous_section)
    Button previousStep;
    @BindView(R.id.next_section)
    Button nextStep;
    @BindView(R.id.footer_buttons) LinearLayout footerButtons;
    @BindView(R.id.details_view) LinearLayout detailView;
    private SimpleExoPlayer mExoPlayer;
    @BindView(R.id.video_recipe_step)
    SimpleExoPlayerView mPlayerView;
    private static MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;

    private static final String LOG_TAG = RecipeStepActivity.class.getSimpleName();
    private static final String RECIPE_STEPS_EXTRA = "RecipeSteps";
    private static final String RECIPE_STEPS_POSITION_EXTRA = "RecipeStepPosition";
    private static final String RECIPE_STEPS_ALL_STEPS_EXTRA = "RecipeStepAllSteps";
    private static final String RECIPE_STEPS_NEXT_STEP_EXTRA = "RecipeStepNext";
    private static final String RECIPE_STEPS_PREVIOUS_STEP_EXTRA = "RecipeStepPrevious";

    private ArrayList<DetailRecipe> detailRecipes;
    private int sizeSteps;
    private DetailRecipe detailRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step);
        ButterKnife.bind(this);

        //get recipe step details
        detailRecipe = getIntent().getParcelableExtra(RECIPE_STEPS_EXTRA);
        int positionStep = getIntent().getIntExtra(RECIPE_STEPS_POSITION_EXTRA, -1);
        DetailRecipe detailRecipePrevious = getIntent().getParcelableExtra(RECIPE_STEPS_PREVIOUS_STEP_EXTRA);
        DetailRecipe detailRecipeNext = getIntent().getParcelableExtra(RECIPE_STEPS_NEXT_STEP_EXTRA);
        detailRecipes = getIntent().getParcelableArrayListExtra(RECIPE_STEPS_ALL_STEPS_EXTRA);
        sizeSteps = detailRecipes.size();

        //set title of toolbar
        this.setTitle(detailRecipe.getDetailTitle());

        if (!detailRecipe.getDetailVideo().isEmpty() || !detailRecipe.getDetailVideo().equals("")) {
            //initialize Media Session.
            initializeMediaSession();
            //initialize player.
            initializePlayer(Uri.parse(detailRecipe.getDetailVideo()));
            noVideoImageView.setVisibility(View.GONE);
        } else {
            mPlayerView.setVisibility(View.GONE);
            noVideoImageView.setVisibility(View.VISIBLE);
        }

        instructions.setText(detailRecipe.getDetailInstructions());

        populateButtons(positionStep, detailRecipePrevious, detailRecipeNext);
    }

    private void initializeMediaSession() {

        //create a MediaSessionCompat
        mMediaSession = new MediaSessionCompat(this, LOG_TAG);

        //enable callbacks
        mMediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS | MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        //don't restart
        mMediaSession.setMediaButtonReceiver(null);

        //set an initial PlaybackState
        mStateBuilder = new PlaybackStateCompat.Builder().setActions(
                PlaybackStateCompat.ACTION_PLAY |
                        PlaybackStateCompat.ACTION_PAUSE |
                        PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                        PlaybackStateCompat.ACTION_PLAY_PAUSE);

        mMediaSession.setPlaybackState(mStateBuilder.build());


        //MySessionCallback has methods that handle callbacks from a media controller.
        mMediaSession.setCallback(new MySessionCallback());

        //start the Media Session
        mMediaSession.setActive(true);

    }

    private void initializePlayer(Uri mediaUri) {
        if (mExoPlayer == null) {
            //create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector, loadControl);
            mPlayerView.setPlayer(mExoPlayer);
            mExoPlayer.addListener(this);

            //prepare MediaSource for .mp4
            String userAgent = Util.getUserAgent(this, "BakingRecipes");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(this, userAgent), new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);
        }
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if ((playbackState == ExoPlayer.STATE_READY) && playWhenReady) {
            mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                    mExoPlayer.getCurrentPosition(), 1f);
        } else if ((playbackState == ExoPlayer.STATE_READY)) {
            mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                    mExoPlayer.getCurrentPosition(), 1f);
        }
        mMediaSession.setPlaybackState(mStateBuilder.build());
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }

    private void releasePlayer() {
        mExoPlayer.stop();
        mExoPlayer.release();
        mExoPlayer = null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!detailRecipe.getDetailVideo().isEmpty() || !detailRecipe.getDetailVideo().equals("")) {
            releasePlayer();
            mMediaSession.setActive(false);
        }
    }

    private class MySessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            mExoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            mExoPlayer.setPlayWhenReady(false);
        }

        @Override
        public void onSkipToPrevious() {
            mExoPlayer.seekTo(0);
        }
    }

    private void populateButtons(int position, final DetailRecipe previous, final DetailRecipe next) {
        if (position == 0) {
            previousStep.setVisibility(View.GONE);
            nextStep.setText(next.getDetailTitle());
            nextStep.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int positionStep = VariousMethods.findPositionClickedStep(next, detailRecipes);
                    Intent recipeStep = new Intent(RecipeStepActivity.this, RecipeStepActivity.class);
                    recipeStep.putExtra(RECIPE_STEPS_EXTRA, next);
                    recipeStep.putExtra(RECIPE_STEPS_POSITION_EXTRA, positionStep);
                    recipeStep.putExtra(RECIPE_STEPS_ALL_STEPS_EXTRA, detailRecipes);
                    if (positionStep == 0) {
                        recipeStep.putExtra(RECIPE_STEPS_NEXT_STEP_EXTRA, detailRecipes.get(positionStep + 1));
                    } else if (positionStep == detailRecipes.size() - 1) {
                        recipeStep.putExtra(RECIPE_STEPS_PREVIOUS_STEP_EXTRA, detailRecipes.get(positionStep - 1));
                    } else {
                        recipeStep.putExtra(RECIPE_STEPS_PREVIOUS_STEP_EXTRA, detailRecipes.get(positionStep - 1));
                        recipeStep.putExtra(RECIPE_STEPS_NEXT_STEP_EXTRA, detailRecipes.get(positionStep + 1));
                    }
                    finish();
                    startActivity(recipeStep);
                }
            });
        } else if (position == sizeSteps - 1) {
            nextStep.setVisibility(View.GONE);
            previousStep.setText(previous.getDetailTitle());
            previousStep.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int positionStep = VariousMethods.findPositionClickedStep(previous, detailRecipes);
                    Intent recipeStep = new Intent(RecipeStepActivity.this, RecipeStepActivity.class);
                    recipeStep.putExtra(RECIPE_STEPS_EXTRA, previous);
                    recipeStep.putExtra(RECIPE_STEPS_POSITION_EXTRA, positionStep);
                    recipeStep.putExtra(RECIPE_STEPS_ALL_STEPS_EXTRA, detailRecipes);
                    if (positionStep == 0) {
                        recipeStep.putExtra(RECIPE_STEPS_NEXT_STEP_EXTRA, detailRecipes.get(positionStep + 1));
                    } else if (positionStep == detailRecipes.size() - 1) {
                        recipeStep.putExtra(RECIPE_STEPS_PREVIOUS_STEP_EXTRA, detailRecipes.get(positionStep - 1));
                    } else {
                        recipeStep.putExtra(RECIPE_STEPS_PREVIOUS_STEP_EXTRA, detailRecipes.get(positionStep - 1));
                        recipeStep.putExtra(RECIPE_STEPS_NEXT_STEP_EXTRA, detailRecipes.get(positionStep + 1));
                    }
                    finish();
                    startActivity(recipeStep);
                }
            });
        } else {
            previousStep.setText(previous.getDetailTitle());
            previousStep.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int positionStep = VariousMethods.findPositionClickedStep(previous, detailRecipes);
                    Intent recipeStep = new Intent(RecipeStepActivity.this, RecipeStepActivity.class);
                    recipeStep.putExtra(RECIPE_STEPS_EXTRA, previous);
                    recipeStep.putExtra(RECIPE_STEPS_POSITION_EXTRA, positionStep);
                    recipeStep.putExtra(RECIPE_STEPS_ALL_STEPS_EXTRA, detailRecipes);
                    if (positionStep == 0) {
                        recipeStep.putExtra(RECIPE_STEPS_NEXT_STEP_EXTRA, detailRecipes.get(positionStep + 1));
                    } else if (positionStep == detailRecipes.size() - 1) {
                        recipeStep.putExtra(RECIPE_STEPS_PREVIOUS_STEP_EXTRA, detailRecipes.get(positionStep - 1));
                    } else {
                        recipeStep.putExtra(RECIPE_STEPS_PREVIOUS_STEP_EXTRA, detailRecipes.get(positionStep - 1));
                        recipeStep.putExtra(RECIPE_STEPS_NEXT_STEP_EXTRA, detailRecipes.get(positionStep + 1));
                    }
                    finish();
                    startActivity(recipeStep);
                }
            });
            nextStep.setText(next.getDetailTitle());
            nextStep.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int positionStep = VariousMethods.findPositionClickedStep(next, detailRecipes);
                    Intent recipeStep = new Intent(RecipeStepActivity.this, RecipeStepActivity.class);
                    recipeStep.putExtra(RECIPE_STEPS_EXTRA, next);
                    recipeStep.putExtra(RECIPE_STEPS_POSITION_EXTRA, positionStep);
                    recipeStep.putExtra(RECIPE_STEPS_ALL_STEPS_EXTRA, detailRecipes);
                    if (positionStep == 0) {
                        recipeStep.putExtra(RECIPE_STEPS_NEXT_STEP_EXTRA, detailRecipes.get(positionStep + 1));
                    } else if (positionStep == detailRecipes.size() - 1) {
                        recipeStep.putExtra(RECIPE_STEPS_PREVIOUS_STEP_EXTRA, detailRecipes.get(positionStep - 1));
                    } else {
                        recipeStep.putExtra(RECIPE_STEPS_PREVIOUS_STEP_EXTRA, detailRecipes.get(positionStep - 1));
                        recipeStep.putExtra(RECIPE_STEPS_NEXT_STEP_EXTRA, detailRecipes.get(positionStep + 1));
                    }
                    finish();
                    startActivity(recipeStep);
                }
            });
        }
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            footerButtons.setVisibility(View.GONE);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            mPlayerView.setLayoutParams(layoutParams);
            detailView.setPadding(0,0,0,0);
            if (getSupportActionBar() != null) {
                getSupportActionBar().hide();
            }
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            footerButtons.setVisibility(View.VISIBLE);
            if (getSupportActionBar() != null) {
                getSupportActionBar().show();
            }
            int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200, getResources().getDisplayMetrics());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
            mPlayerView.setLayoutParams(layoutParams);
            detailView.setPadding(8,8,8,0);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN  | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
        super.onConfigurationChanged(newConfig);
    }
}
