package com.rdev.freetube.helpers;

import android.content.pm.ActivityInfo;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.customui.utils.FadeViewHelper;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.customui.views.YouTubePlayerSeekBar;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.rdev.freetube.MainActivity;
import com.rdev.freetube.R;

public class PlayerUiController{

  private final YouTubePlayerView youTubePlayerView;
  private final YouTubePlayer youTubePlayer;
  private final View rootView;
  private final View panel;

  private final View controlsContainer;
  private final LinearLayout extraViewsContainer;
  private final TextView videoTitle;
  private final TextView liveVideoIndicator;
  private final ImageView menuButton;
  private final ImageView playPauseButton;
  private final ImageView fullscreenButton;
  private final ImageView customActionLeft;
  private final ImageView customActionRight;
  private final YouTubePlayerSeekBar youtubePlayerSeekBar;
  private final FadeViewHelper fadeControlsContainer;
  private View.OnClickListener onFullscreenButtonListener;
  private boolean isPlaying = false;
  private boolean isPlayPauseButtonEnabled = true;
  private boolean isCustomActionLeftEnabled = false;
  private boolean isCustomActionRightEnabled = false;
  private boolean isMatchParent = false;

  MainActivity mainActivity;

  private final AbstractYouTubePlayerListener youTubePlayerStateListener = new AbstractYouTubePlayerListener() {

    public void onStateChange(YouTubePlayer youTubePlayer, PlayerConstants.PlayerState state) {
      updateState(state);

      if (state == PlayerConstants.PlayerState.PLAYING || state == PlayerConstants.PlayerState.PAUSED || state == PlayerConstants.PlayerState.VIDEO_CUED) {
        panel.setBackgroundColor(ContextCompat.getColor(panel.getContext(), android.R.color.transparent));

        if (isPlayPauseButtonEnabled) playPauseButton.setVisibility(View.VISIBLE);
        if (isCustomActionLeftEnabled) customActionLeft.setVisibility(View.VISIBLE);
        if (isCustomActionRightEnabled) customActionRight.setVisibility(View.VISIBLE);

        updatePlayPauseButtonIcon(state == PlayerConstants.PlayerState.PLAYING);

      } else {
        updatePlayPauseButtonIcon(false);

        if (state == PlayerConstants.PlayerState.BUFFERING) {
          panel.setBackgroundColor(ContextCompat.getColor(panel.getContext(), android.R.color.transparent));
          if (isPlayPauseButtonEnabled) playPauseButton.setVisibility(View.INVISIBLE);

          customActionLeft.setVisibility(View.GONE);
          customActionRight.setVisibility(View.GONE);
        }

        if (state == PlayerConstants.PlayerState.UNSTARTED) {
          if (isPlayPauseButtonEnabled) playPauseButton.setVisibility(View.VISIBLE);
        }
      }
    }

  };

  public PlayerUiController(MainActivity mainActivity,YouTubePlayerView youTubePlayerView, YouTubePlayer youTubePlayer) {
    this.youTubePlayerView = youTubePlayerView;
    this.youTubePlayer = youTubePlayer;
    this.rootView = View.inflate(youTubePlayerView.getContext(), R.layout.player_view, null);
    this.panel = rootView.findViewById(R.id.panel);
    this.controlsContainer = rootView.findViewById(R.id.controls_container);
    this.extraViewsContainer = rootView.findViewById(R.id.extra_views_container);
    this.videoTitle = rootView.findViewById(R.id.video_title);
    this.liveVideoIndicator = rootView.findViewById(R.id.live_video_indicator);
    this.menuButton = rootView.findViewById(R.id.menu_button);
    this.playPauseButton = rootView.findViewById(R.id.play_pause_button);
    this.fullscreenButton = rootView.findViewById(R.id.fullscreen_button);
    this.customActionLeft = rootView.findViewById(R.id.custom_action_left_button);
    this.customActionRight = rootView.findViewById(R.id.custom_action_right_button);
    this.youtubePlayerSeekBar = rootView.findViewById(R.id.youtube_player_seekbar);
    this.fadeControlsContainer = new FadeViewHelper(controlsContainer);

    this.mainActivity = mainActivity;
    this.onFullscreenButtonListener = v -> {
      mainActivity.hideBottomNavigation();
      isMatchParent = !isMatchParent;
      if (isMatchParent) {
        mainActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        youTubePlayerView.matchParent();
      } else {
        mainActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mainActivity.showBottomNavigation();
        youTubePlayerView.wrapContent();
      }
    };


    initClickListeners();
  }

  private void initClickListeners() {
    youTubePlayer.addListener(youtubePlayerSeekBar);
    youTubePlayer.addListener(youTubePlayerStateListener);

    youtubePlayerSeekBar.setYoutubePlayerSeekBarListener(time -> youTubePlayer.seekTo(time));
    panel.setOnClickListener(v -> fadeControlsContainer.toggleVisibility());
    playPauseButton.setOnClickListener(v -> onPlayButtonPressed());
    fullscreenButton.setOnClickListener(v -> onFullscreenButtonListener.onClick(fullscreenButton));
  }

  public View getRootView() {
    return rootView;
  }

  private void onPlayButtonPressed() {
    if (isPlaying) {
      youTubePlayer.pause();
    } else {
      youTubePlayer.play();
    }
  }

  private void updateState(PlayerConstants.PlayerState state) {
    switch (state) {
      case ENDED:
      case PAUSED:
        isPlaying = false;
        break;
      case PLAYING:
        isPlaying = true;
        break;
      default:
        break;
    }
    updatePlayPauseButtonIcon(!isPlaying);
  }

  private void updatePlayPauseButtonIcon(boolean playing) {
    int drawable = playing ? R.drawable.ayp_ic_pause_36dp : R.drawable.ayp_ic_play_36dp;
    playPauseButton.setImageResource(drawable);
  }
}
